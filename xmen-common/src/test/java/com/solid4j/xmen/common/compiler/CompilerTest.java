/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.common.compiler;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;

import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;


/**
 * @author: solidwang
 * @date: 17/6/5
 */
public class CompilerTest {

    @Test
    public void generateNewClassByJavassist() throws CannotCompileException, NotFoundException, InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {
        // ClassPool
        ClassPool pool = ClassPool.getDefault();
        // 通过ClassPool生成一个新类Test.java
        CtClass ctClass = pool.makeClass("com.solid4j.xmen.common.compiler.Test");
        ctClass.addInterface(pool.get("java.util.List"));

        // 添加字段
        CtField nameField = new CtField(pool.getCtClass("java.lang.String"), "name", ctClass);
        nameField.setModifiers(Modifier.PRIVATE);
        ctClass.addField(nameField);
        CtField noField = new CtField(pool.getCtClass("int"), "no", ctClass);
        noField.setModifiers(Modifier.PRIVATE);
        ctClass.addField(noField);

        // 为字段添加get和set方法
        ctClass.addMethod(CtNewMethod.getter("getName", nameField));
        ctClass.addMethod(CtNewMethod.setter("setName", nameField));
        ctClass.addMethod(CtNewMethod.getter("getNo", noField));
        ctClass.addMethod(CtNewMethod.setter("setNo", noField));

        // 添加构造函数
        CtConstructor ctConstructor = new CtConstructor(new CtClass[] {}, ctClass);
        // 为构造函数设置函数体
        StringBuilder sb = new StringBuilder();
        sb.append("{\n").append("name=\"test\";\n").append("no=100;\n").append("}");
        ctConstructor.setBody(sb.toString());
        ctClass.addConstructor(ctConstructor);

        // 添加自定义方法
        CtMethod ctMethod = new CtMethod(CtClass.voidType, "printInfo", new CtClass[] {}, ctClass);
        ctMethod.setModifiers(Modifier.PUBLIC);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("{\nSystem.out.println(\"begin!\");\n").append("System.out.println(name);\n").append("System.out.println(no);\n")
                .append("System.out.println(\"over!\");\n").append("}");
        ctMethod.setBody(sb2.toString());
        ctClass.addMethod(ctMethod);

        Class<?> clazz = ctClass.toClass();
        Object obj = clazz.newInstance();
        obj.getClass().getMethod("printInfo", new Class[] {}).invoke(obj, new Object[] {});

        // 把生成的class文件写入文件
        byte[] byteArr = ctClass.toBytecode();
        FileOutputStream fos = new FileOutputStream(new File("/Users/solidwang/Documents/Test.class"));
        fos.write(byteArr);
        fos.close();
    }
}
