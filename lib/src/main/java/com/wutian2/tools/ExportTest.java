package com.wutian2.tools;

import com.wutian.xml.file.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class ExportTest {
    private static String SHAREIT_resPath = "/Users/maxy/Android/workspace/SHAREit/App/src/main/res";

    public static void main(String[] args) {
        exportSingleString("video_channel_title_recommend");
    }

    private static void exportSingleString(String strId) {
        File file = new File(SHAREIT_resPath);

        String exportPath = "/Users/maxy/Desktop/res";
        File exportDir = new File(exportPath);
        if (!exportDir.exists())
            exportDir.mkdir();
        for (File valuesFile : file.listFiles()) {
            System.out.println(valuesFile.getAbsolutePath());
            File xmlFile = new File(valuesFile, "video_strings.xml");
            System.out.println(xmlFile.getAbsolutePath() + "    " + xmlFile.exists());
            if (!xmlFile.exists())
                continue;

            File exportValueDir = new File(exportDir, valuesFile.getName());
            System.out.println(exportValueDir.getAbsolutePath());
            if (!exportValueDir.exists())
                exportValueDir.mkdir();

            File exportFile = new File(exportValueDir, "video_strings.xml");
            System.out.println(exportFile.getAbsolutePath());
            try {
                if (!exportFile.exists())
                    exportFile.createNewFile();
            } catch (Exception e) {
            }


            List<String> stringList = FileUtils.readXmlToList(xmlFile);
            for (String str : stringList) {
                if (str.contains(strId)) {
                    System.out.println(str);
                    writeStrToFile(str, exportFile);
                }
            }
        }
    }

    private static void writeStrToFile(String str, File exportFile) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(exportFile));
            writer.write(str);
            writer.flush();
        } catch (Exception e){
            try {
                if (writer != null)
                    writer.close();
            } catch (Exception e1){}
        }
    }


}
