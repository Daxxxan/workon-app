package com.workon.plugin;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;


public class PluginLoader {
    public PluginLoader() throws Exception {
        File dir = new File("src/main/resources/pluginsWorkon/workonPlugin.jar");
        URL loadPath = dir.toURI().toURL();
        URL[] classUrl = new URL[]{loadPath};

        ClassLoader cl = new URLClassLoader(classUrl);

        Class<?> loadedClass = cl.loadClass("com.workon.plugin.print"); // must be in package.class name format
        Object object = loadedClass.getConstructor().newInstance();
        printInterface printInterface = (com.workon.plugin.printInterface) object;
        printInterface.printHello();
    }
}