package maxy.wutian.bean;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleBean {
    private String moduleName;
    private String resValuesDir;  // values , values-ar ,

    private List<FileStrEntity> fileStrEntityList = new ArrayList<>();
    private Map<String, FileStrEntity> fileStrEntityMap = new HashMap<>();
    public ModuleBean(String moduleName, String resValuesDir) {
        this.moduleName = moduleName;
        this.resValuesDir = resValuesDir;
    }

    public void parseFile(File file) {
        System.out.println("---------- " + file.getAbsolutePath());
        FileStrEntity fileStrEntity = new FileStrEntity(file, moduleName);
        System.out.println(file.getAbsolutePath());
        fileStrEntityList.add(fileStrEntity);
        fileStrEntityMap.put(file.getName(), fileStrEntity);
    }

    public List<FileStrEntity> getFileStrEntityList() {
        return fileStrEntityList;
    }

    public Map<String, FileStrEntity> getFileStrEntityMap() {
        return fileStrEntityMap;
    }

    public FileStrEntity getFileStrEntity(String fileName) {
        return fileStrEntityMap.get(fileName);
    }
}
