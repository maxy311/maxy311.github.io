package com.test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RenameString {
    private static String DESKTOP_PATH = "/Users/maxy/Desktop/res";
    private static Map<String, String> nameMaps = new HashMap<>();

    static {
        nameMaps.put("英语", "values");
        nameMaps.put("阿拉伯语", "values-ar");
        nameMaps.put("保加利亚语", "values-bg");
        nameMaps.put("孟加拉语", "values-bn");
        nameMaps.put("捷克语", "values-cs");
        nameMaps.put("德语", "values-de");
        nameMaps.put("希腊语", "values-el");
        nameMaps.put("西班牙语", "values-es");
        nameMaps.put("爱沙尼亚语", "values-et");
        nameMaps.put("波斯语", "values-fa");
        nameMaps.put("芬兰语", "values-fi");
        nameMaps.put("法语", "values-fr");
        nameMaps.put("印地语", "values-hi");
        nameMaps.put("克罗地亚", "values-hr");
        nameMaps.put("匈牙利语", "values-hu");
        nameMaps.put("印度尼西亚语", "values-in");
        nameMaps.put("意大利语", "values-it");
        nameMaps.put("希伯来语", "values-iw");
        nameMaps.put("日语", "values-ja");
        nameMaps.put("韩语", "values-ko");
        nameMaps.put("立陶宛语", "values-lt");
        nameMaps.put("拉脱维亚语", "values-lv");
        nameMaps.put("马来语", "values-ms");
        nameMaps.put("波兰语", "values-pl");
        nameMaps.put("巴西葡萄牙语", "values-pt-rBR");
        nameMaps.put("葡萄牙葡萄牙语", "values-pt-rPT");
        nameMaps.put("罗马尼亚语", "values-ro");
        nameMaps.put("俄语", "values-ru");
        nameMaps.put("斯洛伐克", "values-sk");
        nameMaps.put("斯洛文尼亚", "values-sl");
        nameMaps.put("塞尔维亚", "values-sr");
        nameMaps.put("泰国语", "values-th");
        nameMaps.put("土耳其语", "values-tr");
        nameMaps.put("乌克兰语", "values-uk");
        nameMaps.put("越南语", "values-vi");
        nameMaps.put("简体中文", "values-zh-rCN");
        nameMaps.put("繁体中文（香港）", "values-zh-rHK");
        nameMaps.put("繁体中文（台湾）", "values-zh-rTW");
        nameMaps.put("乌尔都语", "values-ur");
        nameMaps.put("泰卢固语", "values-te");
        nameMaps.put("泰米尔语", "values-ta");
        nameMaps.put("马拉地语", "values-mr");
        nameMaps.put("旁遮普语", "values-pa");
        nameMaps.put("卡纳达语", "values-kn");
        nameMaps.put("马拉雅拉姆语", "values-ml");
    }

    public static void main(String[] args) {
        File file = new File(DESKTOP_PATH);
        Set<String> keySet = nameMaps.keySet();
        for (File file1 : file.listFiles()) {
            String name = file1.getName();
            if (keySet.contains(name)) {
                String absolutePath = file1.getAbsolutePath();
                String replace = absolutePath.replace(name, nameMaps.get(name));
                File replaceFile = new File(replace);
                System.out.println(replaceFile.getAbsolutePath());
                file1.renameTo(replaceFile);
            }
        }
    }
}
