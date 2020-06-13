package com.wutian.maxy.xml.operate;

import com.Constants;
import com.wutian3.utils.ResFileFilter;

import java.io.File;

public class DeleteJava2 {
    public static void main(String[] args) {
        DeleteJava2 deleteJava2 = new DeleteJava2();
        deleteJava2.start();
    }

    private void start() {
        String path = Constants.BIZONLINE_ONLINE + "/res";
        deleteFile(new File(path), "video_strings.xml");
    }

    private void deleteFile(File resFile, String fileNmae) {
        if (resFile.isDirectory()) {
            for (File listFile : resFile.listFiles(new ResFileFilter())) {
                deleteFile(listFile, fileNmae);
            }
        } else {
            String name = resFile.getName();
            if (name.equals(fileNmae)) {
                System.out.println(resFile.getAbsolutePath());
                resFile.delete();
            }
        }
    }
}
