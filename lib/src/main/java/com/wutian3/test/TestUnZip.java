package com.wutian3.test;

import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class TestUnZip {
    private static final int BUFFER_SIZE = 1024 * 4;

    private static String PATH = "/Users/maxy/Android/workspace/XmlUtilsAs/app/src/main/assets/JS";

    public static void main(String[] args) {
        File jsDir = new File(PATH);
        for (File file : jsDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.getName().endsWith(".zip"))
                    return false;
                return true;
            }
        })) {
            file.delete();
        }
        System.out.println("start UnZip :: " + jsDir.listFiles().length);
        unzip(PATH + "/assert_js_resource.zip", jsDir.getAbsolutePath());

        System.out.println("after UnZip :: " + jsDir.listFiles().length);
    }

    public static void unzip(String zipFilePath, String folderPath) {
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(zipFilePath);
            @SuppressWarnings("rawtypes")
            Enumeration en = zipFile.entries();
            while (en.hasMoreElements()) {
                ZipEntry zipEntry = (ZipEntry)en.nextElement();
                String fileName = zipEntry.getName();
                if (zipEntry.isDirectory()) {
                    fileName = fileName.substring(0, fileName.length() - 1);
                    new File(folderPath + File.separator + fileName).mkdirs();
                } else {
                    File file = new File(folderPath + File.separator + fileName);
                    if (!file.getParentFile().exists())
                        file.getParentFile().mkdirs();
                    file.createNewFile();
                    FileOutputStream fos = new FileOutputStream(file);
                    int length;
                    byte[] buffer = new byte[BUFFER_SIZE];
                    InputStream zis = zipFile.getInputStream(zipEntry);
                    if (zis != null) {
                        while ((length = zis.read(buffer)) != -1) {
                            // write (len) byte from buffer at the position 0
                            fos.write(buffer, 0, length);
                            fos.flush();
                        }
                        close(zis);
                    }
                    close(fos);
                }
            }
        } catch (Exception e) {
        } finally {
            close(zipFile);
        }
    }

    public static void close(Closeable object) {
        if (object != null) {
            try {
                object.close();
            } catch (Exception e) {}
        }
    }
}
