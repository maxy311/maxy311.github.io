package com.wutian2.tools;

import java.io.File;

public class ResNameTest {
    private static String DESKTOP_PATH = "/Users/maxy/Desktop";

    public static void main(String[] args) {
        String path = DESKTOP_PATH + File.separator + "res";
        System.out.println(path);

        File translateFile = new File(path);
        if (!translateFile.exists()) {
            System.out.println(translateFile.getAbsolutePath() + "  : file not exit ");
            return;
        }

        for (File listFile : translateFile.listFiles()) {
            File originFile = new File(listFile, "palyer_strings.xml");
            originFile.renameTo(new File(listFile, "player_strings.xml"));
        }
    }
}
