package com.wutian3.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileHashUtils {
    private static final int COMPARE_BYTE = 1000;

    public static String getHashCode(File file) {
        String name = file.getName();
        try (FileInputStream inputStream = new FileInputStream(file)) {
            int length = inputStream.available();
            if (length <= COMPARE_BYTE * 3) {
                byte[] buffer = new byte[length];

                return name + ":" + length + "_" + createHashCode(buffer);
            } else {
                byte[] buffer = new byte[COMPARE_BYTE];
                //文件开头的200 字节
                String hashCode = name + ":" + length + "_";
                inputStream.read(buffer);
                hashCode += createHashCode(buffer);

                //文件中间的200 字节
                inputStream.skip((long) (length / 2 - COMPARE_BYTE * 1.5));
                inputStream.read(buffer);
                hashCode += createHashCode(buffer);

                //文件最后的200字节
                inputStream.skip(length - COMPARE_BYTE * 2);
                inputStream.read(buffer);
                hashCode += createHashCode(buffer);

                return hashCode;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static int createHashCode(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (byte aByte : bytes) {
            sb.append(aByte);
        }
        return sb.toString().hashCode();
    }
}
