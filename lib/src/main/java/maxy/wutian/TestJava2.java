package maxy.wutian;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import maxy.wutian.utils.StringReader;

public class TestJava2 {
    public static void main(String[] args) {
//        String path = "/Users/maxiaoyu/Android/workspace/SHAREit/App/src/main/res/values/photo_string.xml";
//        StringReader.readFileAllString(new File(path));
        test();
    }

    private static void test() {
        String trimLineStr = "<string name=\"localcommon_entry_name_safebox\">Széf</string>";
        trimLineStr = trimLineStr.replaceAll("\n", "aaaaaa");
        String tagName = "string";
        String valuePattern = "((\\\"| )>(\n|(.*))+</" + tagName + ">)";
        System.out.println(valuePattern);
        Pattern compile = Pattern.compile(valuePattern, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher matcher = compile.matcher(trimLineStr);
        String value;
        if (matcher.find()) {
            value = matcher.group();
            value = value.substring(value.indexOf("\">") + 2, value.lastIndexOf("<"));
            value = value.replaceAll("aaaaaa", "\n");
            System.out.println(value);
        } else
            throw new RuntimeException("cannot parse value : " + trimLineStr);
    }
}
