package com.wutian2.operate;

import com.wutian.xml.file.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.InputStreamReader;

public class TranslateHelpers {
    private static String SHAREIT_resPath = "/Users/maxy/Android/workspace/SHAREit/App/src/main/res";
    /////////444444444444444////
//    private static String SHAREIT_resPath = "/Users/maxy/Android/workspace/SHAREit/BizGame/ModuleGame/src/main/res";
    private static String SHELL_PATH = "/Users/maxy/Android/workspace/XmlUtilsAs/git-for-all.sh";
    private static String SHAREIT_resPath_VALUE_OLD ="/Users/maxy/Android/workspace/SHAREit/App/src/main/res/values_old";
    private static String DESKTOP_PATH = "/Users/maxy/Desktop";

    public static void addTranslate(String translatePath) {
        File translateResFile = new File(translatePath);
        File resDir = new File(SHAREIT_resPath);
        AddTransLateHelper addTransLateHelper = new AddTransLateHelper();
        addTransLateHelper.addTranslate(translateResFile, resDir);
    }

    public static void getTranslate(String preTag) {
        // 1 , checkout last version
        runShellCmd(preTag);
        System.out.println("has checkout to pre version \n\n\n\n\n\n");

        //2, copy last version values file
        copyOldValues();

        //3, checkout master
        runShellCmd("master");
        System.out.println("has checkout to master \n\n\n\n\n\n");


        GetTransLateHelper.GetTransLateListener getTransLateListener = new GetTransLateHelper.GetTransLateListener() {
            @Override
            public void notifyGetTransLateEnd() {
                //5 delete values old;
                deleteOldValues();
            }
        };

        //4, getTransLate
        toGetTranslate(getTransLateListener);
    }

    private static void toGetTranslate(GetTransLateHelper.GetTransLateListener getTransLateListener) {
        File oldValueFile = new File(SHAREIT_resPath_VALUE_OLD);
        File valueFile = new File(SHAREIT_resPath, "values");
        File valueArFile = new File(SHAREIT_resPath, "values-bn");

        String saveDirPath = DESKTOP_PATH + "/res";
        GetTransLateHelper transLateHelper = new GetTransLateHelper();
        transLateHelper.setGetTransLateListener(getTransLateListener);
        transLateHelper.getTranslate(valueFile, valueArFile, oldValueFile, saveDirPath);
    }

    private static void deleteOldValues() {
        System.out.println("\n\n\n\n\n\n --------------- deleteOldValues");
        File oldValueDir = new File(SHAREIT_resPath_VALUE_OLD);
        if (!oldValueDir.exists())
            return;

        for (File file : oldValueDir.listFiles()) {
            file.delete();
        }
        oldValueDir.delete();
    }

    private static void copyOldValues() {
        File valueDir = new File(SHAREIT_resPath, "values");
        if (valueDir == null || !valueDir.exists())
            throw new RuntimeException("can not create old value file");

        File[] files = valueDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
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
        });

        File oldValueDir = new File(SHAREIT_resPath_VALUE_OLD);
        if (!oldValueDir.exists())
            oldValueDir.mkdir();
        for (File file : files) {
            File oldFile = new File(oldValueDir, file.getName());
            FileUtils.copyFile(file, oldFile);
        }

        System.out.println("copy old file success");
    }

    private static void runShellCmd(String tag) {
        checkoutToTag(tag);
    }

    private static void checkoutToTag(String tag) {
        try {
            String command = SHELL_PATH + " checkout " + tag;
            Process ps = Runtime.getRuntime().exec(command);
            ps.waitFor();

            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            String result = sb.toString();
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
