package com.test;

import java.io.File;

public class DeleteLayoutFile {
    private static final String APP_PATH_RES = "/Users/maxy/Android/workspace/SHAREit/App/src/main/res";
    private static final String CORE_PATH_RES = "/Users/maxy/Android/workspace/SHAREit/BaseCore/src/main/res";

    public static void main(String[] args) {
        DeleteLayoutFile deleteLayoutFile = new DeleteLayoutFile();
//        deleteLayoutFile.delete("anim");
//        deleteLayoutFile.delete("drawable");
//        deleteLayoutFile.delete("drawable-xhdpi");
//        deleteLayoutFile.delete("layout");
//        deleteLayoutFile.delete("raw");
        deleteLayoutFile.deleteResFile("premium_strings.xml");
    }

    private void deleteResFile(String dirName) {
        File appLayout = new File(APP_PATH_RES);
        for (File file : appLayout.listFiles()) {
            if (!file.isDirectory())
                continue;
            File appLayoutFile = new File(file, dirName);
            if (appLayoutFile.exists()) {
                System.out.println(dirName);
                appLayoutFile.delete();
            }
        }
    }

    private void delete(String dirName) {
        File coreLayout = new File(CORE_PATH_RES, dirName);
        File appLayout = new File(APP_PATH_RES, dirName);
        if (!coreLayout.exists() || !appLayout.exists())
            throw new RuntimeException(dirName + "  Not exits.");

        for (File file : coreLayout.listFiles()) {
            String name = file.getName();
            File appLayoutFile = new File(appLayout, name);
            if (appLayoutFile.exists()) {
                System.out.println(name);
                appLayoutFile.delete();
            }
        }
    }
}
