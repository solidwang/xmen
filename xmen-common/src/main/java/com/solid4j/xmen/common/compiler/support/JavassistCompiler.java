/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.common.compiler.support;

import javassist.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author solidwang
 * @since 1.0
 */
public class JavassistCompiler extends AbstractCompiler {

    private static final Pattern IMPLEMENTS_PATTERN = Pattern.compile("\\s+implements\\s+([\\w\\.]+)\\s*\\{\n");
    private static final Pattern METHODS_PATTERN = Pattern.compile("(private|public|protected)\\s+");
    
    @Override
    protected Class<?> doCompile(String name, String source) {
        ClassPool pool = ClassPool.getDefault();
        pool.importPackage("com.solid4j.xmen.common.extension.ExtensionLoader");
        // 通过ClassPool生成一个新类
        CtClass ctClass = pool.makeClass(name);
        Matcher matcher = IMPLEMENTS_PATTERN.matcher(source);
        if (matcher.find()) {
            String[] ifaces = matcher.group(1).trim().split("\\,");
            for (String iface : ifaces) {
                iface = iface.trim();
                try {
                    ClassClassPath classPath = new ClassClassPath(this.getClass());
                    pool.insertClassPath(classPath);
                    ctClass.addInterface(pool.get(iface));
                } catch (NotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        String body = source.substring(source.indexOf("{") + 1, source.length() - 1);
        String[] methods = METHODS_PATTERN.split(body);
        for (String method : methods) {
            try {
                method = method.trim();
                if(method.length()!=0) {
                    ctClass.addMethod(CtNewMethod.make("public " + method, ctClass));
                }
            } catch (CannotCompileException e) {
                e.printStackTrace();
            }
        }
        Class<?> clazz = null;
        try {
            clazz = ctClass.toClass();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
        return clazz;
    }
}
