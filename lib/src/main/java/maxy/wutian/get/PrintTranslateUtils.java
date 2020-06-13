package maxy.wutian.get;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import maxy.wutian.bean.CompareBean;
import maxy.wutian.bean.FileStrEntity;
import maxy.wutian.bean.ModuleBean;
import maxy.wutian.bean.StrEntity;

public class PrintTranslateUtils {
    private File outFile;
    private CompareBean valueCompareBean;
    private CompareBean xxValueCompareBean;
    private CompareBean preValueCompareBean;

    public PrintTranslateUtils(File outFile, CompareBean valueCompareBean, CompareBean xxValueCompareBean, CompareBean preValueCompareBean) {
        this.outFile = outFile;
        this.valueCompareBean = valueCompareBean;
        this.xxValueCompareBean = xxValueCompareBean;
        this.preValueCompareBean = preValueCompareBean;
    }

    public void outputTranslateFile() {
        Map<String, ModuleBean> moduleBeanMap = valueCompareBean.getModuleBeanMap();
        if (moduleBeanMap == null || moduleBeanMap.isEmpty())
            return;

        Set<String> moduleNames = moduleBeanMap.keySet();
        for (String moduleName : moduleNames) {
            ModuleBean moduleBean = valueCompareBean.getModuleBean(moduleName);
            ModuleBean xxModuleBean = xxValueCompareBean != null ? xxValueCompareBean.getModuleBean(moduleName) : null;
            ModuleBean preModuleBean = preValueCompareBean != null ? preValueCompareBean.getModuleBean(moduleName) : null;
            outputModuleTranslateFile(moduleBean, xxModuleBean, preModuleBean);
        }
    }

    private void outputModuleTranslateFile(ModuleBean moduleBean, ModuleBean xxModuleBean, ModuleBean preModuleBean) {
        Map<String, FileStrEntity> fileStrEntityList = moduleBean.getFileStrEntityMap();
        if (fileStrEntityList == null || fileStrEntityList.isEmpty())
            return;
        Set<String> fileNameKeys = fileStrEntityList.keySet();
        for (String fileName : fileNameKeys) {
            FileStrEntity fileStrEntity = moduleBean.getFileStrEntity(fileName);
            FileStrEntity xxFileStrEntity = xxModuleBean != null ? xxModuleBean.getFileStrEntity(fileName) : null;
            FileStrEntity preFileStrEntity = preModuleBean != null ? preModuleBean.getFileStrEntity(fileName) : null;
            outputFileEntityFile(fileStrEntity, xxFileStrEntity, preFileStrEntity);
        }
    }
    private void outputFileEntityFile(FileStrEntity fileStrEntity, FileStrEntity xxFileStrEntity, FileStrEntity preFileStrEntity){
        outputStrEntityFile(fileStrEntity.getStrEntityMap(), xxFileStrEntity != null ? xxFileStrEntity.getStrEntityMap() : null , preFileStrEntity != null ? preFileStrEntity.getStrEntityMap() : null);
    }

    private void outputStrEntityFile(Map<String, StrEntity> strEntityMap, Map<String, StrEntity> xxStrEntityMap, Map<String, StrEntity> preStringStrEntityMap) {
        List<String> transList = new ArrayList<>();
        //store keys to find zh values
        List<String> transKeyList = new ArrayList<>();

        Set<String> keySet = strEntityMap.keySet();
        for (String key : keySet) {
            StrEntity strEntity = strEntityMap.get(key);
            if (strEntity == null || !strEntity.isNeedTranslate())
                continue;
            if (xxStrEntityMap == null || !xxStrEntityMap.containsKey(key)) {
                transKeyList.add(strEntity.getStringKey());
                transList.add(strEntity.getLineText());
                continue;
            }


            if (preStringStrEntityMap == null)
                continue;

            StrEntity preStrEntity = preStringStrEntityMap.get(key);
            if (preStrEntity != null && !strEntity.getValue().equals(preStrEntity.getValue())) {
                transKeyList.add(strEntity.getStringKey());
                transList.add(strEntity.getLineText());
                continue;
            }
        }

        if (!transList.isEmpty()) {
            transValueMap.put(fileName, transList);
            transZHValueMap.put(fileName, getZhTranslateData(module, fileName, transList, transKeyList));
        }
    }

}
