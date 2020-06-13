package maxy.wutian;

import java.io.File;

import maxy.wutian.get.GetTranslateHelper;

public class TestJava {
    private static final String PROJECT_PATH = "/Users/maxiaoyu/Android/workspace/SHAREit";
    private static final String DESKTOP_PATH = "/Users/maxiaoyu/Desktop";
    public static void main(String[] args) {
        File file = new File(PROJECT_PATH);
        File deskFile = new File(DESKTOP_PATH);
        GetTranslateHelper getSHAREitTranslate = new GetTranslateHelper(file, deskFile, null, null);
        getSHAREitTranslate.startGetTranslate();
    }


    private static void testGetTranslate() {
        File file = new File(PROJECT_PATH);
        File deskFile = new File(DESKTOP_PATH);
        GetSHAREitTranslate getSHAREitTranslate = new GetSHAREitTranslate(file, deskFile, null, null);
        getSHAREitTranslate.startGetTranslate();
    }
}
