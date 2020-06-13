package com.wutian2.operate;

import com.wutian.xml.file.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ErrorStringCheck {
    public static String DESKTOP_PATH = "/Users/maxy/Desktop";
    public static String SHAREIT_resPath = "/Users/maxy/Android/workspace/SHAREit/App/src/main/res";

    public static void main(String[] args) {
        ErrorStringCheck errorStringCheck = new ErrorStringCheck();
        errorStringCheck.init();
        errorStringCheck.startCheck();
    }

    private ExecutorService mExecutorService = Executors.newCachedThreadPool();
    private List<Future> mTaskList = new ArrayList<>();

    private Map<String, Map<String, String>> mValueXMlMap = new HashMap<>();
    private Map<String, List<String>> mNotExitErrorInfo = new HashMap<>();
    private List<String> mErrorInfo = new ArrayList<>();

    private Map<String, String[]> mEqualsMap = new HashMap<>();
    private static String[] CHAR_ARRAYS = {
            "%s", "%S", "%d", "%1$s", "%1$d", "%2$s", "%d$s", "%3$s", "%3$d", "%1$t", "%1$S",
    };

    private void startCheck() {
        File[] valueDirs = getValuesDir();
        for (File valueDir : valueDirs) {
            if (valueDir.getName().equals("values"))
                continue;

            File[] xmlFiles = getValueXmlFiles(valueDir);
            for (final File xmlFile : xmlFiles) {
                Future<?> submit = mExecutorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        checkErrorChar(xmlFile);
                    }
                });
                mTaskList.add(submit);
            }
        }
        checkToShutDownService();
    }

    private void init() {
        builderEqualsMap();
        buildValueXMLMap();
    }

    private void builderEqualsMap() {
        String[] SPECIAL_CHARS = {"%s", "%S", "%d", "%1$s", "%1$S", "%1$d", "%1$t"};
        for (String specialChar : SPECIAL_CHARS) {
            mEqualsMap.put(specialChar, SPECIAL_CHARS);
        }
    }

    private void buildValueXMLMap() {
        File resDir = new File(SHAREIT_resPath);
        File valueDir = new File(resDir, "values");
        if (!valueDir.exists())
            throw new RuntimeException("valueDir path error");
        for (File xmlFile : getValueXmlFiles(valueDir)) {
            Map<String, String> stringStringMap = FileUtils.readStringToMap(xmlFile);
            mValueXMlMap.put(xmlFile.getName(), stringStringMap);
        }
    }

    private File[] getValueXmlFiles(File valueDir) {
        return valueDir.listFiles(new FileFilter() {
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
    }

    private File[] getValuesDir() {
        File resDir = new File(SHAREIT_resPath);
        return resDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String name = pathname.getName();

                if (name.startsWith("values")) {
                    if (name.contains("xlarge"))
                        return false;
                    else if (name.contains("dp"))
                        return false;
                    else if (name.equals("values-v21") || name.equals("values-v9") || name.equals("values-en"))
                        return false;
                    else if (name.contains("land"))
                        return false;
                    else if (name.equals("values-en"))
                        return false;
                    else
                        return true;
                }

                return false;
            }
        });
    }

    private void checkToShutDownService() {
        new Runnable() {
            @Override
            public void run() {
                while (true) {
                    boolean hasAllDone = true;
                    for (Future future : mTaskList) {
                        if (!future.isDone()) {
                            hasAllDone = false;
                            break;
                        }
                    }
                    if (hasAllDone) {
                        try {
                            Thread.sleep(5000);
                        } catch (Exception e) {
                        }
                        mExecutorService.shutdown();
                        printError();
                        System.out.println("------------------  All Done        =================");
                        return;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }
                }
            }
        }.run();

    }

    private void checkErrorChar(File xmlFile) {
        String xmlFileName = xmlFile.getName();
        Map<String, String> valueXMLMap = mValueXMlMap.get(xmlFileName);
        if (mValueXMlMap == null)
            throw new RuntimeException("valueXMLMap get Error:::: " + xmlFileName + "");
        if (mValueXMlMap.isEmpty()) {
            System.out.println("CHECK :::::: " + xmlFileName);
        }
        Map<String, String> xmlMap = FileUtils.readStringToMap(xmlFile);

        Set<String> allKeys = valueXMLMap.keySet();
        for (String key : allKeys) {
            if (key.contains("video_history_last_pos_0"))
                continue;

            if (key.contains("<plurals"))
                continue;

            if (!xmlMap.containsKey(key)) {
                continue;
            }

            String originValue = valueXMLMap.get(key);
            String compareValue = xmlMap.get(key);
            if (originValue.contains("%")) {
                List<String> list = new ArrayList<>();
                for (String charArray : CHAR_ARRAYS) {
                    if (originValue.contains(charArray)) {
                        list.add(charArray);
                    }
                }

                if (list.isEmpty()) {
                    collectionNotExitInfo(xmlFile, key, originValue, compareValue, "values contains %, but not exit 字典::::::");
                } else {
                    for (String charStr : list) {
                        if (compareValue.contains(charStr))
                            continue;
                        if (list.size() == 1) {
                            boolean hasMatched = false;
                            String[] equalArrays = mEqualsMap.get(charStr);
                            for (String equalStr : equalArrays) {
                                if (compareValue.contains(equalStr)) {
                                    String errorInfo = charStr + " ======  equals  === " + equalStr;
                                    mErrorInfo.add(errorInfo);
                                    hasMatched = true;
                                    break;
                                }
                            }
                            if (hasMatched)
                                continue;
                        }
                        String errorInfo = "values contains " + charStr + ", but values- not contains:::: "
                                + '\n'
                                + originValue
                                + '\n'
                                + compareValue
                                + '\n'
                                + xmlFile.getParentFile().getName() + "      " + xmlFileName + "    " + key;
                        mErrorInfo.add(errorInfo);
                    }
                }
            } else if (compareValue.contains("%s")) {
                collectionNotExitInfo(xmlFile, key, originValue, compareValue, "compareValue not contains %, but values- contains %s::::-------");
            }
        }
    }

    private void collectionNotExitInfo(File xmlFile, String key, String originValue, String compareValue, String errorTag) {
        if (mNotExitErrorInfo.containsKey(key)) {
            List<String> errorList = mNotExitErrorInfo.get(key);
            errorList.add(xmlFile.getParentFile().getName() + "      " + xmlFile.getName() + '\n');
        } else {
            String errorInfo = errorTag
                    + '\n'
                    + originValue
                    + '\n'
                    + compareValue
                    + '\n'
                    + xmlFile.getParentFile().getName() + "      " + xmlFile.getName()
                    + '\n';
            List<String> errorList = new ArrayList<>();
            errorList.add(errorInfo);
            mNotExitErrorInfo.put(key, errorList);
        }
    }

    private void printStr(BufferedWriter writer, String str) {
        try {
            writer.write(str);
            writer.flush();
        }catch (Exception e) {}
    }

    private void printError() {
        printNotExitErrorInfo();
        printErrorInfo();

    }

    private void printErrorInfo() {
        BufferedWriter errorWriter = null;
        try {
            File deskDir = new File(DESKTOP_PATH);
            File errorFile = new File(deskDir, "error.xml");
            if (!errorFile.exists())
                errorFile.createNewFile();

            errorWriter = new BufferedWriter(new FileWriter(errorFile));
            for (String str : mErrorInfo) {
                printStr(errorWriter, str);
                errorWriter.newLine();
                errorWriter.newLine();
                errorWriter.flush();
            }
        } catch (Exception e) {
        } finally {
            if (errorWriter != null) {
                try {
                    errorWriter.close();
                } catch (Exception e) {
                }
            }
        }
    }

    private void printNotExitErrorInfo() {
        if (mNotExitErrorInfo.isEmpty())
            return;
        BufferedWriter notExitWriter = null;
        try {
            File deskDir = new File(DESKTOP_PATH);
            File notExitFile = new File(deskDir, "not_exit.xml");
            if (!notExitFile.exists())
                notExitFile.createNewFile();
            notExitWriter = new BufferedWriter(new FileWriter(notExitFile));
            Set<String> allKey = mNotExitErrorInfo.keySet();
            for (String key : allKey) {
                printStr(notExitWriter, key);
                notExitWriter.newLine();
                List<String> list = mNotExitErrorInfo.get(key);
                for (String str : list) {
                    printStr(notExitWriter, str);
                }

                notExitWriter.newLine();
                notExitWriter.newLine();
                notExitWriter.newLine();
                notExitWriter.flush();
            }
        } catch (Exception e) {
        } finally {
            if (notExitWriter != null) {
                try {
                    notExitWriter.close();
                } catch (Exception e) {
                }
            }
        }
    }

}
