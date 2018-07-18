package com.workon.utils;

import com.workon.utils.parser.AnnotationParser;
import com.workon.utils.parser.NoNull;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.workon.utils.FormatedDate.sortByValue;

abstract public class FormatedDate implements Comparable{
    public static String StringFormater(@NoNull String date){
        AnnotationParser.parse(date);
        LocalDate localDate = LocalDate.parse(date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
        return localDate.format(formatter);
    }

    public static LocalDate stringToLocalDate(@NoNull String date){
        AnnotationParser.parse(date);
        Instant instant = Instant.parse(date);
        //get date time only
        LocalDateTime result = LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId()));
        //get localdate
        return  result.toLocalDate();
    }

    public static String localDateToString(@NoNull LocalDate localDate){
        AnnotationParser.parse(localDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
        return localDate.format(formatter);
    }

    public static LocalDateTime stringToLocalDateTime(@NoNull String date){
        AnnotationParser.parse(date);
        Instant instant = Instant.parse(date);
        //get date time only
        return LocalDateTime.ofInstant(instant, ZoneId.of(String.valueOf(ZoneOffset.UTC)));
    }

    public static String localDateTimeToString(@NoNull LocalDateTime localDate){
        AnnotationParser.parse(localDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy HH:mm");
        return localDate.format(formatter);
    }

    public static Map<String, LocalDate> sortStringLocalDate(@NoNull Map<String, String> map){
        AnnotationParser.parse(map);
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
        }

        localDateMapSorted = sortByValue(localDateMap);

        return localDateMapSorted;
    }

    public static Map<String, LocalDate> sortByValue(@NoNull Map<String, LocalDate> unsortMap) {
        AnnotationParser.parse(unsortMap);
        List<Map.Entry<String, LocalDate>> list = new LinkedList<>(unsortMap.entrySet());

        list.sort(Comparator.comparing(o -> (o.getValue())));

        Map<String, LocalDate> result = new LinkedHashMap<>();
        for (Map.Entry<String, LocalDate> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;

    }

}
