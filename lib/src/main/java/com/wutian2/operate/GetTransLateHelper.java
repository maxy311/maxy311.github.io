package com.wutian2.operate;

import com.wutian.xml.file.FileUtils;
import com.wutian.xml.file.task.CompleteCompareTask;
import com.wutian.xml.file.task.Task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GetTransLateHelper implements CompleteCompareTask.CompareTaskListener {
    private ExecutorService mFixedThreadPool = Executors.newFixedThreadPool(5);
    private List<Task> mTaskList = new ArrayList<>();
    private GetTransLateListener mGetTransLateListener;
    public interface GetTransLateListener {
        void notifyGetTransLateEnd();
    }

    public void setGetTransLateListener(GetTransLateListener getTransLateListener) {
        this.mGetTransLateListener = getTransLateListener;
    }

    private void endThreadPool() {
        if (mTaskList.isEmpty()) {
            notifyGetTransLateEnd();
            return;
        }
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
                            notifyGetTransLateEnd();
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

    private void notifyGetTransLateEnd() {
        if (mGetTransLateListener != null)
            mGetTransLateListener.notifyGetTransLateEnd();
    }

    public void getTranslate(File valueDir, File valueArDir, File oldValueDir, String saveDirPath) {
        checkFileIsExit(valueDir);
        checkFileIsExit(valueArDir);
        checkFileIsExit(oldValueDir);

        File saveDir = new File(saveDirPath);
        if (!saveDir.exists())
            saveDir.mkdir();


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

        for (File valueFile : files) {
            String name = valueFile.getName();
            File arFile = new File(valueArDir, name);
            File oldFile = new File(oldValueDir, name);

            CompleteCompareTask completeCompareTask = new CompleteCompareTask(this, valueFile, arFile, oldFile, saveDir);
            mTaskList.add(completeCompareTask);
            mFixedThreadPool.submit(completeCompareTask);
        }
        endThreadPool();
    }


    /*
    * real to get Translate
    * */
    @Override
    public void startGetTranslate(File valueFile, File valueArFile, File oldValueFile, File saveDir) {
        File resFile = valueFile.getParentFile().getParentFile();
        File valueZHDir = new File(resFile, "values-zh-rCN");
        if (valueZHDir == null || !valueZHDir.exists())
            throw new RuntimeException("value zh-rCN dir not exit   " + valueZHDir.getAbsolutePath());

        String fileName = valueFile.getName();
        File valueZHFile = new File(valueZHDir, fileName);
        File saveValueFile = getSaveValueFile(saveDir, fileName);
        File saveZHValueFile = getSaveZHValueFile(saveDir, fileName);
        //if the valueFile is new file, need translate
        if (valueArFile == null || !valueArFile.exists()) {
            FileUtils.copyFile(valueFile, saveValueFile);
            if (valueZHFile != null && valueZHFile.exists()) {
                FileUtils.copyFile(valueZHFile, saveZHValueFile);
                saveZHValueFile.delete();
            } else
                System.out.println("need to add zh-rCN file : " + fileName);
            return;
        }

        Map<String, String> valueMap = FileUtils.readStringToLinkedHasMap(valueFile);
        Map<String, String> valueArMap = FileUtils.readStringToLinkedHasMap(valueArFile);
        Map<String, String> oldValueMap = FileUtils.readStringToLinkedHasMap(oldValueFile);
        List<String> translateList = new ArrayList<>();
        Set<String> valueKeys = valueMap.keySet();
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(saveValueFile)));
            writerFileHeader(writer);
            for (String key : valueKeys) {
                String value = valueMap.get(key);
                boolean isNeedTranslate = checkToNeedTranslate(key, value);
                if (!isNeedTranslate) {
                    continue;
                }

                if (!valueArMap.containsKey(key)) {
                    writerString(writer, value);
                    translateList.add(key);
                } else if (oldValueMap.containsKey(key)) {
                    String oldValue = oldValueMap.get(key);

                    if (value.equals(oldValue))
                        continue;
                    writerString(writer, value);
                    translateList.add(key);
                }
            }
            writerFileEnd(writer);
        } catch (IOException e) {
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        if (translateList.isEmpty()) {
            saveValueFile.delete();
            saveZHValueFile.delete();
        } else {
            tryToGetZhString(valueZHFile, saveZHValueFile, translateList);
        }
    }

    private boolean checkToNeedTranslate(String key, String value) {
        if (key.contains("translate") || key.contains("translatable") || key.contains("<plurals name="))
            return false;

        if (value.contains("\">@string"))
            return false;
        else if (value.startsWith("<item"))
            return false;
        else if (value.startsWith("<integer-array"))
            return false;
        return true;
    }

    private void tryToGetZhString(File valueZHFile, File saveZHValueFile, List<String> translateList) {
        if (valueZHFile == null || !valueZHFile.exists()) {
            System.out.println("need to add zh-rCN file : " + valueZHFile.getName());
            return;
        }
        Map<String, String> valueZHMap = FileUtils.readStringToLinkedHasMap(valueZHFile);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(saveZHValueFile)));
            writerFileHeader(writer);
            for (String key : translateList) {
                if (valueZHMap.containsKey(key)) {
                    writerString(writer, valueZHMap.get(key));
                } else {
                    System.out.println(valueZHFile.getName() + "       zh-rCN file not contains key : " + key);
                }
            }
            writerFileEnd(writer);
        } catch (IOException e) {

        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException e1) {}
        }
    }

    private static final void writerString(BufferedWriter writer, String strValue) throws IOException {
        writer.write("    " + strValue);
        writer.flush();
        writer.newLine();
    }

    private static final void writerFileEnd(BufferedWriter writer) throws IOException {
        writer.write("</resources>");
        writer.flush();
    }

    private static final void writerFileHeader(BufferedWriter writer) throws IOException {
        writer.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        writer.newLine();
        writer.write("<resources>");
        writer.flush();
        writer.newLine();
    }

    private File getSaveValueFile(File saveDir, String fileName){
        File saveValueFile = null;
        if (!"values".equals(saveDir.getName())) {
            saveValueFile = new File(saveDir, "values");
            if (!saveValueFile.exists())
                saveValueFile.mkdirs();
        }

        saveValueFile = new File(saveValueFile, fileName);
        try {
            if (!saveValueFile.exists())
                saveValueFile.createNewFile();
        } catch (IOException e) {}

        if (saveValueFile == null || !saveValueFile.exists())
            throw new RuntimeException("can not create save value file");
        return saveValueFile;
    }

    private File getSaveZHValueFile(File saveDir, String fileName){
        File saveCNValueFile = null;
        if (!"values-zh".equals(saveDir.getName())) {
            saveCNValueFile = new File(saveDir, "values-zh");
            if (!saveCNValueFile.exists())
                saveCNValueFile.mkdirs();
        }

        saveCNValueFile = new File(saveCNValueFile, fileName);
        try {
            if (!saveCNValueFile.exists())
                saveCNValueFile.createNewFile();
        } catch (IOException e) {}

        if (saveCNValueFile == null || !saveCNValueFile.exists())
            throw new RuntimeException("can not create save CN value file");
        return saveCNValueFile;
    }

    private static void checkFileIsExit(File file) {
        if (file == null || !file.exists())
            throw new RuntimeException(file.getAbsolutePath() + " not exit");
    }
}
