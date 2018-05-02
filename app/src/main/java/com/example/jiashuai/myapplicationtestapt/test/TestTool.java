package com.example.jiashuai.myapplicationtestapt.test;

import java.lang.reflect.Method;

/**
 * Created by JiaShuai on 2018/4/25.
 */

public class TestTool {
    public static void main(String[] arg) {

        NoBug noBug = new NoBug();

        checkBug(noBug);
    }

    public static void checkBug(Object o) {
        Class aClass = o.getClass();
        /**
         * getDeclaredMethod*()获取的是类自身声明的所有方法，包含public、protected和private方法。
         * getMethod*()获取的是类的所有共有方法，这就包括自身的所有public方法，和从基类继承的、从接口实现的所有public方法。
         */
        StringBuilder sb = new StringBuilder();
        int errNum = 0;
        Method[] methods = aClass.getDeclaredMethods();
        for (Method method : methods) {
            boolean isAnn = method.isAnnotationPresent(Testing.class);
            if (!isAnn) {
                continue;
            }
            try {
                method.invoke(o);
            } catch (Exception e) {
                errNum++;
                sb.append(method.getName())
                        .append(" ")
                        .append("has error：")
                        .append("\n\r caused by   ")
                        .append(e.getCause().getClass().getSimpleName())
                        .append("\n\r")
                        .append(e.getCause().getMessage())
                        .append("\n\r");
            }
        }
        sb.append(aClass.getSimpleName())
                .append("  has  ")
                .append(errNum)
                .append("  error   ");

        System.out.println(sb.toString());
    }

}
