/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.common.extension;

import com.solid4j.xmen.common.compiler.Compiler;
import com.solid4j.xmen.common.rpc.URL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
/**
 * @author solidwang
 * @since 1.0
 */
public class ExtensionLoader<T> {

    private static final String SERVICES_DIRECTORY = "META-INF/services/";

    // 加载的类型type
    private Class<T> type;
    // 存放class<?>的单例ExtensionLoader
    private static final ConcurrentMap<Class<?>, ExtensionLoader<?>> EXTENSION_LOADER = new ConcurrentHashMap<Class<?>, ExtensionLoader<?>>();
    // 存放SPI的实例化对象
    private static final ConcurrentMap<String, Object> CACHE_INSTANCE = new ConcurrentHashMap<String, Object>();
    // 存放AdaptiveClass
    private volatile Class<?> cachedAdaptiveClass = null;
    // 存放默认名称
    private String cachedDefaultName;

    private ExtensionLoader(Class<T> type) {
        this.type = type;
    }

    @SuppressWarnings("unchecked")
    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
        checkType(type);
        // 获取单例ExtensionLoader
        ExtensionLoader<T> loader = ((ExtensionLoader<T>) EXTENSION_LOADER.get(type));
        if (loader == null) {
            EXTENSION_LOADER.putIfAbsent(type, new ExtensionLoader<T>(type));
            loader = (ExtensionLoader<T>) EXTENSION_LOADER.get(type);
        }
        return loader;
    }

    @SuppressWarnings({ "unchecked", "hiding" })
    public <T> T getExtension(String name) {
        Object instance = CACHE_INSTANCE.get(name);
        if (instance == null) {
            synchronized (this) {
                instance = createExtension(name);
                CACHE_INSTANCE.putIfAbsent(name, instance);
            }
        }
        return (T) instance;
    }

    @SuppressWarnings("hiding")
    public <T> T getDefaultExtension() {
        loadExtensionClasses();
        if (cachedDefaultName == null || cachedDefaultName.length() == 0) { return null; }
        return getExtension(cachedDefaultName);
    }

    @SuppressWarnings("unchecked")
    public T getAdaptiveExtension() {
        Object instance = null;
        if (instance == null) {
            instance = createAdaptiveExtension();
        }
        return (T) instance;
    }

    @SuppressWarnings("unchecked")
    private T createAdaptiveExtension() {
        try {
            return (T) getAdaptiveExtensionClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException("createAdaptiveExtension error, " + e.getMessage());
        }
    }

    private Class<?> getAdaptiveExtensionClass() {
        loadExtensionClasses();
        if (cachedAdaptiveClass != null)
            return cachedAdaptiveClass;
        return cachedAdaptiveClass = createAdaptiveExtendsionClass();
    }

    private Class<?> createAdaptiveExtendsionClass() {
        String code = createAdaptiveExtendsionClassCode();
        Compiler compiler = ExtensionLoader.getExtensionLoader(Compiler.class).getAdaptiveExtension();
        return compiler.compile(code);
    }

    private String createAdaptiveExtendsionClassCode() {
        StringBuilder sb = new StringBuilder();
        Method[] methods = type.getMethods();
        boolean hasAdaptiveAnnotation = false;
        for (Method m : methods) {
            if (m.isAnnotationPresent(Adaptive.class)) {
                hasAdaptiveAnnotation = true;
                break;
            }
        }
        if (!hasAdaptiveAnnotation)
            throw new IllegalStateException("No Adaptive annotation on " + type.getName() + ", refuse create Adaptive!");
        // 定义package、import、class名称
        sb.append("package " + type.getPackage().getName() + ";");
        sb.append("\nimport " + ExtensionLoader.class.getName() + ";");
        sb.append("\npublic class " + type.getSimpleName() + "$Adaptive" + " implements " + type.getCanonicalName() + " {\n");

        // 定义method，没有注解@Adaptive的方法抛出异常
        for (Method method : methods) {
            Class<?> returnType = method.getReturnType();
            Class<?>[] paramTypes = method.getParameterTypes();
            method.getParameterTypes();
            StringBuilder code = new StringBuilder();
            Adaptive apaptiveAnnotation = method.getAnnotation(Adaptive.class);
            if (apaptiveAnnotation == null) {
                // 无@Adaptive注解的方法没有方法体

            } else {
                // 判断参数中是否有URL
                String key = apaptiveAnnotation.value();
                int urlTypeIndex = -1;
                for (int i = 0; i < paramTypes.length; i++) {
                    if (paramTypes[i].equals(URL.class)) {
                        urlTypeIndex = i;
                        break;
                    }
                }
                // 如果参数中存在URL
                if (urlTypeIndex != -1) {
                    String s = String.format("%s url = arg%d;\n", URL.class.getName(), urlTypeIndex);
                    code.append(s);
                } else {
                    // 如果参数中不存在URL，遍历参数的方法，寻找URL
                    String attributeMethod = null;
                    for (int i = 0; i < paramTypes.length; i++) {
                        Method[] ms = paramTypes[i].getMethods();
                        for (Method m : ms) {
                            String name = m.getName();
                            if (name.startsWith("get") && m.getParameterTypes().length == 0 && m.getReturnType() == URL.class) {
                                urlTypeIndex = i;
                                attributeMethod = name;
                            }
                        }
                    }
                    if (attributeMethod == null)
                        throw new IllegalStateException("fail to create adative class for interface " + type.getName()
                                + ": not found url parameter or url attribute in parameters of method " + method.getName());
                    String s = String.format("%s url = arg%d.%s();\n", URL.class.getName(), urlTypeIndex, attributeMethod);
                    code.append(s);
                }
                // 获取到extension的名称

                //System.out.println("value=" + value);
                String extName = "";
                if (key == null || key.length()==0) {
                    extName = "(url.getProtocol() == null ? \"xmen\" : url.getProtocol())";
                } else {
                    extName = String.format("url.getParameter(\"%s\", \"%s\")", key, cachedDefaultName);
                }
                code.append("String extName=" + extName + ";");
                String s = String.format("\n%s extension = (%<s)%s.getExtensionLoader(%s.class).getExtension(extName);", type.getName(),
                        ExtensionLoader.class.getSimpleName(), type.getName());
                code.append(s);

                // 返回值
                if (!returnType.equals(void.class)) {
                    code.append("\nreturn ");
                }
                s = String.format("extension.%s(", method.getName());
                code.append(s);
                for (int i = 0; i < paramTypes.length; i++) {
                    if (i != 0)
                        code.append(", ");
                    code.append("arg").append(i);
                }
                code.append(");");
            }
            sb.append("public " + returnType.getCanonicalName() + " " + method.getName() + "(");
            for (int i = 0; i < paramTypes.length; i++) {
                if (i > 0)
                    sb.append(", ");
                sb.append(paramTypes[i].getCanonicalName());
                sb.append(" arg" + i);
            }
            sb.append(") ");
            sb.append("{\n");
            sb.append(code);
            sb.append("\n}\n");
        }
        sb.append("}");
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    private T createExtension(String name) {
        Map<String, Class<?>> map = loadExtensionClasses();
        Class<?> cls = map.get(name);
        T t = null;
        try {
            t = (T) cls.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return t;
    }

    private Map<String, Class<?>> loadExtensionClasses() {
        final SPI defalutAnnotation = type.getAnnotation(SPI.class);
        if (defalutAnnotation != null) {
            String value = defalutAnnotation.value();
            if (value != null && value.length() != 0) {
                cachedDefaultName = value;
            }
        }
        String fileName = SERVICES_DIRECTORY + type.getName();
        Map<String, Class<?>> map = new HashMap<String, Class<?>>();
        Enumeration<java.net.URL> urls = null;
        try {
            urls = ExtensionLoader.class.getClassLoader().getResources(fileName);
            while (urls.hasMoreElements()) {
                java.net.URL url = urls.nextElement();
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    int end = line.indexOf("#");
                    if (end >= 0)
                        line = line.substring(0, end).trim();
                    String name = line.split("=")[0];
                    String value = line.split("=")[1];
                    Class<?> clazz = Class.forName(value);
                    if (clazz.isAnnotationPresent(Adaptive.class)) {
                        if (cachedAdaptiveClass == null)
                            cachedAdaptiveClass = clazz;
                    } else {
                        map.put(name, clazz);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return map;
    }

    private static <T> void checkType(Class<T> type) {
        if (type == null)
            throw new IllegalArgumentException("extension type==null");
        if (!type.isInterface())
            throw new IllegalArgumentException("type is not interface");
        if (!withSPIAnnotation(type))
            throw new IllegalArgumentException("this type:" + type.getName() + " without @SPI Annotation");
    }

    private static <T> boolean withSPIAnnotation(Class<T> type) {
        return type.isAnnotationPresent(SPI.class);
    }

}
