package maxy.wutian.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import maxy.wutian.bean.StrEntity;

public class StringReader {
    public static List<StrEntity> readFileAllString(File file) {
        if (file.isDirectory() || file.isHidden())
            return Collections.emptyList();
        List<StrEntity> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                String trimLine = line.trim();
                if (!(trimLine.startsWith("<string") || trimLine.contains("plurals") || trimLine.contains("<item") || trimLine.contains("-array")))
                    continue;
                if (line.endsWith("</plurals>") || line.endsWith("</string>") || line.endsWith("</string-array>")) {
                    if (sb.length() != 0)
                        sb.append("\n" + "    " + line);
                    StrEntity strEntity = new StrEntity(sb.length() != 0 ? sb.toString() : line);
                    list.add(strEntity);
                    sb.setLength(0);
                } else if (trimLine.startsWith("<plurals") || trimLine.startsWith("<string-array")) {
                    sb.setLength(0);
                    sb.append(line);
                } else {
                    if (sb.length() != 0)
                        sb.append("\n" + "    " + line);
                }
            }
            return list;
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException " + e.toString());
        } catch (IOException e) {
            System.out.println("IOException" + e.toString());
        }
        return Collections.emptyList();
    }
}
