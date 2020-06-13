package com.wutian.maxy.xml.operate;

import com.wutian.xml.file.FileUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.Map;

public class DeleteEmptyFile {
    private static final String res = "/Users/maxy/Android/workspace/SHAREit/App/src/main/res";
    public static void main(String[] args) {
        DeleteEmptyFile deleteEmptyFile = new DeleteEmptyFile();
        deleteEmptyFile.start();
    }

    private void start() {
        File resFile = new File(res);
        File[] files = resFile.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                String name = file.getName();
                if (!name.startsWith("values"))
                    return false;
                if (name.contains("hdpi"))
                    return false;

                if (name.contains("land"))
                    return false;

                if (name.contains("sw600dp"))
                    return false;
                if (name.contains("v21") || name.contains("v9"))
                    return false;
                if (name.contains("xlarge"))
                    return false;
                return true;
            }
        });

        System.out.println(files.length);
        for (File file : files) {
            deleteValueFile(file);
        }
    }

    private void deleteValueFile(File valueDir) {
        File[] listFiles = valueDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                String name = file.getName();
                return name.contains("string");
            }
        });

        for (File listFile : listFiles) {
            Map<String, String> stringMap = FileUtils.readStringToMap(listFile);
            if (stringMap.isEmpty()) {
                listFile.delete();
                System.out.println(listFile.getAbsolutePath());
                System.out.println();
                System.out.println();
            }

        }
    }
}
