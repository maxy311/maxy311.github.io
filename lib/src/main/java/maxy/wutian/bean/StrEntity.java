package maxy.wutian.bean;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrEntity {
    private String lineStr;
    private String tagName;
    private String stringKey;
    private String value;
    private boolean isNeedTranslate;

    public StrEntity(String lineStr) {
        this.lineStr = lineStr;
        String trimLineStr = lineStr.trim();
        tagName = trimLineStr.substring(1, trimLineStr.indexOf("name") - 1);
        parseStringTagLine(trimLineStr);
        isNeedTranslate = checkNeedTranslate(trimLineStr);
    }

    public String getTagName() {
        return tagName;
    }

    public String getStringKey() {
        return stringKey;
    }

    public String getValue() {
        return value;
    }

    public boolean isNeedTranslate() {
        return isNeedTranslate;
    }

    private boolean checkNeedTranslate(String trimLineStr) {
        if (value.contains("@string/"))
            return false;

        if (stringKey.equals("app_name"))
            return false;

        return !trimLineStr.contains("translatable=\"false\"") && !trimLineStr.contains("translate=\"false\"");
    }

    public String getLineText() {
        return lineStr;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        StrEntity strEntity = (StrEntity) o;
        return stringKey.equals(strEntity.stringKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineStr, tagName, stringKey, value, isNeedTranslate);
    }

    @Override
    public String toString() {
        return getLineText();
    }


    private void parseStringTagLine(String trimLineStr) {
        String keyPattern = "<" + tagName + " name=\".*?\\\"";
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(keyPattern);
        Matcher m = r.matcher(trimLineStr);
        if (m.find()) {
            stringKey = m.group();
            stringKey = stringKey.substring(stringKey.indexOf("\"") + 1, stringKey.lastIndexOf("\""));
        } else
            throw new RuntimeException("cannot parse key : " + lineStr);


        String valuePattern = "((\\\"| )>(\n|(.*))+</" + tagName + ">)";
        Pattern compile = Pattern.compile(valuePattern);
        Matcher matcher = compile.matcher(trimLineStr);
        if (matcher.find()) {
            value = matcher.group();
            value = value.substring(value.indexOf(">") + 1, value.lastIndexOf("<"));
        } else
            throw new RuntimeException("cannot parse value : " + lineStr);
    }
}
