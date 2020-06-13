package com.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class DimensTest {
    private static String SHAREIT_resPath = "/Users/maxy/Android/workspace/SHAREit/App/src/main/res";

    public static void main(String[] args) {
        String path = SHAREIT_resPath + "/values-xhdpi/musicplayer_dimens.xml";
        File dimensFile = new File(path);

        File targeFile = new File(SHAREIT_resPath + "/values-xhdpi/musicplayer_dimens2.xml");
        try {
            if (!targeFile.exists())
                targeFile.createNewFile();
        } catch (Exception e) {
        }

        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            br = new BufferedReader(new FileReader(dimensFile));
            bw = new BufferedWriter(new FileWriter(targeFile));
            String line = null;
            while ((line = br.readLine()) != null) {
                if (!line.contains("<dimen name=\"common_dimens_")) {
                    bw.write(line);
                    bw.flush();
                    bw.newLine();
                    continue;
                }

                String dimensStr = line.substring(line.indexOf("\">") + 2, line.indexOf("</dimen>"));
                int length = dimensStr.length();
                String dp = dimensStr.substring(length -2, length);
                float aaa = Float.parseFloat(dimensStr.substring(0, length - 2));
                System.out.println(aaa);
                String replace = (int)(aaa * 2) + dp;
                System.out.println(replace);
                line = line.replace(dimensStr + "</dimen>", replace + "</dimen>");

                bw.write(line);
                bw.flush();
                bw.newLine();
//                System.out.println(dimensStr);
            }
        } catch (Exception e){}
        finally {

            try {
                if (br != null)
                    br.close();
            } catch (Exception e1){}

            try {
                if (bw != null)
                    bw.close();
            } catch (Exception e1){}

        }
    }
}
