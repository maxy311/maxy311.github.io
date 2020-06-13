package com.example.tools;


import com.wutian.xml.file.FileUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class XlargeUtils {
    private static final String valuePath = "/Users/maxy/Android/workspace/App/res/values";
    private static final String valueXPath = "/Users/maxy/Android/workspace/App/res/values-xlarge";

    public static void outPutXlarge(){
        File valueDir = new File(valuePath);
        if (!valueDir.exists() || !valueDir.isDirectory())
            return;

        File[] files = valueDir.listFiles();

        for (int i = 0; i < files.length; i++) {
            File valueFile = files[i];
            String name = valueFile.getName();
            if (!(name.endsWith("dimens.xml") || name.endsWith("dimen.xml")))
                continue;

            File valueXFile = new File(valueXPath + "/" + name);
            if (!valueXFile.exists()) {
//                System.out.println("no Xlarge File :  " + name);
                addXLargeValues(valueFile, valueXFile);
                continue;
            }

            List<String> valueList = readXmlToList(valueFile);
            List<String> valueXList = readXmlToList(valueXFile);
            for (String value : valueList) {
                if (!valueXList.contains(value)) {
                    System.out.println(name + "    Xlarge not contains :  " + value);
                }
            }
        }
    }

    public static List<String> readXmlToList(File file) {
        ArrayList<String> lines = new ArrayList<>();
        if (file == null || !file.exists())
            return lines;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            while ((line = reader.readLine()) != null) {
                if (getStringKey(line) == null)
                    continue;
                lines.add(line);
            }
        } catch (FileNotFoundException e) {
            return lines;
        } catch (IOException e) {
            return lines;
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lines;
    }

    private static String getStringKey(String line) {
        line = line.trim();
        if (!line.contains("\">"))
            return null;

        if (!line.endsWith("</dimen>"))
            return null;

        String[] split = line.split("\">");
        if (split.length != 2) {
            System.out.println("split error " + line);
            return null;
        }

        return split[0].replace("<dimen name=\"", "");
    }


    private static void addXLargeValues(File valueFile, File xLargeFile) {
        try {
            List<String> stringList = FileUtils.readXmlToList(valueFile);
            if (!xLargeFile.exists())
                xLargeFile.createNewFile();

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(xLargeFile)));

            for (String str : stringList) {
                String[] split = str.split("\">");
                if (split.length != 2) {
                    System.out.println("Error :" + str);
                    writer.write(str);
                    writer.newLine();
                    writer.flush();
                    continue;
                }

                String num = split[1].substring(0, split[1].length() - 10);

                String outString = split[0] + "\">" + ((int) (Float.parseFloat(num) * 1.5)) + split[1].replace(num, "");
                writer.write(outString);
                writer.newLine();
                writer.flush();
            }
        } catch (Exception e) {
            System.out.println("Exception :" + e.toString());
        } finally {
        }

    }
}
