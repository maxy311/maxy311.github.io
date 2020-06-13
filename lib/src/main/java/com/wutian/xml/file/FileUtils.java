package com.wutian.xml.file;

import com.wutian.utils.Utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FileUtils {
    public static void fileChannelCopy(File src,File dst) {
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            inChannel = new FileInputStream(src).getChannel();
            outChannel = new FileOutputStream(dst).getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (IOException e) {
        } finally {
            Utils.close(inChannel);
            Utils.close(outChannel);
        }
    }

    public static void copyFile(String originPath, String targetPath) {
        copyFile(new File(originPath), new File(targetPath));
    }

    public static void copyFile(File originFile, File targetFile) {
        BufferedReader reader = null;
        BufferedWriter writer = null;

        if (originFile == null || !originFile.exists() || targetFile == null)
            return;

        try {
            if (!targetFile.exists())
                targetFile.createNewFile();
        } catch (IOException e) {
            targetFile = null;
        }

        if (targetFile == null || !targetFile.exists())
            return;

        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(originFile)));
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile)));

            String line;
            while (true) {
                line = reader.readLine();
                if (line == null)
                    break;
                writer.write(line);
                writer.flush();
                writer.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Utils.close(reader);
            Utils.close(writer);
        }
    }

    public static List<String> readXmlToList(String path) {
        return readXmlToList(new File(path));
    }

    public static List<String> readXmlToList(File file) {
        ArrayList<String> lines = new ArrayList<>();
        if (file == null || !file.exists())
            return lines;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line = "";
            StringBuffer sb = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                if (line.contains("plurals") || line.contains("<item quantity")) {
                    if (line.contains("plurals")) {
                        if (line.trim().startsWith("<plurals"))
                            sb.append(line);
                        else if (line.trim().startsWith("</plurals>")) {
                            sb.append("\n" + line);
                            lines.add(sb.toString());
                            sb.setLength(0);
                        }
                    } else if (line.contains("<item quantity")) {
                        sb.append("\n" + line);
                    }
                } else if (line.contains("-array") || (line.contains("<item>") && line.contains("</item>"))) {
                    if (line.trim().startsWith("<string-array") || line.trim().startsWith("<integer-array"))
                        sb.append(line);
                    else if (line.trim().startsWith("</string-array>") || line.trim().startsWith("</integer-array>")) {
                        sb.append("\n" + line);
                        lines.add(sb.toString());
                        sb.setLength(0);
                    } else if ((line.contains("<item>") && line.contains("</item>"))) {
                        sb.append("\n" + line);
                    }
                } else
                    lines.add(line);
            }
        } catch (FileNotFoundException e) {
            return lines;
        } catch (IOException e) {
            return lines;
        } finally {
            Utils.close(reader);
        }
        return lines;
    }


    // read string to a Map
    public static Map<String, String> readStringToMap(String filePath) {
        return readStringToMap(new File(filePath));
    }

    public static Map<String, String> readStringToMap(File file) {
        Map<String, String> map = new HashMap<>();
        return readStringToMap(file, map);
    }

    public static Map<String, String> readStringToLinkedHasMap(File file) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        return readStringToMap(file, map);
    }

    public static Map<String, String> readStringToMap(File file, Map<String, String> map) {
        if (file == null || !file.exists())
            return map;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            StringBuffer sb = new StringBuffer();
            String line;
            String str = "";
            while ((line = reader.readLine()) != null) {
                if (line.contains("<resources>") || line.contains("</resources>") || line.contains("<?xml") || line.endsWith("-->"))
                    continue;

                line = line.trim();
                if (line.startsWith("  "))
                    line = line.replace("  ", "");
                line = line.trim();
                if (!(line.startsWith("<string") || line.contains("plurals") || line.contains("<item") || line.contains("-array")))
                    continue;

                if (line.contains("plurals") || line.contains("<item quantity")) {
                    if (line.contains("plurals")) {
                        if (line.startsWith("<plurals")) {
                            str = line.split("\">")[0];
                            sb.append(line);
                        } else if (line.startsWith("</plurals>")) {
                            sb.append("\n" + "    " + line);
                            map.put(str, sb.toString());
                            str = "";
                            sb.setLength(0);
                        }
                    } else if (line.contains("<item quantity")) {
                        sb.append("\n" + "        " + line);
                    }
                } else if ((line.contains("-array") || line.contains("-array")) || (line.startsWith("<item>") && line.endsWith("</item>"))) {
                    if ((line.contains("<string-array") || line.contains("<integer-array"))) {
                        str = line.split("\">")[0];
                        sb.append(line);
                    } else if (line.startsWith("<item>") && line.endsWith("</item>")) {
                        if (str == "" || str.equals(""))
                            continue;
                        sb.append("\n" + "        " + line);
                    } else if (line.startsWith("</string-array>") || line.startsWith("</integer-array>")) {
                        sb.append("\n" + "    " + line);
                        map.put(str, sb.toString());
                        str = "";
                        sb.setLength(0);
                    }
                } else {
                    String strs[] = line.split("\">");
                    if (strs.length != 2)
                        continue;

                    map.put(strs[0], line);
                }

            }
            return map;
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        } finally {
            Utils.close(reader);
        }

    }

    public static void deleteFile(File file) {
        if (file.isDirectory()) {
            for (File listFile : file.listFiles()) {
                deleteFile(listFile);
            }

            if (file.listFiles().length == 0)
                file.delete();
        } else {
            file.delete();
        }
    }
}
