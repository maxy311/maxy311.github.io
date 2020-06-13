package com.wutian.maxy.xml.operate;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DeleteJava {
    private static final String online_path = "/Users/maxy/Android/workspace/SHAREit/BizOnline/Online/src/main/java";
    private static final String app_path = "/Users/maxy/Android/workspace/SHAREit/App/src/main/java";
    private static final String basecore_path = "/Users/maxy/Android/workspace/SHAREit/BaseCore/src/main/java";
    private static final String bizlocal_common = "/Users/maxy/Android/workspace/SHAREit/BizLocal/LocalCommon/src/main/java";
    private static final String bizlocal_local = "/Users/maxy/Android/workspace/SHAREit/BizLocal/ModuleLocal/src/main/java";
    private static final String bizlocal_transfater = "/Users/maxy/Android/workspace/SHAREit/BizLocal/ModuleTransfer/src/main/java";


    private static final String bizgame = "/Users/maxy/Android/workspace/SHAREit/BizGame/src/main/java";

    private static final String bizbasic_download = "/Users/maxy/Android/workspace/SHAREit/BizBasic/ModuleDownload/src/main/java";
    private static final String bizbasic_feedback = "/Users/maxy/Android/workspace/SHAREit/BizBasic/ModuleFeedback/src/main/java";
    private static final String bizbasic_invite = "/Users/maxy/Android/workspace/SHAREit/BizBasic/ModuleInvite/src/main/java";
    private static final String bizbasic_login = "/Users/maxy/Android/workspace/SHAREit/BizBasic/ModuleLogin/src/main/java";
    private static final String bizbasic_notify = "/Users/maxy/Android/workspace/SHAREit/BizBasic/ModuleNotify/src/main/java";
    private static final String bizbasic_settings = "/Users/maxy/Android/workspace/SHAREit/BizBasic/ModuleSetting/src/main/java";
    private static final String bizbasic_udpate = "/Users/maxy/Android/workspace/SHAREit/BizBasic/ModuleUpgrade/src/main/java";


    public static void main(String[] args) {
        DeleteJava deleteJava = new DeleteJava();
//        deleteJava.startDelete(online_path);
//        deleteJava.startDelete(basecore_path);
//        deleteJava.startDelete(bizlocal_common);
//        deleteJava.startDelete(bizlocal_local);
//        deleteJava.startDelete(bizlocal_transfater);

//        deleteJava.startDelete(bizgame);

//        deleteJava.startDelete(bizbasic_download);
//        deleteJava.startDelete(bizbasic_feedback);
//        deleteJava.startDelete(bizbasic_invite);
//        deleteJava.startDelete(bizbasic_login);
//        deleteJava.startDelete(bizbasic_notify);
//        deleteJava.startDelete(bizbasic_settings);
        deleteJava.startDelete(bizbasic_udpate);

    }

    private static Map<File, List<File>> sMap = new HashMap<>();
    private void startDelete(String online_path) {
        File appFile = new File(app_path);
        File onlineFile = new File(online_path);


        doDelete(appFile, onlineFile);

        if (sMap.size() > 0) {
            System.out.println();
            System.out.println("------------------------------");
            System.out.println("------------------------------");
            System.out.println("------------------------------");
            Set<File> files = sMap.keySet();
            for (File file : files) {
                List<File> fileList = sMap.get(file);
                System.out.println(file.getAbsolutePath());
                for (File file1 : fileList) {
                    System.out.println(file1.getAbsolutePath());
                }
                System.out.println();
                System.out.println();
            }
        }

    }

    private void doDelete(File appFile, File onlineFile) {
        if (onlineFile.isDirectory()) {
            for (File file : onlineFile.listFiles()) {
                doDelete(appFile, file);
            }
        } else {
            String name = onlineFile.getName();
            List<File> files = new ArrayList<>();
            getAppSameFile(files, appFile, name);

            if (files.size() > 1) {
                sMap.put(onlineFile, files);
            } else {

                for (File file : files) {
                    if (file.exists()) {
                        file.delete();
                        System.out.println(onlineFile.getAbsolutePath());
                        System.out.println(file.getAbsolutePath());

                        System.out.println();
                        System.out.println();
                        System.out.println();
                    }
                }
            }
        }
    }

    private void getAppSameFile(List<File> files, File appFile, String fileName) {
        if (appFile.isDirectory()) {
            for (File listFile : appFile.listFiles()) {
                getAppSameFile(files, listFile, fileName);
            }
        } else {
            String name = appFile.getName();
            if (fileName.equals(name))
                files.add(appFile);
        }
    }
}
