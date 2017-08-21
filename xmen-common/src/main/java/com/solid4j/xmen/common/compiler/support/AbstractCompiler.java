/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.common.compiler.support;

import com.solid4j.xmen.common.compiler.Compiler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author solidwang
 * @since 1.0
 */
public abstract class AbstractCompiler implements Compiler {

    private static final Pattern PACKAGE_PATTERN = Pattern.compile("package\\s+([$_a-zA-Z][$_a-zA-Z0-9\\.]*);");
    private static final Pattern CLASS_PATTERN = Pattern.compile("class\\s+([$_a-zA-Z][$_a-zA-Z0-9]*)\\s+");

    public Class<?> compile(String code) {
        Matcher matcher = PACKAGE_PATTERN.matcher(code);
        String pkg;
        if (matcher.find()) {
            pkg = matcher.group(1);
        } else {
            pkg = "";
        }
        String clazz = "";
        matcher = CLASS_PATTERN.matcher(code);
        if (matcher.find()) {
            clazz = matcher.group(1);
        } else {
            throw new IllegalArgumentException("No such class name in " + code);
        }
        String className = ("".equals(pkg) || pkg.length() == 0 ? clazz : pkg + "." + clazz);
        return doCompile(className, code);
    }

    protected abstract Class<?> doCompile(String name, String source);
}
