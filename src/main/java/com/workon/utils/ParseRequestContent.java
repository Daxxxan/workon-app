package com.workon.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.workon.utils.parser.AnnotationParser;
import com.workon.utils.parser.NoNull;

import java.io.IOException;
import java.util.ArrayList;

public class ParseRequestContent {
    /**
     * Parser json pour recuperer la valeur d'un cle dans un JSON
     *
     * @param content
     *        Contenu JSON
     * @param key
     *        Cle pour recuperer la valeur
     * @return String
     */
    public static String getValueOf(String content, String key){
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonContent = null;
        try {
            jsonContent = objectMapper.readTree(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(jsonContent != null){
            JsonNode jsonObject = jsonContent.get(key);
            return jsonObject.toString();
        }else{
            return null;
        }
    }

    /**
     * Parser JSON pour recuperer une ArrayList de cle dans un contenu JSON
     *
     * @param content
     *        Contenu JSON
     * @param key
     *        Cle pour recuperer la valeur
     * @return ArrayList String
     */
    public static ArrayList<String> getValuesOf(@NoNull String content, @NoNull String key){
        AnnotationParser.parse(content, key);
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<String> contents = new ArrayList<>();
        JsonNode jsonContent = null;
        try {
            jsonContent = objectMapper.readTree(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(jsonContent != null){
            for(JsonNode jsonNode : jsonContent){
                contents.add(jsonNode.get(key).toString());
            }
        }
        return contents;
    }
}
