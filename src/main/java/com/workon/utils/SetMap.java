package com.workon.utils;

import com.workon.utils.parser.AnnotationParser;
import com.workon.utils.parser.NoNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SetMap {
    /**
     * Set une map avec deux ArrayList la premiere etant la cle et la seconde la valeur
     *
     * @param first
     *        Premiere ArrayList designant la cle
     * @param second
     *        Deuxieme ArrayList designant la valeur
     * @return Map String String
     */
    public static Map<String, String> setStringStringMapWithArrayLists(@NoNull ArrayList<String> first, @NoNull ArrayList<String> second){
        AnnotationParser.parse(first, second);
        Map<String, String> map = new HashMap<>();
        if(first.size() == second.size()){
            for(int counter = 0; counter < first.size(); counter++){
                map.put(first.get(counter), second.get(counter));
            }
            return map;
        }else{
            return null;
        }
    }
}
