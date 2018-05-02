package com.example.jiashuai.myapplicationtestapt.test;

/**
 * Created by JiaShuai on 2018/4/25.
 */

public class NoBug {
    @Testing
    public void suanShu(){
        System.out.println("1234567890");
    }
    @Testing
    public void jiafa(){
        System.out.println("1+1="+1+1);
    }
    @Testing
    public void jiefa(){
        System.out.println("1-1="+(1-1));
    }
    @Testing
    public void chengfa(){
        System.out.println("3 x 5="+ 3*5);
    }
    @Testing
    public void chufa(){
        System.out.println("6 / 0="+ 6 / 0);
    }

    public void ziwojieshao(){
        System.out.println("我写的程序没有 bug!");
    }
}
