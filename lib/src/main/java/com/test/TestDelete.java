package com.test;

import java.io.File;

public class TestDelete {
    private static final String VALUE_DIR = "/Users/maxy/Desktop/SHAREit/BizGame/ModuleGame";
    public static void main(String[] args) {
        File vauleFile = new File(VALUE_DIR);
        for (File file : vauleFile.listFiles()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File file1 : files) {
                    if ("game_strings.xml".equals(file1.getName())) {
                        file1.delete();
                        break;
                    }
                }
            }
        }
//        File[] files1 = vauleFile.listFiles();
//        for (File file : files1) {
//            boolean stringFiles = isStringFiles(file);
//            if (!stringFiles) {
//                file.delete();
//            }
//        }
//        File[] files = valueDir.listFiles(new FileFilter() {
//            @Override
//            public boolean accept(File pathname) {
//                String name = pathname.getName();
//                if (!name.contains("string"))
//                    return false;
//                if (name.contains("filter"))
//                    return false;
//                if (name.contains("dimens"))
//                    return false;
//                if (name.contains("account"))
//                    return false;
//                if (name.contains("product_setting"))
//                    return false;
//                if (name.contains("country_code_string"))
//                    return false;
//                return true;
//            }
//        });
    }

    private static boolean isStringFiles(File pathname) {
        String name = pathname.getName();
        if (!name.contains("string"))
            return false;
        if (name.contains("filter"))
            return false;
        if (name.contains("dimens"))
            return false;
        if (name.contains("account"))
            return false;
        if (name.contains("product_setting"))
            return false;
        if (name.contains("country_code_string"))
            return false;
        return true;
    }
}
