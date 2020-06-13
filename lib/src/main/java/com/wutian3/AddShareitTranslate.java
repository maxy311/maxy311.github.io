package com.wutian3;

import com.Constants;
import com.wutian.xml.file.FileUtils;
import com.wutian2.operate.AddTransLateHelper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AddShareitTranslate {
    private static String TRANSLATE_DIR = "SHAREit_Translate";
    private static final String SHAREIT_TRANSLATE_PATH = Constants.DESKTOP_PATH + File.separator + "SHAREit";
    public static void main(String[] args) {
        AddShareitTranslate addShareitTranslate = new AddShareitTranslate();
        addShareitTranslate.start();
    }

    private void start() {
        File translateFile = getShareitTranslateDir();
        FileUtils.deleteFile(translateFile);

        File file = new File(Constants.DESKTOP_PATH + "/" + TRANSLATE_DIR);
        checkTranslateDir(file);
        //0. add file name tag
        addFileNameTag(file);

        // 1. split file
        slipFile(file);

        // 2. add Translate
        for (File listFile : translateFile.listFiles()) {
            addTranslate(listFile);
        }
    }

    private void addFileNameTag(File file) {
        Map<String, List<String>> valuesMap = readValuesToMap(file);
        for (File listFile : file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isHidden())
                    return false;
                if (file.getName().equals("values"))
                    return false;
                return true;
            }
        })) {
            for (File valueFile : listFile.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    if (file.isHidden())
                        return false;
                    String fileName = file.getName();
                    if (!fileName.endsWith("xml"))
                        return false;
                    if (fileName.toLowerCase().contains("releasenote"))
                        return false;
                    if (fileName.toLowerCase().contains("release"))
                        return false;
                    return true;
                }
            })) {
                List<String> stringList = valuesMap.get(valueFile.getName());
                Map<String, String> stringMap = FileUtils.readStringToMap(valueFile);
                Set<String> keySet = stringMap.keySet();
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(valueFile))) {
                    for (String str : stringList) {
                        String key = str.trim().split("\">")[0];
                        if (keySet.contains(key)) {
                            str = "    " + stringMap.get(key);
                        }
                        bw.write(str);
                        bw.newLine();
                        bw.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Map<String, List<String>> readValuesToMap(File file) {
        File valuesDir = new File(file, "values");
        if (!valuesDir.exists()) {
            throw new RuntimeException("values path error:" + valuesDir.getAbsolutePath());
        }
        Map<String, List<String>> valuesMap = new HashMap<>();
        for (File listFile : valuesDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isHidden())
                    return false;
                return true;
            }
        })) {
            valuesMap.put(listFile.getName(), FileUtils.readXmlToList(listFile));
        }
        return valuesMap;
    }

    private void addTranslate(File file) {
        if (!file.isDirectory()) {
            System.out.println("addTranslate Error :: " + file.getName());
            return;
        }

        boolean childIsValues = false;
        File[] childFiles = file.listFiles();
        for (File listFile : childFiles) {
            childIsValues = listFile.getName().startsWith("values");
            if (childIsValues)
                break;
        }

        if (!childIsValues) {
            for (File childFile : childFiles) {
                addTranslate(childFile);
            }
            return;
        }

        File moduleResFile = getShareitModuleResFile(file);
        AddTransLateHelper addTransLateHelper = new AddTransLateHelper();
        addTransLateHelper.addTranslate(file, moduleResFile);
    }

    private File getShareitModuleResFile(File file) {
        String filePath = file.getAbsolutePath();
        String modulePath = filePath.replace(SHAREIT_TRANSLATE_PATH, "");
        File moduleFile = new File(Constants.SHAREit_PATH, modulePath);

        File resFile = new File(moduleFile, "src/main/res");
        if (!resFile.exists())
            resFile = new File(moduleFile, "res");
        return resFile;
    }

    private void slipFile(File file) {
        for (File valueDir : file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.getName().startsWith("values"))
                    return true;
                return false;
            }
        })) {
            for (File xmlFile : valueDir.listFiles()) {
                if (isTransLateFile(xmlFile)) {
                    splitFileToValueXX(xmlFile);
                } else {
                    copyToReleaseNote(xmlFile);
                }
            }
        }
    }

    private void splitFileToValueXX(File xmlFile) {
        // create vaules-XX Dir
        File shareitFile = getShareitTranslateDir();
        String name = xmlFile.getName();
        String valueXXName = xmlFile.getParentFile().getName();
        String replaceName = name.replace("_strings.xml", "");
        replaceName = replaceName.replace("_", "/");
        File valueXXDir = new File(shareitFile, replaceName + File.separator + valueXXName);
        if (!valueXXDir.exists())
            valueXXDir.mkdirs();

        // out xmlFile to real file;
        splitXmlFile(valueXXDir, xmlFile);
    }

    private void splitXmlFile(File valueXXDir, File xmlFile) {
        BufferedWriter bw = null;
        try (BufferedReader br = new BufferedReader(new FileReader(xmlFile))) {
            StringBuffer sb = new StringBuffer();
            String line;
            String str = "";
            while ((line = br.readLine()) != null) {
                if (line.contains("<resources>") || line.contains("</resources>") || line.contains("<?xml"))
                    continue;
                if (line.startsWith(GetSHAREitTranslate2.WRITE_FIELNMAE_SPLIT)) {
                    if (bw != null) {
                        writeLine(bw, "</resources>");
                        bw.close();
                    }

                    String fileName = line.replace(GetSHAREitTranslate2.WRITE_FIELNMAE_SPLIT, "").trim();
                    bw = resetOutFileWriter(valueXXDir, fileName);
                    bw.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" + "<resources>\n");
                } else {
                    if (bw == null) {
                        System.out.println("ERROR ::: " + line);
                        continue;
                    }

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
                                writeLine(bw, "    " + sb.toString());
                                str = "";
                                sb.setLength(0);
                            }
                        } else if (line.contains("<item quantity")) {
                            sb.append("\n" + "        " + line);
                        }
                    } else if ((line.contains("-array") || line.contains("-array")) || (line.startsWith("<item>") && line.endsWith("</item>"))) {
                        if ((line.contains("<string-array") || line.contains("<integer-array"))) {
                            str = line;
                            sb.append(line);
                        } else if (line.startsWith("<item>") && line.endsWith("</item>")) {
                            if (str == "" || str.equals(""))
                                continue;
                            sb.append("\n" + "        " + line);
                        } else if (line.startsWith("</string-array>") || line.startsWith("</integer-array>")) {
                            sb.append("\n" + "    " + line);
                            writeLine(bw, "    " + sb.toString());
                            str = "";
                            sb.setLength(0);
                        }
                    } else {
                        writeLine(bw, "    " + line);
                    }
                }
            }
            if (bw != null)
                writeLine(bw, "</resources>");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null)
                    bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void writeLine(BufferedWriter bw, String line) throws IOException {
        bw.write(line + "\n");
        bw.flush();
    }

    private BufferedWriter resetOutFileWriter(File valueXXDir, String fileName) {
        BufferedWriter bw = null;
        try {

            File file = new File(valueXXDir, fileName);
            if (!file.exists())
                file.createNewFile();

            bw = new BufferedWriter(new FileWriter(file));
        } catch (IOException e) {
        }
        return bw;
    }

    private void checkTranslateDir(File translateDir) {
        if (!translateDir.exists())
            throw new RuntimeException(TRANSLATE_DIR + " not exists!");

        File valueDir = new File(translateDir, "values");
        if (!valueDir.exists())
            throw new RuntimeException("value dir not exists::: " + valueDir);
    }

    private boolean isTransLateFile(File file) {
        if (file.isHidden())
            return false;

        if (file.isDirectory())
            return false;
        String fileName = file.getName();

        if (fileName.contains("push"))
            return false;
        if (fileName.toLowerCase().contains("releasenote"))
            return false;
        if (fileName.toLowerCase().contains("release"))
            return false;

        if (!fileName.endsWith(".xml")) {
            return false;
        }
        return true;
    }

    private void copyToReleaseNote(File releaseNoteFile) {
        File releaseNoteDir = new File(Constants.DESKTOP_PATH + File.separator + "ReleaseNote");
        if (!releaseNoteDir.exists())
            releaseNoteDir.mkdir();
        String valueXXName = releaseNoteFile.getParentFile().getName();
        File valueXXFile = new File(releaseNoteDir, valueXXName);
        if (!valueXXFile.exists())
            valueXXFile.mkdir();

        File file = new File(valueXXFile, releaseNoteFile.getName());
        try {
            if (!file.exists())
                file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileUtils.fileChannelCopy(releaseNoteFile, file);
    }

    public static  File getShareitTranslateDir() {
        File shareitFile = new File(SHAREIT_TRANSLATE_PATH);
        if (shareitFile.exists())
            shareitFile.mkdir();
        return shareitFile;
    }

}
