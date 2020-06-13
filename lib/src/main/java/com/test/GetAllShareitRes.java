package com.test;

import com.Constants;
import com.wutian.xml.file.FileUtils;
import com.wutian3.utils.TranslateFilter;

import java.io.File;
import java.io.IOException;

public class GetAllShareitRes {
    private static String SHAREIT_PATh = Constants.SHAREit_PATH;
    private static String DESKTOP = Constants.DESKTOP_PATH + "/SHAREir_Res";
    public static void main(String[] args) {
//        File deskDir = new File(DESKTOP, "SHAREir_Res");
//        if (deskDir.exists())
//            deskDir.mkdir();

        tryGetTranslateFile(new File(SHAREIT_PATh));
    }

    private static void tryGetTranslateFile(File file) {
        if (file.isDirectory()) {
            if (file.getName().equals("debug"))
                return;
            if (file.getName().equals("BizPay"))
                return;
            if (file.getName().equals("SDK"))
                return;

            for (File listFile : file.listFiles()) {
                tryGetTranslateFile(listFile);
            }
        } else {
            if (TranslateFilter.isStringsFile(file)) {
                copyFieToDesk(file);
            }
        }
    }

    private static void copyFieToDesk(File file) {
        String absolutePath = file.getAbsolutePath();
        try {
            String path = absolutePath.substring(absolutePath.indexOf("SHAREit") + 8, absolutePath.indexOf("src") -1);

            File deskFile = getDeskFile(file, path);
            FileUtils.fileChannelCopy(file, deskFile);
            System.out.println(path + "       "+ "       " + deskFile.getAbsolutePath());
        } catch (Exception e) {
        }
    }


    private static File getDeskFile(File file, String path) {
        String[] split = path.split(File.separator);
        File deskFile = new File(DESKTOP);
        if (!deskFile.exists())
            deskFile.mkdir();
        for (String s : split) {
            deskFile = getOrCreateFileDir(deskFile, s);
        }

        String parentName = file.getParentFile().getName();
        deskFile = getOrCreateFileDir(deskFile, parentName);
        return getOrCreateFile(deskFile, file.getName());
    }

    private static File getOrCreateFileDir(File parent, String dir) {
        File file = new File(parent, dir);
        if (!file.exists())
            file.mkdir();
        return file;
    }

    private static File getOrCreateFile(File parent, String fileName) {
        File file = new File(parent, fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
