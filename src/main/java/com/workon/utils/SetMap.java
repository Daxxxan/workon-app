package com.workon.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SetMap {
    public static Map<String, String> setStringStringMapWithArrayLists(ArrayList<String> first, ArrayList<String> second){
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
