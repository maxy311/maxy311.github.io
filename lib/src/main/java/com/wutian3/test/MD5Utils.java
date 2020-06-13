package com.wutian3.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

public class MD5Utils {
    private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F' };

    //BCB176AF5496CB6C7DC7F228D5B42E60
    //43BCB9EEBE69336B93E7BB8D7A6CD4C4
    private static String PATH = "/Users/maxy/Android/workspace/XmlUtilsAs/app/src/main/assets/JS/assert_js_resource.zip";
    public static void main(String[] args) {
        try {
            InputStream inputStream = new FileInputStream(new File(PATH));
            String md5 = getMD5(inputStream);
            System.out.println("md5 = " + md5);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public static String getMD5(InputStream fis) {
        byte[] buffer = new byte[1024];
        int numRead = 0;
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            while ((numRead = fis.read(buffer)) > 0)
                md5.update(buffer, 0, numRead);
            return toHexString(md5.digest());
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }
}
