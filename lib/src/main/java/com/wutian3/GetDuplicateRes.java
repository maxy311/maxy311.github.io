package com.wutian3;

import com.Constants;
import com.wutian3.utils.FileHashUtils;
import com.wutian3.utils.ResFileFilter;

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
 * java
 * res: layout /color /drawable /drawable-hdpi /drawable-xhdpi /drawable-ldrtl-xhdpi /raw /anim
 */
public class GetDuplicateRes {
    private static final String DUPLICATE_FILE = "duplicate_file.xml";
    public static void main(String[] args) {
        GetDuplicateRes deleteDuplicateRes = new GetDuplicateRes();
        deleteDuplicateRes.start();
    }

    private static List<String> sFilterPath = new ArrayList<>();

    static {
        sFilterPath.add("anim");
        sFilterPath.add("color");
        sFilterPath.add("drawable");
        sFilterPath.add("drawable-hdpi");
        sFilterPath.add("drawable-mdpi");
        sFilterPath.add("drawable-xhdpi");
        sFilterPath.add("drawable-xlarge");
        sFilterPath.add("drawable-xxhdpi");
        sFilterPath.add("drawable-ldrtl-hdpi");
        sFilterPath.add("drawable-ldrtl-xhdpi");
        sFilterPath.add("layout");
        sFilterPath.add("raw");
    }

    private void start() {
        Map<String, List<String>> map = new HashMap<>();
        File shareitFile = new File(Constants.SHAREit_PATH);
        findDuplicateFile(map, shareitFile);

        printDuplicateFile(map);
    }

    private void findDuplicateFile(Map<String, List<String>> map, File shareitFile) {
        if (shareitFile.isDirectory()) {
            for (File file : shareitFile.listFiles(new ResFileFilter())) {
                findDuplicateFile(map, file);
            }
        } else {
            if (!isFilterFile(shareitFile))
                return;
            String fileHashCode = FileHashUtils.getHashCode(shareitFile);
            List<String> list = map.get(fileHashCode);
            if (list == null)
                list = new ArrayList<>();
            list.add(shareitFile.getAbsolutePath());
            map.put(fileHashCode, list);
        }
    }

    private boolean isFilterFile(File file) {
        if (file.getName().endsWith(".java"))
            return true;
        return sFilterPath.contains(file.getParentFile().getName());
    }

    private void printDuplicateFile(Map<String, List<String>> map) {
        File file = new File(Constants.DESKTOP_PATH);
        File duplicateRes = new File(file, DUPLICATE_FILE);
        try {
            if (!duplicateRes.exists())
                duplicateRes.createNewFile();
        } catch (IOException e) {
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(duplicateRes))) {
            Set<String> keySet = map.keySet();
            Map<String, List<String>> javaMap = new HashMap<>();
            for (String key : keySet) {
                List<String> stringList = map.get(key);
                if (stringList.size() > 1) {
                    boolean isJavaFile = false;
                    for (String str : stringList) {
                        if (str.endsWith(".java")) {
                            javaMap.put(str, stringList);
                            isJavaFile = true;
                            break;
                        } else
                            bw.write(str + "\n");
                    }
                    if (!isJavaFile)
                        bw.write("\n\n");
                }
            }
            bw.write("\n\n");
            bw.write("\n\n");
            bw.write("-----------------------------------Java Duplicate File-------------------------------------");
            bw.write("\n\n\n");
            if (!javaMap.isEmpty()) {
                for (String key : javaMap.keySet()) {
                    List<String> javaList = javaMap.get(key);
                    for (String javaPath : javaList) {
                        bw.write(javaPath + "\n");
                    }
                    bw.write("\n\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
