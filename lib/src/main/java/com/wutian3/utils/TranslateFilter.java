package com.wutian3.utils;

import java.io.File;
import java.io.FileFilter;


public class TranslateFilter implements FileFilter {
    @Override
    public boolean accept(File file) {
        return isStringsFile(file);
    }

    public static boolean isStringsFile(File file) {
        String name = file.getName();
        if (file.isHidden())
            return false;
        if (!name.contains("string"))
            return false;
        if (name.contains("filter"))
            return false;
        if (name.contains("dimens"))
            return false;
        if (name.contains("account"))
            return false;
        if (name.contains("product_setting"))
            return false;
        if (name.contains("country_code_string"))
            return false;
        return true;
    }
}
