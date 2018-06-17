package com.workon.utils;

public class ParseRequestContent {
    public static String getValueOf(StringBuffer content, String key){
        String stringBufferContentToString = content.toString();
        String[] bufferParts = stringBufferContentToString.split(",");

        for (String bufferPart : bufferParts) {
            String[] keyValue = bufferPart.split(":");
            if(key.equals(keyValue[0])){
                return  keyValue[1];
            }
        }
        return null;
    }
}
