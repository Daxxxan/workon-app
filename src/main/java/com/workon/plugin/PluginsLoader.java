package com.workon.plugin;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarFile;

public class PluginsLoader {
    private String[] files;
    private ArrayList<Class> classCoffeePlugins;

    public PluginsLoader(String[] files){
        this.classCoffeePlugins = new ArrayList<>();
        this.files = files;
    }

    public CoffeePluginInterface[] loadAllStringPlugins() throws Exception {

        this.initializeLoader();

        CoffeePluginInterface[] tmpPlugins = new CoffeePluginInterface[this.classCoffeePlugins.size()];

        for(int index = 0 ; index < tmpPlugins.length; index ++ ){

            //On créer une nouvelle instance de l'objet contenu dans la liste grâce à newInstance()
            //et on le cast en StringPlugins. Vu que la classe implémente StringPlugins, le cast est toujours correct
            tmpPlugins[index] = (CoffeePluginInterface)(this.classCoffeePlugins.get(index)).newInstance() ;

        }

        return tmpPlugins;
    }

    private void initializeLoader() throws Exception{
        //On vérifie que la liste des pluginsWorkon à charger à été initialisé
        if(this.files == null || this.files.length == 0 ){
            throw new Exception("Pas de fichier spécifié");
        }

        //Pour eviter le double chargement des pluginsWorkon
        if(this.classCoffeePlugins.size() != 0 ){
            return ;
        }

        File[] f = new File[this.files.length];
//		Pour charger le .jar en memoire
        URLClassLoader loader;
        //Pour la comparaison de chaines
        String tmp = "";
        //Pour le contenu de l'archive jar
        Enumeration<java.util.jar.JarEntry> enumeration;
        //Pour déterminé quels sont les interfaces implémentées
        Class tmpClass = null;

        for(int index = 0 ; index < f.length ; index ++ ){

            f[index] = new File(this.files[index]);

            if( !f[index].exists() ) {
                break;
            }

            URL u = f[index].toURL();
            //On créer un nouveau URLClassLoader pour charger le jar du plugin à charger
            loader = new URLClassLoader(new URL[] {u});

            //On charge le jar en mémoire
            JarFile jar = new JarFile(f[index].getAbsolutePath());

            //On récupére le contenu du jar
            enumeration = jar.entries();

            while(enumeration.hasMoreElements()){

                tmp = enumeration.nextElement().toString();

                //On vérifie que le fichier courant est un .class (et pas un fichier d'informations du jar )
                if(tmp.length() > 6 && tmp.substring(tmp.length()-6).compareTo(".class") == 0) {

                    tmp = tmp.substring(0,tmp.length()-6);
                    tmp = tmp.replaceAll("/",".");

                    tmpClass = Class.forName(tmp ,true,loader);

                    for(int i = 0 ; i < tmpClass.getInterfaces().length; i ++ ){

                        //Une classe ne doit pas appartenir à deux catégories de pluginsWorkon différents.
                        //Si tel est le cas on ne la place que dans la catégorie de la première interface correct
                        // trouvée
                        if(tmpClass.getInterfaces()[i].getName().equals("com.workon.plugin.coffee.interfaces.CoffeePluginInterface") ) {
                            boolean add = this.classCoffeePlugins.add(tmpClass);
                        }
                    }

                }
            }


        }

    }



}
