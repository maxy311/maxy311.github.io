package com.wutian.maxy.xml.operate;

import com.wutian.xml.file.FileUtils;

import java.io.File;

public class CopyDrawables {
    private static final String res = "/Users/maxy/Android/workspace/SHAREit/App/src/main/res";
    private static final String baseCore_res = "/Users/maxy/Android/workspace/SHAREit/BaseCore/src/main/res";
    public static void main(String[] args) {
        CopyDrawables helper = new CopyDrawables();
        helper.starMoveDraw("drawable-xhdpi", "drawable-ldrtl-xhdpi");
    }

    private void starMoveDraw(String resDirName, String toDirName) {
        File baseResDir = new File(baseCore_res, resDirName);
        File baseToResDir = new File(baseResDir, toDirName);
        File resDir = new File(res, resDirName);
        File resToDir = new File(res, toDirName);

        for (File baseResFile : baseResDir.listFiles()) {
            String name = baseResFile.getName();
            File resFile = new File(resDir, name);
            if (resFile.exists()) {
                System.out.println(name);
                resFile.delete();
            }

            File resToFile = new File(resToDir, name);
            if (resToFile.exists()) {
                System.out.println(resToFile.getAbsolutePath());
                File baseToFile = new File(baseToResDir, name);
                try {
                    if (!baseToFile.exists())
                        baseToFile.createNewFile();
                } catch (Exception e) {}

                FileUtils.fileChannelCopy(resToFile, baseToFile);
            }
        }
    }
}
