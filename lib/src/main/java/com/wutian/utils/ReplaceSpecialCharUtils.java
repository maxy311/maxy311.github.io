package com.wutian.utils;

public class ReplaceSpecialCharUtils {

    public static String replaceSpecialChar(String originLine, String line) {
        line = checkAndReplaceSpecialChar(line, "\u2028", " ","2028"); //行分隔符	行结束符
        line = checkAndReplaceSpecialChar(line, "\u2029", " ", "2029"); //段落分隔符	行结束符
        line = checkAndReplaceSpecialChar(line, "\uFEFF", " ", "FEFF");//字节顺序标记	空白
        line = checkAndReplaceSpecialChar(line, "\u00A0", " ", "00A0");
//                line = checkAndReplaceSpecialChar(line, "%", "%");
        line = checkAndReplaceSpecialChar2(line); // '-------> \'

        try {
            if (!originLine.equals(line))
                line = replaceSpaceChar(originLine, line);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return line;
    }

    private static String replaceSpaceChar(String originLine, String line) {
        String startChar = "\">";
        String space = startChar;
        while (originLine.contains(space)) {
            space += " ";
        }
        if (!space.equals(startChar)) {
            String lineSpace = startChar;
            while (line.indexOf(lineSpace) != -1) {
                lineSpace += " ";
            }

            if (!lineSpace.equals(startChar)) {
                lineSpace = lineSpace.substring(0, lineSpace.length() - 1);
                space = space.substring(0, space.length() - 1);
                line = line.replace(lineSpace, space);
            }
        }

        startChar = "</string>";
        space = startChar;
        while (originLine.contains(space)) {
            space = " " + space;
        }
        if (!space.equals(startChar)) {
            String lineSpace = startChar;
            while (line.indexOf(lineSpace) != -1) {
                lineSpace = " " + lineSpace;
            }

            if (!lineSpace.equals(startChar)) {
                lineSpace = lineSpace.substring(1, lineSpace.length());
                space = space.substring(1, space.length());
                if (!lineSpace.equals(space)) {
                   line =  line.replace(lineSpace, space);
                }
            }
        }
        return line;
    }

    public static String replaceSpecialChar(String line) {
        line = checkAndReplaceSpecialChar(line, "\u2028", " ","2028"); //行分隔符	行结束符
        line = checkAndReplaceSpecialChar(line, "\u2029", " ", "2029"); //段落分隔符	行结束符
        line = checkAndReplaceSpecialChar(line, "\uFEFF", " ", "FEFF");//字节顺序标记	空白
        line = checkAndReplaceSpecialChar(line, "\u00A0", " ", "00A0");
//                line = checkAndReplaceSpecialChar(line, "%", "%");
        line = checkAndReplaceSpecialChar2(line); // '-------> \'

        return line;
    }

    private static  String checkAndReplaceSpecialChar(String line, String replaceTarget, String replaceStr, String tag) {
        boolean hasChanged = false;
        String originLine = line;
        while (line.indexOf(replaceTarget) != -1) {
            line = line.replace(replaceTarget, replaceStr);
            hasChanged = true;
        }
        if (hasChanged) {
            System.out.println("old    " + originLine);
            System.out.println("new    " + line + "        " + tag);
        }

        return line;
    }

    // '  ------------------>   \'
    private static String checkAndReplaceSpecialChar2(String line) {
        boolean hasChanged = false;
        if (line.indexOf("'") != -1) {
            char[] chars = line.toCharArray();
            char lastChar = '1';
            StringBuffer sb = new StringBuffer();
            for (char aChar : chars) {
                if ('\'' == aChar) {
                    if ('\\' != lastChar) {
                        sb.append("\\");
                        hasChanged = true;
                    }
                }
                sb.append(aChar);
                lastChar = aChar;
            }
            if (hasChanged) {
                line = sb.toString();
                sb.setLength(0);
            }
        }
        return line;
    }
}
