package com.wutian.xml.file.task;

import com.wutian.xml.file.TranslateHelper;

import java.io.File;

public class TranslateTask extends Task {
    private File mValueFile;
    private File mOriginFile;
    private File mTargetFile;

    public TranslateTask(File valueFile, File originFile, File targetFile) {
        mValueFile = valueFile;
        mOriginFile = originFile;
        mTargetFile = targetFile;
    }

    @Override
    public void run() {
        TranslateHelper.addTransValuesToRes(mValueFile, mOriginFile, mTargetFile);
        mIsDone = true;
    }

    @Override
    public boolean isDone() {
        return mIsDone;
    }
}
