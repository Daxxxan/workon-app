package com.workon.utils.parser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;

public class AnnotationParser {
    public static void parse(Object... args) {
        ArrayList<Object> params = getArgsAsArray(args);
        int paramsCounter = 0;

        StackTraceElement elem = new Exception().getStackTrace()[1];
        String qualifiedMethodName = elem.toString().substring(0, elem.toString().indexOf('('));
        String qualifiedClassName = qualifiedMethodName.substring(0, qualifiedMethodName.lastIndexOf('.'));

        try {
            Method[] methods = Class.forName(qualifiedClassName).getDeclaredMethods();
            for(Method method: methods) {
                String temp = method.toString().substring(0, method.toString().lastIndexOf('('));
                if(temp.substring(temp.lastIndexOf(' ') + 1).equals(qualifiedMethodName)) {
                    for( Parameter parameter: method.getParameters() ) {
                        for( Annotation annotation: parameter.getAnnotations() ) {
                            dispatch(parameter.getType(), params.get(paramsCounter), parameter.getAnnotation(annotation.annotationType()));
                        }
                        paramsCounter++;
                    }
                    break;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<Object> getArgsAsArray(Object... args) {
        ArrayList<Object> result = new ArrayList<>();
        result.addAll(Arrays.asList(args));
        return result;
    }

    private static void dispatch(Class<?> paramType, Object param, Annotation annotation) {
        //System.out.println(paramType + "; " + param + "; " + annotation);

        String qualifiedAnnotationName = annotation.toString().substring(0, annotation.toString().lastIndexOf('('));
        switch(qualifiedAnnotationName) {
            case "@com.workon.utils.parser.NoNull":
                execNoNull(paramType, param, annotation);
                break;
            case "@com.workon.utils.parser.Min":
                execMin(paramType, param, annotation);
                break;
            case "@com.workon.utils.parser.Max":
                execMax(paramType, param, annotation);
                break;
            case "@com.workon.utils.parser.Between":
                execBetween(paramType, param, annotation);
                break;
        }
    }

    private static void execNoNull(Class<?> paramType, Object param, Annotation annotation) {
        if(param == null) {
            Exception e = new Exception("Null value is not allowed.");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private static void execMin(Class paramType, Object param, Annotation annotation) {
        Min minAnnotation = (Min)annotation;
        double paramValue = getParamValueAsDouble(paramType, param);

        if(paramValue < minAnnotation.value()) {
            Exception e = new Exception("Value is under the allowed limit.");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private static void execMax(Class paramType, Object param, Annotation annotation) {
        Max maxAnnotation = (Max)annotation;
        double paramValue = getParamValueAsDouble(paramType, param);

        if(paramValue > maxAnnotation.value()) {
            Exception e = new Exception("Value is over the allowed limit.");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private static void execBetween(Class paramType, Object param, Annotation annotation) {
        Between betweenAnnotation = (Between)annotation;
        double paramValue = getParamValueAsDouble(paramType, param);

        if(paramValue < betweenAnnotation.min()) {
            Exception e = new Exception("Value is under the allowed range.");
            e.printStackTrace();
            System.exit(-1);
        }
        if(paramValue > betweenAnnotation.max()) {
            Exception e = new Exception("Value is over the allowed range.");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private static double getParamValueAsDouble(Class paramType, Object param) {
        if(param instanceof Integer) {
            Integer o = (Integer)param;
            return o.doubleValue();
        }else if(param instanceof Double) {
            return (Double)param;
        }else if(param instanceof Float) {
            Float o = (Float)param;
            return o.doubleValue();
        }else{
            IllegalArgumentException e = new IllegalArgumentException(paramType + " is not allowed");
            e.printStackTrace();
            System.exit(-1);
        }
        return 0;
    }
}
