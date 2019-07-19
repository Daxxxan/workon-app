package com.workon.plugin;

import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;


public class PluginLoader {
    private String dirPath;

    public PluginLoader(){
        this.dirPath = "src/main/resources/pluginsWorkon/";

    }
    public PluginLoader(String path) {
        this.dirPath = path;
    }

    public Object loadPlugin(String jarFile) throws Exception {
        String path = this.dirPath + jarFile;
        Path file = Paths.get(path);
        URL loadPath = file.toUri().toURL();
        URL[] classUrl = new URL[]{loadPath};

        ClassLoader cl = new URLClassLoader(classUrl);

        InputStream stream = loadPath.openStream();
        JarInputStream jarStream = new JarInputStream(stream);
        Manifest mf = jarStream.getManifest();
        String className = mf.getMainAttributes().getValue("Main-Class");
        Class loadedClass = cl.loadClass(className);
        return loadedClass.getConstructor().newInstance();
    }
}