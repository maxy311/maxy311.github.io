package com.wutian.xml.file;


import com.wutian.xml.file.task.Task;
import com.wutian.xml.file.task.TranslateTask;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TranslateHelper {
    private ExecutorService mFixedThreadPool = Executors.newFixedThreadPool(5);
    private List<Task> mTaskList = new ArrayList<>();

    private void endThreadPool() {
        if (mTaskList.isEmpty())
            return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    int i = 0;
                    try {
                        for (Task task : mTaskList) {
                            if (task.isDone()) {
                                i++;
                                continue;
                            } else
                                break;
                        }

                        if (i == mTaskList.size()) {
                            mFixedThreadPool.shutdownNow();
                            mFixedThreadPool = null;
                            break;
                        }
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void addTranslateToValues(String resPath, String translatePath) {
        addTranslate(resPath, translatePath);
        endThreadPool();
    }

    private void addTranslate(String resPath, String translatePath) {
        File originDir = new File(resPath);
        System.out.println(originDir.getAbsolutePath());
        if (!originDir.exists())
            return;

        File translateDir = new File(translatePath);
        if (!translateDir.exists())
            return;

        for (File translateFile : translateDir.listFiles()) {
            if (translateFile.isDirectory()) {
                File origin = new File(originDir, translateFile.getName());
                if (!origin.exists()) {
                    System.out.println(origin.getName());
                    continue;
                }
                addTranslate(origin.getAbsolutePath(), translateFile.getAbsolutePath());
            } else {
                try {
                    File origin = new File(originDir, translateFile.getName());
                    if (!origin.exists()) {
                        origin.createNewFile();
                        FileUtils.copyFile(translateFile, origin);
                    } else {
                        File resDir = originDir.getParentFile();
                        if (!resDir.getName().equals("res"))
                            continue;

                        File valuesDir = new File(resDir, "values");
                        if (valuesDir == null || !valuesDir.exists())
                            return;

                        File valueFile = new File(valuesDir, origin.getName());
                        if (valueFile == null || !valueFile.exists())
                            continue;

                        TranslateTask translateTask = new TranslateTask(valueFile, origin, translateFile);
                        mTaskList.add(translateTask);
                        mFixedThreadPool.submit(translateTask);
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public static void addTransValuesToRes(File valuesFile, File valueXXFile, File translateFile) {
        List<String> lines = FileUtils.readXmlToList(valuesFile);
        Map<String, String> transMap = FileUtils.readStringToMap(translateFile);
        Map<String, String> valueXXMap = FileUtils.readStringToMap(valueXXFile);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(valueXXFile)));
            String defaultValue;
            for (String line : lines) {
                String[] strs = line.trim().split("\">");
                if (line.contains("<string-array") || line.contains("<integer-array"))
                    strs = line.trim().split("\n");

                if (strs.length >= 2) {
                    String key = strs[0];
                    defaultValue = null;
                    if (transMap.containsKey(key)) {
                        defaultValue = transMap.get(key);
                    } else if (valueXXMap.containsKey(key)) {
                        defaultValue = valueXXMap.get(key);
                    }

                    if (null == defaultValue && valueXXMap.size() != 0) {
                        if (key.contains("translate") || key.contains("translatable")
                                || key.contains("<plurals name="))
                            continue;

                        System.out.println(valueXXFile.getParentFile().getName() + "    " + valueXXFile.getName() + "   don't contains " + key);
                        continue;
                    }

                    writer.write("    " + defaultValue);
                    writer.flush();
                    writer.newLine();
                } else {
                    writer.write(line);
                    writer.flush();
                    writer.newLine();
                }
            }

        } catch (Exception e) {
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
