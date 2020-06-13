package com.test;

import com.Constants;
import com.wutian.xml.file.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TestJava2 {
    public static void main(String[] args) {
        TestJava2 testJava = new TestJava2();
        testJava.start();
    }

    private void start() {
        String path = "/Users/maxy/Desktop/res111";
        File file = new File(path);
        slipFile(file, "setting_strings.xml");


    }

    private void slipFile(File file, String splitFile) {
        File resFile = getDestResFile();
        for (File listFile : file.listFiles()) {
            File file1 = new File(listFile, splitFile);
            System.out.println(file1.getAbsolutePath());
            if (file1.exists()) {
                copyToResFile(resFile, listFile.getName(),  splitFile, file1);
            }
        }
    }

    private void copyToResFile(File resFile, String name, String splitFile, File originFile) {
        File dir = new File(resFile, name);
        if (!dir.exists())
            dir.mkdir();
        File file = new File(dir, "strings.xml");
        System.out.println(file.getAbsolutePath());
        try {
            if (!file.exists())
                file.createNewFile();
        } catch (IOException e) {
        }

        List<String> stringList = FileUtils.readXmlToList(originFile);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))){
            for (String str : stringList) {
                if (str.contains("setting_screen_lock"))
                    continue;
                if (str.contains("setting_screen_lock_desc"))
                    continue;
                if (str.contains("setting_sz_message_notification_new"))
                    continue;
                if (str.contains("setting_notification_cmd_recommend"))
                    continue;

                bw.write(str);
                bw.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private File getDestResFile() {
        File deskFile = new File(Constants.DESKTOP_PATH);
        File resFile = new File(deskFile, "res");
        if (!resFile.exists())
            resFile.mkdir();
        return resFile;
    }

}
