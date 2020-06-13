package com.wutian3.test;

import com.Constants;

import java.io.File;

public class DeleteDuplicateRes {
    private static final String RES_PATH = "/res";

    public static void main(String[] args) {
        File resFile = new File(Constants.APP + RES_PATH);
        DeleteDuplicateRes deleteDuplicateFile = new DeleteDuplicateRes();

        // delete app duplicate
        deleteDuplicateFile.startCheck(resFile, Constants.BASECORE + RES_PATH);
        deleteDuplicateFile.startCheck(resFile, Constants.BIZONLINE_ONLINE + RES_PATH);


        // delete online duplicate
        resFile = new File(Constants.BIZONLINE_ONLINE + RES_PATH);
        deleteDuplicateFile.startCheck(resFile, Constants.BASECORE + RES_PATH);
    }

    private void startCheck(File resFile, String path) {
        File deleteFile = new File(path);
        startDelete(resFile, deleteFile, "layout");
        startDelete(resFile, deleteFile, "color");
        startDelete(resFile, deleteFile, "drawable");
        startDelete(resFile, deleteFile, "drawable-hdpi");
        startDelete(resFile, deleteFile, "drawable-xhdpi");
        startDelete(resFile, deleteFile, "drawable-ldrtl-xhdpi");
        startDelete(resFile, deleteFile, "raw");
        startDelete(resFile, deleteFile, "anim");
    }

    private void startDelete(File resFile, File deleteDir, String resDirName) {
        File resDir = new File(resFile, resDirName);
        if (!resDir.exists())
            return;
        File deleteResDir = new File(deleteDir, resDirName);
        if (!deleteResDir.exists())
            return;
        for (File file : deleteResDir.listFiles()) {
            String name = file.getName();
            File valueFile = new File(resDir, name);
            if (valueFile.exists())
                valueFile.delete();
        }
    }

}
