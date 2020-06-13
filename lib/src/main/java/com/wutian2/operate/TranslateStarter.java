package com.wutian2.operate;

public class TranslateStarter {
    public static void main(String[] args) {
        addTranslate();
//        getTranslate();
    }

    private static void addTranslate() {
//        SplitUtils splitUtils = new SplitUtils();
//        splitUtils.startSplit();
//        System.out.println("Split Release Note End!!!!!!");

        TranslateHelpers.addTranslate("/Users/maxy/Desktop/res");
    }

    private static void getTranslate() {
        TranslateHelpers.getTranslate("v4.8.8_ww");
    }
}
