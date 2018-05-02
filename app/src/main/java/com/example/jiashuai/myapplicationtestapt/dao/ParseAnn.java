package com.example.jiashuai.myapplicationtestapt.dao;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by JiaShuai on 2018/4/25.
 */

public class ParseAnn {

    public static void main(String[] arg) {
        Filter filter1 = new Filter();
        filter1.setName("黑崎一护");
        filter1.setCity("空座町");
        filter1.setId(1);
        Filter filter2 = new Filter();
        filter2.setName("乌尔奇奥拉");
        filter2.setCity("虚");
        filter2.setId(2);
        filter2.setEmail("jiashuai@163.com");

        String sqs = inqueirSql(filter1);
        System.out.println(sqs);
        String sqs2 = inqueirSql(filter2);
        System.out.println(sqs2);


    }

    public static String inqueirSql(Object o) {
        StringBuilder sb = new StringBuilder();
        //获取到class
        Class cl = o.getClass();
        //是否被Table注解
        boolean isAnn = cl.isAnnotationPresent(Table.class);
        if (!isAnn) {
            return null;
        }
        //获取这个注解
        Table t = (Table) cl.getAnnotation(Table.class);
        String tableName = t.value();
        sb.append("select * from ").append(tableName).append(" where 1 = 1 ");
        //获取成员变量
        Field[] fArray = cl.getDeclaredFields();
        for (Field field : fArray) {
            //是否被Column注解
            boolean fExists = field.isAnnotationPresent(Column.class);
            if (!fExists)
                continue;
            //获取这个注解
            Column column = field.getAnnotation(Column.class);
            String columnName = column.value();
            //获取被注解的成员变量名称
            String fieldName = field.getName();
            //根据该名称获取getXXX方法名
            String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Object fieldValue = null;
            try {
                //使用反射机制调用改get方法获取返回值
                Method getMethod = cl.getMethod(methodName);
                fieldValue = getMethod.invoke(o);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (fieldValue != null) {
                sb.append(" and ").append(columnName).append("=").append(fieldValue);
            }

        }
        return sb.toString();
    }

}
