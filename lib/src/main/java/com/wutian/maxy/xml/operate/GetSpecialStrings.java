package com.wutian.maxy.xml.operate;

import com.Constants;
import com.wutian.xml.file.FileUtils;
import com.wutian3.utils.TranslateFilter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GetSpecialStrings {
    private static final String SPLIT = "\">";

    private static final Map<String, List<String>> DELETE_IDS = new HashMap<>();

    ///////1111111111111111//////// App
    private static final String res = "/Users/maxy/Android/workspace/SHAREit/BizOnline/ModuleOnline/src/main/res";

    public static void main(String[] args) {
        GetSpecialStrings getSpecialStrings = new GetSpecialStrings();

//        //BaseCore -------> App
        File appRes = new File(Constants.APP + "/res/values");
        File baseCore = new File(Constants.BASECORE + "/res");
        getSpecialStrings.star(appRes, baseCore);

//            //BaseCore ------->Online
//        File onlineRes = new File(Constants.BIZONLINE_ONLINE + "/res");
//        File baseCore = new File(Constants.BASECORE + "/res/values");
//        getSpecialStrings.star(baseCore, onlineRes);

//        //BaseCore ------->BIZLOCAL_LOCALCOMMON
//        File localCommonRes = new File(Constants.BIZLOCAL_LOCALCOMMON + "/res");
//        File baseCore = new File(Constants.BASECORE + "/res/values");
//        getSpecialStrings.star(baseCore, localCommonRes);

        //BaseCore ------->BIZLOCAL_MODULELOCAL
//        File moduleLocalRes = new File(Constants.BIZLOCAL_MODULELOCAL + "/res");
//        File baseCore = new File(Constants.BASECORE + "/res/values");
//        getSpecialStrings.star(baseCore, moduleLocalRes);

//        //BaseCore ------->BIZLOCAL_MODULETRANSFER
//        File moduleTransRes = new File(Constants.BIZLOCAL_MODULETRANSFER + "/res");
//        File baseCore = new File(Constants.BASECORE + "/res/values");
//        getSpecialStrings.star(baseCore, moduleTransRes);

//        //BaseCore ------->BIZBASIC_MODULEDOWNLOAD
//        File basicDownlaod = new File(Constants.BIZBASIC_MODULEDOWNLOAD + "/res");
//        File baseCore = new File(Constants.BASECORE + "/res/values");
//        getSpecialStrings.star(baseCore, basicDownlaod);

//        //BaseCore ------->BIZBASIC_MODULEFEEDBACK
//        File basicFeedback = new File(Constants.BIZBASIC_MODULEFEEDBACK + "/res");
//        File baseCore = new File(Constants.BASECORE + "/res/values");
//        getSpecialStrings.star(baseCore, basicFeedback);

//        //BaseCore ------->BIZBASIC_MODULEINVITE
//        File basicInvite = new File(Constants.BIZBASIC_MODULEINVITE + "/res");
//        File baseCore = new File(Constants.BASECORE + "/res/values");
//        getSpecialStrings.star(baseCore, basicInvite);

//        //BaseCore ------->BIZBASIC_MODULEINVITE
//        File basicLogin = new File(Constants.BIZBASIC_MODULELOGIN+ "/res");
//        File baseCore = new File(Constants.BASECORE + "/res/values");
//        getSpecialStrings.star(baseCore, basicLogin);

//        //BaseCore ------->BIZBASIC_MODULENOTIFY
//        File basicNotify = new File(Constants.BIZBASIC_MODULENOTIFY+ "/res");
//        File baseCore = new File(Constants.BASECORE + "/res/values");
//        getSpecialStrings.star(baseCore, basicNotify);

//        //BaseCore ------->BIZBASIC_MODULEROUTER
//        File basicRouter = new File(Constants.BIZBASIC_MODULEROUTER+ "/res");
//        File baseCore = new File(Constants.BASECORE + "/res/values");
//        getSpecialStrings.star(baseCore, basicRouter);

        //BaseCore ------->BIZBASIC_MODULESETTING
//        File basicSettings = new File(Constants.BIZBASIC_MODULESETTING+ "/res");
//        File baseCore = new File(Constants.BASECORE + "/res/values");
//        getSpecialStrings.star(baseCore, basicSettings);

//        BaseCore ------->BIZBASIC_MODULEUPGRADE
//        File basicUpgrade = new File(Constants.BIZBASIC_MODULEUPGRADE+ "/res");
//        File baseCore = new File(Constants.BASECORE + "/res/values");
//        getSpecialStrings.star(baseCore, basicUpgrade);

//        //BaseCore ------->BIZPAY_MODULEPAY
//        File bizPay = new File(Constants.BIZPAY_MODULEPAY+ "/res");
//        File baseCore = new File(Constants.BASECORE + "/res/values");
//        getSpecialStrings.star(baseCore, bizPay);

                //BaseCore ------->BIZPAY_MODULEPAY
//        File bizGame = new File(Constants.BIZGAME_MODULEGAME + "/res");
//        File baseCore = new File(Constants.BASECORE + "/res/values");
//        getSpecialStrings.star(baseCore, bizGame);

//        //BaseCore -------> App
//        File appRes = new File(Constants.APP + "/res");
//        File baseCore = new File(Constants.BIZONLINE_ONLINE + "/res/values");
//        getSpecialStrings.star(baseCore, appRes);

//        //BIZLOCAL_LOCALCOMMON ------->App
//        File bizlocalCommon = new File(Constants.BIZLOCAL_LOCALCOMMON + "/res/values");
//        getSpecialStrings.star(bizlocalCommon, appRes);

//        BIZLOCAL_MODULELOCAL ------->App
//        File moduleLocal = new File(Constants.BIZLOCAL_MODULELOCAL + "/res/values");
//        getSpecialStrings.star(moduleLocal, appRes);

//        //BaseCore ------->BIZLOCAL_MODULETRANSFER
//        File moduleTransRes = new File(Constants.BIZLOCAL_MODULETRANSFER + "/res/values");
//        getSpecialStrings.star(moduleTransRes, appRes);

////        //BaseCore ------->BIZBASIC_MODULEDOWNLOAD
//        File basicDownlaod = new File(Constants.BIZBASIC_MODULEDOWNLOAD + "/res/values");
//        getSpecialStrings.star(basicDownlaod, appRes);

//        //BaseCore ------->BIZBASIC_MODULEFEEDBACK
//        File basicFeedback = new File(Constants.BIZBASIC_MODULEFEEDBACK + "/res/values");
//        getSpecialStrings.star(basicFeedback, appRes);

//        //BaseCore ------->BIZBASIC_MODULEINVITE
//        File basicInvite = new File(Constants.BIZBASIC_MODULEINVITE + "/res/values");
//        getSpecialStrings.star(basicInvite, appRes);

//        //BaseCore ------->BIZBASIC_MODULEINVITE
//        File basicLogin = new File(Constants.BIZBASIC_MODULELOGIN+ "/res/values");
//        getSpecialStrings.star(basicLogin, appRes);

//        //BaseCore ------->BIZBASIC_MODULENOTIFY
//        File basicNotify = new File(Constants.BIZBASIC_MODULENOTIFY+ "/res/values");
//        getSpecialStrings.star(basicNotify, appRes);

//        //BaseCore ------->BIZBASIC_MODULEROUTER
//        File basicRouter = new File(Constants.BIZBASIC_MODULEROUTER+ "/res/values");
//        getSpecialStrings.star(basicRouter, appRes);

        //BaseCore ------->BIZBASIC_MODULESETTING
//        File basicSettings = new File(Constants.BIZBASIC_MODULESETTING+ "/res/values");
//        getSpecialStrings.star(basicSettings, appRes);

//        BaseCore ------->BIZBASIC_MODULEUPGRADE
//        File basicUpgrade = new File(Constants.BIZBASIC_MODULEUPGRADE+ "/res/values");
//        getSpecialStrings.star(basicUpgrade, appRes);

//        //BaseCore ------->BIZPAY_MODULEPAY
//        File bizPay = new File(Constants.BIZPAY_MODULEPAY+ "/res/values");
//        getSpecialStrings.star(bizPay, appRes);

        //BaseCore ------->BIZPAY_MODULEPAY
//        File bizGame = new File(Constants.BIZGAME_MODULEGAME + "/res/values");
//        getSpecialStrings.star(bizGame, appRes);

//        File bizGame = new File(Constants.APP + "/res/values");
//        appRes = new File(Constants.BIZBASIC_MODULESETTING+ "/res");
//        getSpecialStrings.star(bizGame, appRes);
    }

    private void star(File checkFile, File deleteRes) {
        for (File listFile : checkFile.listFiles(new TranslateFilter())) {
            start(listFile, deleteRes);
        }
    }

    private void start(File checkFile, File deleteDir){
        List<String> originList = FileUtils.readXmlToList(checkFile);
        Set<String> keys = new HashSet<>();
        for (String line : originList) {
            String trim = line.trim();
            String[] split = trim.split(SPLIT);
            if (split.length >= 2) {
                keys.add(split[0]);
            } else
                System.out.println("--------" + trim);
        }

        System.out.println("--------- "  + keys);
        int i = 0;
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
            System.out.println(getList);
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();

            if (getList.isEmpty())
                continue;

            if (!copySet.isEmpty()) {
                System.out.println("Error:::::  " + copySet.size() + "        " + copySet.toString());
            }
            writeToFile(name, checkFile.getName(), getList);
        }
        System.out.println(i);
    }
    private void start(String fileName) {
        /////////////2222222222222222////////////////
        String filePath = "/Users/maxy/Android/workspace/SHAREit/BaseCore/src/main/res/values/" + fileName;
        List<String> originList = FileUtils.readXmlToList(filePath);

        Set<String> keys = new HashSet<>();
        for (String line : originList) {
            String trim = line.trim();
            String[] split = trim.split(SPLIT);
            if (split.length >= 2) {
                keys.add(split[0]);
            } else
                System.out.println("--------" + trim);
        }

        System.out.println("--------- "  + keys);
        int i = 0;
        File resPath = new File(res);
        for (File file : resPath.listFiles()) {
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
            System.out.println(getList);
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();

            if (getList.isEmpty())
                continue;

            if (!copySet.isEmpty()) {
                System.out.println("Error:::::  " + copySet.size() + "        " + copySet.toString());
            }
            writeToFile(name, fileName, getList);

        }
        System.out.println(i);
    }

    private void writeToFile(String dirName, String fileName, List<String> getList) {
        /////////////3333333333333333333/////////
        File deskResFile = new File("/Users/maxy/Desktop/res");
        if (!deskResFile.exists())
            deskResFile.mkdir();
        File valueFile = new File(deskResFile, dirName);
        if (!valueFile.exists())
            valueFile.mkdir();

        File targeFile = new File(valueFile, fileName);
        if (!targeFile.exists()) {
            try {
                targeFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targeFile)));
            writer.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<resources>");
            writer.flush();
            writer.newLine();
            for (String line : getList) {
                line = "" + line;
                writer.write(line);
                writer.flush();
                writer.newLine();
            }

            writer.write("</resources>");
            writer.flush();
            writer.newLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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

        if (!deleteFile.getName().contains("strings"))
            return;

        Map<String, String> stringMap = FileUtils.readStringToMap(deleteFile);
        if (stringMap.isEmpty())
            deleteFile.delete();
    }
}
