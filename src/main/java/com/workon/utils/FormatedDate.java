package com.workon.utils;

import com.workon.utils.parser.AnnotationParser;
import com.workon.utils.parser.NoNull;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

abstract public class FormatedDate implements Comparable{

    /**
     * Transforme une string en LocalDate
     *
     * @param date
     *        Date a convertir
     * @return Annee mois jour d'une date
     */
    public static LocalDate stringToLocalDate(@NoNull String date){
        AnnotationParser.parse(date);
        Instant instant = Instant.parse(date);
        //get date time only
        LocalDateTime result = LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId()));
        //get localdate
        return  result.toLocalDate();
    }

    /**
     * Transofrme une LocalDate en String sous le format (14 Juillet 2018)
     *
     * @param localDate
     *        Date a transofrmer
     * @return String
     */
    public static String localDateToString(@NoNull LocalDate localDate){
        AnnotationParser.parse(localDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
        return localDate.format(formatter);
    }

    /**
     * Transforme une string en LocalDateTime
     *
     * @param date
     *        Date a transformer
     * @return LocalDateTime
     */
    public static LocalDateTime stringToLocalDateTime(@NoNull String date){
        AnnotationParser.parse(date);
        Instant instant = Instant.parse(date);
        //get date time only
        return LocalDateTime.ofInstant(instant, ZoneId.of(String.valueOf(ZoneOffset.UTC)));
    }

    /**
     * Transforme une LocalDateTime en String
     *
     * @param localDate
     *        Date a transformer
     * @return String
     */
    public static String localDateTimeToString(@NoNull LocalDateTime localDate){
        AnnotationParser.parse(localDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy HH:mm");
        return localDate.format(formatter);
    }

    /**
     * Tri une map par LocalDate
     *
     * @param map
     *        ID de la conversation
     * @return Map
     */
    public static Map<String, LocalDate> sortStringLocalDate(@NoNull Map<String, String> map){
        AnnotationParser.parse(map);
        Map<String, LocalDate> localDateMap = new HashMap<>();

        Set set = map.entrySet();
        for (Object aSet : set) {
            Map.Entry entry = (Map.Entry) aSet;
            localDateMap.put(entry.getKey().toString(), stringToLocalDate(entry.getValue().toString().substring(1, entry.getValue().toString().length() - 1)));
        }

        Set setss = localDateMap.entrySet();
        for (Object aSet : setss) {
            Map.Entry entry = (Map.Entry) aSet;
        }

        return localDateMap;
    }
}
