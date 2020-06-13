package com.wutian3;

import com.Constants;
import com.wutian.xml.file.FileUtils;
import com.wutian3.utils.ResFileFilter;
import com.wutian3.utils.ShellUtils;
import com.wutian3.utils.TranslateFilter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GetSHAREitTranslate {
    private static final String LAST_TAG = "v4.8.30_4_test";

    public static void main(String[] args) {
        GetSHAREitTranslate getSHAREitTranslate = new GetSHAREitTranslate();
        getSHAREitTranslate.startGetTranslate();
    }

    private void startGetTranslate() {
        //1. delete origin shareit file;
        deleteShareitFile(getDesktopShareitFile());

        File shareitPath = new File(Constants.SHAREit_PATH);
        //read values file to map
        Map<String, Map<String, Map<String, String>>> valuesMap = new HashMap<>(); // module -- file -- strings.
        readStringsToMap(valuesMap, shareitPath, "values");

        //read values-XX file to map
        Map<String, Map<String, Map<String, String>>> valuesXXMap = new HashMap<>();
        readStringsToMap(valuesXXMap, shareitPath, "values-ar");

        //read last tag values file to map
        ShellUtils.checkoutToTag(LAST_TAG);
        System.out.println("has checkout to:" + LAST_TAG);
        Map<String, Map<String, Map<String, String>>> preValueMap = new HashMap<>();
        readStringsToMap(preValueMap, shareitPath, "values");
        ShellUtils.checkoutToTag("master");

        printTranslateFile(valuesMap, valuesXXMap, preValueMap);
    }

    private void readStringsToMap(Map<String, Map<String, Map<String, String>>> valuesMap, File file, String valueDir) {
        if (file.isDirectory()) {
            for (File listFile : file.listFiles(new ResFileFilter())) {
                readStringsToMap(valuesMap, listFile, valueDir);
            }
        } else {
            if (!TranslateFilter.isStringsFile(file))
                return;

            if (!isValuesFile(file, valueDir))
                return;
            String fileModule = getFileModule(file);
            Map<String, Map<String, String>> mapMap = valuesMap.get(fileModule);
            if (mapMap == null)
                mapMap = new HashMap<>();

            Map<String, String> fileMap = FileUtils.readStringToLinkedHasMap(file);
            mapMap.put(file.getName(), fileMap);
            valuesMap.put(fileModule, mapMap);
        }
    }

    private String getFileModule(File file) {
        String parent = file.getParent();
        String module = parent.replace(Constants.SHAREit_PATH, "");
        module = module.replace("/", "_");
        if (module.contains("src"))
            return module.substring(1, module.indexOf("src") - 1);
        else
            return module.substring(1, module.indexOf("res") - 1);
    }

    private boolean isValuesFile(File file, String valueDir) {
        return valueDir.equals(file.getParentFile().getName());
    }

    private void printTranslateFile(Map<String, Map<String, Map<String, String>>> valuesMap, Map<String, Map<String, Map<String, String>>> valuesXXMap, Map<String, Map<String, Map<String, String>>> preValueMap) {
        Set<String> keySet = valuesMap.keySet();
        for (String key : keySet) {
            //key ----> module
            Map<String, Map<String, String>> valueFile = valuesMap.get(key);
            Map<String, Map<String, String>> valueXXFile = valuesXXMap == null ? null : valuesXXMap.get(key);
            Map<String, Map<String, String>> preValueFile = preValueMap == null ? null : preValueMap.get(key);
            outTranslateToFile(key, valueFile, valueXXFile, preValueFile);
        }
    }

    private void outTranslateToFile(String module, Map<String, Map<String, String>> valueMap, Map<String, Map<String, String>> valueXXMap, Map<String, Map<String, String>> preValueMap) {
        Set<String> keySet = valueMap.keySet();
        for (String key : keySet) {
            //key ----> fileName
            Map<String, String> valueFile = valueMap.get(key);
            Map<String, String> valueXXFile = valueXXMap == null ? null : valueXXMap.get(key);
            Map<String, String> preValueFile = preValueMap == null ? null : preValueMap.get(key);
            outTranslateToFile(module, key, valueFile, valueXXFile, preValueFile);
        }
    }

    private void outTranslateToFile(String module, String fileName, Map<String, String> valueMap, Map<String, String> valueXXMap, Map<String, String> preValueMap) {
        List<String> list = new ArrayList<>();

        //store keys to find zh values
        List<String> keys = new ArrayList<>();
        if (valueXXMap == null) {
            for (String key : valueMap.keySet()) {
                String value = valueMap.get(key);
                if (isNotTranslate(value))
                    continue;
                keys.add(key);
            }
            writeValuesToFile(module, fileName, list, keys);
            return;
        }

        for (String key : valueMap.keySet()) {
            String value = valueMap.get(key);
            if (isNotTranslate(value))
                continue;

            if (!valueXXMap.containsKey(key)) {
                keys.add(key);
                list.add(value);
                continue;
            }

            if (preValueMap == null)
                continue;

//            if (!preValueMap.containsKey(key)) {
//                keys.add(key);
//                list.add(value);
//                continue;
//            }
//
//            String preValue = preValueMap.get(key);
//            if (!preValue.equals(value)) {
//                keys.add(key);
//                list.add(value);
//            }
        }
        writeValuesToFile(module, fileName, list, keys);
    }

    private void writeValuesToFile(String module, String fileName, List<String> list, List<String> zhKeys) {
        if (list.isEmpty())
            return;

        writeListToFile(module, fileName, list, false);

        //try write values-zh-rCN file
        String moduleRealPath = Constants.SHAREit_PATH + "/" + module.replace("_", "/");
        String moduleResPath = moduleRealPath + "/src/main/res";
        File file = new File(moduleResPath);
        if (!file.exists()) {
            moduleResPath = moduleRealPath + "/res";
            file = new File(moduleResPath);
        }

        File zhDir = new File(file, "values-zh-rCN");
        if (!zhDir.exists()) {
            System.out.println("values-zh-rCN not exists :::: " + zhDir.getAbsolutePath());
            return;
        }

        File zhFile = new File(zhDir, fileName);
        if (!zhFile.exists()) {
            System.out.println("zhFile not exists :::: " + zhFile.getAbsolutePath());
            return;
        }

        list.clear();
        Map<String, String> zhMaps = FileUtils.readStringToMap(zhFile);
        for (String key : zhMaps.keySet()) {
            if (!zhKeys.contains(key))
                continue;
            list.add(zhMaps.get(key));
        }

        writeListToFile(module, fileName, list, true);
    }

    private void writeListToFile(String module, String fileName, List<String> list, boolean isZHFile) {
        File file = getModuleFile(module, fileName, isZHFile);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            //write header
            bw.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" + "<resources>\n");
            for (String str : list) {
                if (isNotTranslate(str))
                    continue;
                bw.write("    " + str + "\n");
            }
            bw.write("</resources>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isNotTranslate(String str) {
        if (str.contains("translate") || str.contains("translatable") || str.contains("translatable"))
            return true;
        if (str.contains("\">@string/"))
            return true;
        if (str.contains("<string name=\"app_name\">"))
            return true;
        return false;
    }

    private static File getModuleFile(String module, String fileName, boolean isZHFile) {
        File shareitFile = getDesktopShareitFile();
        File moduleDir = new File(shareitFile, module);
        if (!moduleDir.exists())
            moduleDir.mkdir();
        File valueDir = null;
        if (isZHFile)
            valueDir = new File(moduleDir, "values-zh-rCN");
        else
            valueDir = new File(moduleDir, "values");
        if (!valueDir.exists())
            valueDir.mkdir();
        File file = new File(valueDir, fileName);
        try {
            if (!file.exists())
                file.createNewFile();
        } catch (Exception e) {
        }
        return file;
    }

    private static File getDesktopShareitFile() {
        File file = new File(Constants.DESKTOP_PATH);
        File shareitFile = new File(file, "SHAREit");
        if (!shareitFile.exists())
            shareitFile.mkdir();
        return shareitFile;
    }


    private void deleteShareitFile(File desktopShareitFile) {
        if (desktopShareitFile.isDirectory()) {
            for (File listFile : desktopShareitFile.listFiles()) {
                deleteShareitFile(listFile);
            }

            if (desktopShareitFile.listFiles().length == 0)
                desktopShareitFile.delete();
        } else {
            desktopShareitFile.delete();
        }
    }
}
