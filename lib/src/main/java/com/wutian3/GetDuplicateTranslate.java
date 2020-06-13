package com.wutian3;

import com.Constants;
import com.wutian.xml.file.FileUtils;
import com.wutian3.utils.ResFileFilter;
import com.wutian3.utils.TranslateFilter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *  String
 */
public class GetDuplicateTranslate {
    private static final String DUPLICATE_TRANSLATE = "duplicate_translate.xml";

    public static void main(String[] args) {
        GetDuplicateTranslate getDuplicateTranslate = new GetDuplicateTranslate();
        getDuplicateTranslate.start();
    }

    private void start() {
        File file = new File(Constants.SHAREit_PATH);
        Map<String, List<String>> map = new HashMap<>();
        getDuplicateTranslate(map, file);

        if (map.isEmpty())
            return;
        printDuplicateToFile(map);
    }

    private void getDuplicateTranslate(Map<String, List<String>> map, File file) {
        if (file.isDirectory()) {
            for (File listFile : file.listFiles(new ResFileFilter())) {
                getDuplicateTranslate(map, listFile);
            }
        } else {
            if (!TranslateFilter.isStringsFile(file))
                return;

            if (!isValuesFile(file))
                return;

            Map<String, String> stringMap = FileUtils.readStringToMap(file);
            if (stringMap.isEmpty())
                return;

            Set<String> keySet = stringMap.keySet();
            for (String key : keySet) {
                String values = stringMap.get(key);

                List<String> stringList = map.get(key);
                if (stringList == null)
                    stringList = new ArrayList<>();

                String path = file.getAbsolutePath().replace(Constants.SHAREit_PATH, "");
                stringList.add(path  + "\n       " + values);

                map.put(key, stringList);
            }
        }
    }

    private boolean isValuesFile(File file) {
        return "values".equals(file.getParentFile().getName());
    }

    private void printDuplicateToFile(Map<String, List<String>> map) {
        File file = new File(Constants.DESKTOP_PATH);
        File duplicateFile = new File(file, DUPLICATE_TRANSLATE);
        try {
            if (!duplicateFile.exists())
                duplicateFile.createNewFile();
        } catch (Exception e) {}

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(duplicateFile))){
            Set<String> keySet = map.keySet();
            for (String key : keySet) {
                List<String> stringList = map.get(key);
                if (stringList.size() > 1) {
                    for (String str : stringList) {
                        bw.write(str + "\n");
                    }
                    bw.write("\n");
                    bw.write("\n");
                    bw.write("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
