package com.workon.utils;

import com.workon.utils.parser.AnnotationParser;
import com.workon.utils.parser.NoNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SetMap {
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
