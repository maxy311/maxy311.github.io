package com.wutian2.tools;

import com.wutian.xml.file.FileUtils;

import java.io.File;

public class SplitUtils {
    public static void main(String[] args) {
        SplitUtils splitUtils = new SplitUtils();
        splitUtils.startSplit();
    }

    private static String DESKTOP_PATH = "/Users/maxy/Desktop";
    public void startSplit() {
        String path = DESKTOP_PATH + File.separator + "translate";

        System.out.println(path);

        File translateFile = new File(path);
        if (!translateFile.exists()) {
            System.out.println(translateFile.getAbsolutePath() + "  : file not exit ");
            return;
        }

        splitFiles(translateFile);
    }

    private void splitFiles(File originFile) {
        String targetResPath = DESKTOP_PATH + File.separator + "res";
        File targetResDir = checkToCreateDir(targetResPath);
        String releaseNotePath = DESKTOP_PATH + File.separator + "ReleaseNote";
        File releaseNoteDir = checkToCreateDir(releaseNotePath);
        for (File valuesDir : originFile.listFiles()) {
            if (valuesDir.isHidden())
                continue;
            String targetValueDirPath = targetResDir.getAbsolutePath() + File.separator + valuesDir.getName();
            File targetValueDir = checkToCreateDir(targetValueDirPath);
            copyToResFile(releaseNoteDir, targetValueDir, valuesDir);
        }
    }

    private void copyToResFile(File releaseNoteDir, File targetValueDir, File originValuesDir) {
        for (File originValuesSubFile : originValuesDir.listFiles()) {
            if (originValuesSubFile.isDirectory()) {
                copyToResFile(releaseNoteDir, targetValueDir, originValuesSubFile);
            } else if (isTransLateFile(originValuesSubFile)) {
                startToCopyFile(targetValueDir, originValuesSubFile);
            } else {
                //copy release note dir;
               File releaseNoteValueDir =  new File(releaseNoteDir, targetValueDir.getName());
               if (!releaseNoteValueDir.exists())
                   releaseNoteValueDir.mkdir();
               System.out.println(releaseNoteValueDir.getAbsolutePath() + "\n" + originValuesSubFile);
               startToCopyFile(releaseNoteValueDir, originValuesSubFile);
            }
        }
    }

    private static void startToCopyFile(File targetDir, File originFile) {
        File targetFile = new File(targetDir, originFile.getName());
        FileUtils.copyFile(originFile, targetFile);
    }

    private static File checkToCreateDir(String resPath) {
        File file = new File(resPath);
        if (!file.exists()) {
            try {
                file.mkdir();
            } catch (Exception e) {
                throw new RuntimeException("create new file error!!!!!   " + file.getAbsolutePath());
            }
        }

        return file;
    }

    private boolean isTransLateFile(File file) {
        if (file.isHidden())
            return false;

        if (file.isDirectory())
            return false;
        String fileName = file.getName();

        if (!fileName.endsWith(".xml")) {
            return false;
        }

        if (fileName.contains("push"))
            return false;
        if (fileName.contains("releasenote"))
            return false;
        if (fileName.contains("Release"))
            return false;
        return true;
    }
}
