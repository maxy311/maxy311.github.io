package maxy.wutian.bean;

import java.util.HashMap;
import java.util.Map;

public class CompareBean {
    private Map<String, ModuleBean> moduleBeanMap = new HashMap<>();

    private String valueDir; // values   values-ar

    public CompareBean(String valueDir) {
        this.valueDir = valueDir;
    }

    public String getValueDir() {
        return valueDir;
    }

    public ModuleBean getModuleBean(String module) {
        if (moduleBeanMap.containsKey(module))
            return moduleBeanMap.get(module);
        ModuleBean moduleBean = new ModuleBean(module, valueDir);
        moduleBeanMap.put(module, moduleBean);
        return moduleBean;
    }


    public Map<String, ModuleBean> getModuleBeanMap() {
        return moduleBeanMap;
    }
}
