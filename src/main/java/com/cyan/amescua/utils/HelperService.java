package com.cyan.amescua.utils;

import com.cyan.amescua.model.Feed;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelperService {

    /**
     * Parse a JSON list of items and turn them into a List of Java Objects of your choice
     * In this case is Used to get a List of Feed objects.
     * @param json
     * @return List<Feed>
     */
    public static List<Feed> jsonToList(String json) {
        try {
            return new ObjectMapper().readValue(json, new TypeReference<List<Feed>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Parse a Java Object into a JSON String (to store it in the Database)
     * @param o
     * @return
     */
    public static String toJson(Object o) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(o);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    /**
     * Checks if the word is contained in the given sSring
     * @param source
     * @param subItem
     * @return true or false
     */
    public static boolean isContain(String source, String subItem){
        String pattern = "\\b"+subItem+"\\b";
        Pattern p=Pattern.compile(pattern);
        Matcher m=p.matcher(source);
        return m.find();
    }
}
