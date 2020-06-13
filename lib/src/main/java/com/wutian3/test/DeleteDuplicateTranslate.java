package com.wutian3.test;

import com.Constants;
import com.wutian.xml.file.FileUtils;
import com.wutian3.utils.TranslateFilter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DeleteDuplicateTranslate {
    private static final String SPLIT = "\">";

    public static void main(String[] args) {
        DeleteDuplicateTranslate deleteDuplicateTranslate = new DeleteDuplicateTranslate();
        deleteDuplicateTranslate.startDelete();
    }

    private void startDelete() {
        File baseCore = new File(Constants.BASECORE + "/res/values");
        File appFile = new File(Constants.APP + "/res");
        startDelete(baseCore, appFile);
    }

    private void startDelete(File checkDir, File deleteRes) {
        for (File listFile : checkDir.listFiles(new TranslateFilter())) {
            start(listFile, deleteRes);
        }
    }

    private void start(File checkFile, File deleteDir) {
        List<String> originList = FileUtils.readXmlToList(checkFile);
        Set<String> keys = new HashSet<>();
        for (String line : originList) {
            String trim = line.trim();
            String[] split = trim.split(SPLIT);
            if (split.length >= 2) {
                keys.add(split[0]);
            }
        }

        for (File file : deleteDir.listFiles()) {
            Set<String> copySet = new HashSet<>(keys);
            String name = file.getName();
            if (!name.startsWith("value") || name.contains("hdpi") || name.contains("-v21") || name.contains("-land") || name.contains("-sw600dp")
                    || name.contains("xlarge") || name.contains("-v9") || name.contains("gu"))
                continue;
            List<String> getList = new ArrayList<>();
            for (File valueFile : file.listFiles()) {
                if (copySet.isEmpty())
                    break;
                startDelete(valueFile, copySet, getList);
            }
        }
    }

    protected static void startDelete(File deleteFile, Set<String> copySet, List<String> ids) {
        if (deleteFile == null || !deleteFile.exists())
            return;
        List<String> lines = FileUtils.readXmlToList(deleteFile);
        if (lines == null || lines.isEmpty())
            return;
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(deleteFile)));
            for (String line : lines) {
                String[] strs = line.split(SPLIT);
                if (strs.length >= 2) {
                    if (copySet.contains(strs[0].trim())) {
                        copySet.remove(strs[0].trim());
                        ids.add(line);
                        continue;
                    }
                }
                writer.write(line);
                writer.flush();
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Map<String, String> stringMap = FileUtils.readStringToMap(deleteFile);
        if (stringMap.isEmpty())
            deleteFile.delete();
    }
}

