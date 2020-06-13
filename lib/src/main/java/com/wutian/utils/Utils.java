package com.wutian.utils;


import java.io.Closeable;

public class Utils {
    public static void close(Closeable cursor) {
        try {
            if (cursor != null)
                cursor.close();
        } catch (Exception e){}
    }
}
