package com.wutian.maxy.xml.operate;

import com.wutian.xml.file.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DeleteStringUtils {
    private static final String SPLIT = "\">";

    private static final Map<String, List<String>> DELETE_IDS = new HashMap<>();
    private static final String res = "/Users/maxy/Android/workspace/SHAREit/BizGame/src/main/res";

    static {
        List<String> ids = new ArrayList<>();

        ids.add("<string name=\"content_file_not_exist");
        ids.add("<string name=\"content_app_run_failed");
        ids.add("<string name=\"content_contact_import_success");
        ids.add("<string name=\"content_contact_import_failed");

        DELETE_IDS.put("content_strings.xml", ids);

    }

    public static void main(String[] args){
        startDelete();
    }

    public static void startDelete() {
        if (DELETE_IDS != null && DELETE_IDS.size() > 0) {
            deleteString(DELETE_IDS);
        }
    }

    private static void deleteString(Map<String, List<String>> deleteIds) {
        if (deleteIds == null || deleteIds.size() == 0)
            return;
        Set<String> keys = deleteIds.keySet();
        for (String key : keys) {
            List<String> ids = deleteIds.get(key);
//            if (ids.size() == 1)
//                deleteStringInFile(ids.get(0), key);
//            else if (ids.size() > 1) {
//                deleteStringsInFile(ids, key);
//            } else {
//                return;
//            }
            deleteStringsInFile(ids, key);
        }
    }

    /*
     * del ： 要删除的字符串id
     * fileName:要删除的字符串所在的文件名
     * resDir:res目录
     */
    private static void deleteStringInFile(String del, String fileName) {
        File file = new File(res);
        for (File f : file.listFiles()) {
            if (!f.isDirectory())
                return;
            if (!f.getName().contains("value"))
                return;
            File deleteFile = new File(f, fileName);
            startDelete(deleteFile, del);
        }
    }

    private static void deleteStringsInFile(List<String> ids, String fileName) {
        File file = new File(res);

        for (File f : file.listFiles()) {
            if (!f.isDirectory())
                continue;
            if (!f.getName().contains("value"))
                continue;
            File deleteFile = new File(f, fileName);
            startDelete(deleteFile, ids);
        }
    }

    protected static void startDelete(File deleteFile, List<String> ids) {
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
                    if (ids.contains(strs[0].trim()))
                        continue;
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
    }

    /*
     * deleteFile :要删除的文件
     * del :要删除的字符串id:
     */

    protected static void startDelete(String deleteFilePath, String del) {
        startDelete(new File(deleteFilePath), del);
    }

    protected static void startDelete(File deleteFile, String del) {
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
                    if (strs[0].trim().equals(del))
                        continue;
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
    }
}
