package com.test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestJava {
    private static final String app = "/Users/maxy/Android/workspace/SHAREit/App/src/main/java";
    private static final String baseCore = "/Users/maxy/Android/workspace/SHAREit/BaseCore/src/main/java";

    private static final String basesdk_basebiz = "/Users/maxy/Android/workspace/SHAREit/BaseSDK/BaseBiz/src/main/java";
    private static final String basesdk_common = "/Users/maxy/Android/workspace/SHAREit/BaseSDK/BaseCommon/src/main/java";


    private static final String bizbasic_ModuleSetting = "/Users/maxy/Android/workspace/SHAREit/BizBasic/ModuleSetting/src/main/java";
    private static final String bizbasic_ModuleNotify = "/Users/maxy/Android/workspace/SHAREit/BizBasic/ModuleNotify/src/main/java";
    private static final String bizbasic_ModuleInvite = "/Users/maxy/Android/workspace/SHAREit/BizBasic/ModuleInvite/src/main/java";
    private static final String bizbasic_ModuleUpgrade = "/Users/maxy/Android/workspace/SHAREit/BizBasic/ModuleUpgrade/src/main/java";
    private static final String bizbasic_ModuleFeedback = "/Users/maxy/Android/workspace/SHAREit/BizBasic/ModuleFeedback/src/main/java";
    private static final String bizbasic_ModuleLogin = "/Users/maxy/Android/workspace/SHAREit/BizBasic/ModuleLogin/src/main/java";
    private static final String bizbasic_ModuleRouter = "/Users/maxy/Android/workspace/SHAREit/BizBasic/ModuleRouter/src/main/java";
    private static final String bizbasic_ModuleDownload = "/Users/maxy/Android/workspace/SHAREit/BizBasic/ModuleDownload/src/main/java";

    private static final String bizlocal_LocalCommon = "/Users/maxy/Android/workspace/SHAREit/BizLocal/LocalCommon/src/main/java";
    private static final String bizlocal_ModuleLocal = "/Users/maxy/Android/workspace/SHAREit/BizLocal/ModuleLocal/src/main/java";
    private static final String bizlocal_ModuleTransfer = "/Users/maxy/Android/workspace/SHAREit/BizLocal/ModuleTransfer/src/main/java";


    private static final String bizOnline_Online = "/Users/maxy/Android/workspace/SHAREit/BizOnline/Online/src/main/java";
    private static final String bizPay_ModulePay = "/Users/maxy/Android/workspace/SHAREit/BizPay/ModulePay/src/main/java";


    private static final String BizGame = "/Users/maxy/Android/workspace/SHAREit/BizGame/src/main/java";
    private static final String LibSDK = "/Users/maxy/Android/workspace/SHAREit/LibSDK/src/main/java";
    private static final String PlayerSDK = "/Users/maxy/Android/workspace/SHAREit/PlayerSDK/src/main/java";
    private static final String SDK = "/Users/maxy/Android/workspace/SHAREit/SDK/src/main/java";
    private static final String ShareAd = "/Users/maxy/Android/workspace/SHAREit/ShareAd/src/main/java";

    private static List<String> path = new ArrayList<>();
    static{
        path.add(app);
        path.add(baseCore);
        path.add(basesdk_basebiz);
        path.add(basesdk_common);
        path.add(bizbasic_ModuleSetting);
        path.add(bizbasic_ModuleNotify);
        path.add(bizbasic_ModuleInvite);
        path.add(bizbasic_ModuleUpgrade);
        path.add(bizbasic_ModuleFeedback);
        path.add(bizbasic_ModuleLogin);
        path.add(bizbasic_ModuleRouter);
        path.add(bizbasic_ModuleDownload);
        path.add(bizlocal_LocalCommon);
        path.add(bizlocal_ModuleLocal);
        path.add(bizlocal_ModuleTransfer);
        path.add(bizOnline_Online);
        path.add(bizPay_ModulePay);
        path.add(BizGame);
        path.add(LibSDK);
        path.add(PlayerSDK);
        path.add(SDK);
        path.add(ShareAd);
    }

    private static Map<String, Integer> map = new HashMap<>();
    public static void main(String[] args) {
        for (String itemPath : path) {
            readStringToPath(itemPath, new File(itemPath));
        }
    }

    private static void readStringToPath(String itemPath, File dirFile) {
        if (dirFile.isDirectory()) {
            for (File listFile : dirFile.listFiles()) {
                readStringToPath(itemPath, listFile);
            }
        } else {
            String absolutePath = dirFile.getAbsolutePath();
            absolutePath = absolutePath.replace(itemPath, "");
            int count = map.containsKey(absolutePath) ? map.get(absolutePath) : 1;
            if (count > 1) {
                System.out.println(absolutePath + "       " + count);
            }
            map.put(absolutePath, count + 1);
        }
    }
}
