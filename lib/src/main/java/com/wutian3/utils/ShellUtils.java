package com.wutian3.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ShellUtils {
    private static final String GIT_FOR_ALL_PATH = "/Users/maxiaoyu/Android/workspace/XmlUtilsAs/git-for-all.sh";
    public static void checkoutToTag(String tag) {
        try {
            String command = GIT_FOR_ALL_PATH + " checkout " + tag;
            Process ps = Runtime.getRuntime().exec(command);
            ps.waitFor();

            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            String result = sb.toString();
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("checkoutToTag  ---  " + e.toString());
        }
    }
}
