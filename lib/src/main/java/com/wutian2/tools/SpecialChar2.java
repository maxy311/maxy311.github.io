package com.wutian2.tools;

import com.Constants;
import com.wutian.utils.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SpecialChar2 {
    public static String SHAREIT_resPath = "/Users/maxy/Android/workspace/SHAREit/App/src/main/res";

    public static void main(String[] args) {
        File file = new File(Constants.DESKTOP_PATH);

        File xmlFile = new File(file, "aaaaa.xml");
        if (!xmlFile.exists()) {
            System.out.println(xmlFile.getAbsolutePath() + "  not exit");
        }
        List<String> list = readFileToList(xmlFile);
        String str = list.get(0);
        System.out.println(str);
        System.out.println(str.indexOf("&"));
        System.out.println(str.indexOf("&amp;"));
    }

    private static List<String> readFileToList(File file) {
        List<String> list = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line = null;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        } catch (Exception e) {

        } finally {
            Utils.close(br);
        }
        return list;
    }
}
