package maxy.wutian.bean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FileStrEntity {
    private File file;
    private String fileName;
    private Map<String, StrEntity> strEntityMap;
    private String moduleName;

    public FileStrEntity(File file, String moduleName) {
        this.moduleName = moduleName;
        this.file = file;
        fileName = file.getName();
        strEntityMap = readFileAllString(file);
    }


    public Map<String, StrEntity> getStrEntityMap() {
        return strEntityMap;
    }

    public String getModuleName() {
        return moduleName;
    }

    public File getFile() {
        return file;
    }

    public String getFileName() {
        return fileName;
    }

    public static Map<String, StrEntity> readFileAllString(File file) {
        if (file.isDirectory() || file.isHidden())
            return Collections.emptyMap();
        Map<String, StrEntity> map = new LinkedHashMap<>();
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
                    map.put(strEntity.getStringKey(), strEntity);
                    sb.setLength(0);
                } else if (trimLine.startsWith("<plurals") || trimLine.startsWith("<string-array")) {
                    sb.setLength(0);
                    sb.append(line);
                } else {
                    if (sb.length() != 0)
                        sb.append("\n" + "    " + line);
                }
            }
            return map;
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException " + e.toString());
        } catch (IOException e) {
            System.out.println("IOException" + e.toString());
        }
        return Collections.emptyMap();
    }

}
