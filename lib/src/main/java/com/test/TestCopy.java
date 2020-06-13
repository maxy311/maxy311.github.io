package com.test;

import com.wutian.xml.file.FileUtils;

import java.io.File;
import java.io.IOException;

public class TestCopy {
    public static String DESKTOP_PATH = "/Users/maxy/Desktop";
    public static void main(String[] args) {
        File file = new File("/Users/maxy/Desktop/mapping.txt");
        File dest = new File("/Users/maxy/Desktop/mapping2.xml");
        try {
            if (!dest.exists())
                dest.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileUtils.fileChannelCopy(file, dest);
    }
}
