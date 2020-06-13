package maxy.wutian.get;

import com.Constants;
import com.wutian3.utils.ResFileFilter;
import com.wutian3.utils.ShellUtils;
import com.wutian3.utils.TranslateFilter;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import maxy.wutian.bean.CompareBean;
import maxy.wutian.bean.FileStrEntity;
import maxy.wutian.bean.ModuleBean;

public class GetTranslateHelper {
    public static final String WRITE_FIELNMAE_SPLIT = "    //-----------------------------";

    private File outFile;
    private File projectFile;
    private String projectName;
    private String basePath;
    private String lastTag;
    private String compareDir;

    public GetTranslateHelper(File projectFile, File outFile, String lastTag, String compareDir) {
        this.outFile = outFile;
        this.projectFile = projectFile;
        this.projectName = projectFile.getName();
        this.basePath = projectFile.getAbsolutePath();

        this.lastTag = lastTag;
        this.compareDir = compareDir;
    }

    public void startGetTranslate() {
        //1. delete origin shareit file;
        checkOutputFile();

        //read values file to map
        CompareBean valueCompareBean = new CompareBean("values");
        pareString(valueCompareBean, projectFile, valueCompareBean.getValueDir());

        CompareBean xxValueCompareBean = new CompareBean(!isEmptyStr(compareDir) ? compareDir : "values-ar");
        pareString(xxValueCompareBean, projectFile, xxValueCompareBean.getValueDir());

        //read last tag values file to map
        CompareBean preValueCompareBean  = new CompareBean("values");
        if (!isEmptyStr(lastTag)) {
            ShellUtils.checkoutToTag(lastTag);
            System.out.println("has checkout to:" + lastTag);
            pareString(preValueCompareBean, projectFile, xxValueCompareBean.getValueDir());
            ShellUtils.checkoutToTag("master");
        }

        PrintTranslateUtils printTranslateUtils = new PrintTranslateUtils(outFile, valueCompareBean, xxValueCompareBean, preValueCompareBean);
        printTranslateUtils.outputTranslateFile();
    }

    private void pareString(CompareBean compareBean, File file, String valueDir) {
        if (file.isDirectory()) {
            for (File listFile : file.listFiles(new ResFileFilter())) {
                pareString(compareBean, listFile, valueDir);
            }
        } else {
            if (!TranslateFilter.isStringsFile(file))
                return;

            if (!isValuesFile(file, valueDir))
                return;
            String fileModule = getFileModule(file);
            ModuleBean moduleBean = compareBean.getModuleBean(fileModule);
            moduleBean.parseFile(file);
        }
    }

    private String getFileModule(File file) {
        String parent = file.getParent();
        String module = parent.replace(Constants.SHAREit_PATH, "");
        module = module.replace("/", "_");
        if (module.contains("src"))
            return module.substring(1, module.indexOf("src") - 1);
        else
            return module.substring(1, module.indexOf("res") - 1);
    }

    private boolean isValuesFile(File file, String valueDir) {
        if (valueDir == null)
            System.out.println("isValuesFile   " + file.getAbsolutePath() + "         " + valueDir);
        return valueDir.equals(file.getParentFile().getName());
    }

    private void checkOutputFile() {
        if (projectFile == null || !projectFile.exists() || outFile == null)
            throw new RuntimeException("GetTranslateHelper projectFile not exist :" + projectFile + "     " + outFile);

        if (!outFile.getName().equals("Translate"))
            outFile = new File(outFile, projectName+ "_Translate");

        if (!outFile.exists())
            outFile.mkdir();

        removeAllSubFiles(outFile);
    }

    private void removeAllSubFiles(File desktopShareitFile) {
        if (desktopShareitFile.isDirectory()) {
            for (File listFile : desktopShareitFile.listFiles()) {
                removeAllSubFiles(listFile);
            }
        } else {
            desktopShareitFile.delete();
        }
    }

    private boolean isEmptyStr(String str) {
        return str == null || str.trim().length() == 0;
    }

}
