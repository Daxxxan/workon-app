package com.workon.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

abstract public class FormatedDate implements Comparable{
    public static String StringFormater(String date){
        LocalDate localDate = LocalDate.parse(date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
        return localDate.format(formatter);
    }

    public static LocalDate stringToLocalDate(String date){
        Instant instant = Instant.parse(date);
        //get date time only
        LocalDateTime result = LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId()));
        //get localdate
        return  result.toLocalDate();
    }

    public static String localDateToString(LocalDate localDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
        return localDate.format(formatter);
    }

    public static Map<String, LocalDate> sortStringLocalDate(Map<String, String> map){
        Map<String, LocalDate> localDateMap = new HashMap<>();
        Map<String, LocalDate> localDateMapSorted;

        Set set = map.entrySet();
        for (Object aSet : set) {
            Map.Entry entry = (Map.Entry) aSet;
            localDateMap.put(entry.getKey().toString(), stringToLocalDate(entry.getValue().toString().substring(1, entry.getValue().toString().length() - 1)));
        }

        Set setss = localDateMap.entrySet();
        for (Object aSet : setss) {
            Map.Entry entry = (Map.Entry) aSet;
            System.out.println("first key: " + entry.getKey() + " value: " + entry.getValue());
        }

        localDateMapSorted = sortByValue(localDateMap);

        return localDateMapSorted;
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> unsortMap) {

        List<Map.Entry<K, V>> list =
                new LinkedList<Map.Entry<K, V>>(unsortMap.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;

    }

}
