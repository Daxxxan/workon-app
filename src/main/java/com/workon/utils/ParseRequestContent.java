package com.workon.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.util.ArrayList;

public class ParseRequestContent {
    public static String getValueOf(String content, String key) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonContent = objectMapper.readTree(content);
        JsonNode jsonObject = jsonContent.get(key);
        return jsonObject.toString();
    }

    public static ArrayList<String> getValuesOf(String content, String key) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<String> contents = new ArrayList<>();
        JsonNode jsonContent = objectMapper.readTree(content);

        for(JsonNode jsonNode : jsonContent){
            contents.add(jsonNode.get(key).toString());
        }

        return contents;
    }
}
