package com.wutian.maxy.xml.operate;

public class TranslateHelper {
//    private static String resPath = "/Users/maxy/Android/workspace/App/res";Users/maxy/Android/workspace/SHAREit/App
    private static String SHAREIT_resPath = "/Users/maxy/Android/workspace/SHAREit/App/src/main/res";
    private static String SHAREIT_DAILY_resPath = "/Users/maxy/Android/workspace/SHAREit/App/src/daily/res";

    public static void main(String[] args) {
        addTranslateMethod();
    }

    private static void addTranslateMethod() {
        String transPath = "/Users/maxy/Desktop/res2";
        com.wutian.xml.file.TranslateHelper translateHelper = new com.wutian.xml.file.TranslateHelper();
        translateHelper.addTranslateToValues(SHAREIT_resPath, transPath);
    }
}
