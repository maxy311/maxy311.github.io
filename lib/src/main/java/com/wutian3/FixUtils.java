package com.wutian3;

import com.Constants;
import com.wutian.xml.file.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FixUtils {
    private static String TRANSLATE_DIR = "SHAREit_Translate";
    private static Map<String, List<String>> map = new HashMap<>();

    public static void main(String[] args) {
        File translateDir = new File(Constants.DESKTOP_PATH + "/" + TRANSLATE_DIR);

        readValuesToMap(translateDir);

        resortString(translateDir);
    }

    private static void resortString(File translateDir) {
        for (File listFile : translateDir.listFiles()) {
            if (listFile.getName().equals("values"))
                continue;
            if (listFile.isHidden())
                continue;
            for (File file : listFile.listFiles()) {
                if (file.isHidden())
                    continue;
                System.out.println(file.getAbsolutePath());
                reWriteFile(file);
            }

        }
    }

    private static void reWriteFile(File file) {
        Map<String, String> stringMap = FileUtils.readStringToMap(file);
        Set<String> keySet = stringMap.keySet();
        List<String> stringList = map.get(file.getName());
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))){
            for (String str : stringList) {
                String[] split = str.trim().split("\">");
                if (keySet.contains(split[0])) {
                   str = "    " + stringMap.get(split[0]);
                   System.out.println(str);
                }

                bw.write(str);
                bw.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readValuesToMap(File translateDir) {
        File valuesDir = new File(translateDir, "values");
        if (!valuesDir.exists())
            System.out.println("Error " + valuesDir.getAbsolutePath());

        for (File file : valuesDir.listFiles()) {
            if (file.isHidden())
                continue;
            map.put(file.getName(), FileUtils.readXmlToList(file));
        }
    }
}
