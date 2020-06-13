package com.wutian.xml.file.task;

import java.io.File;

public class CompleteCompareTask extends Task {
    public File mValueFile;
    public File mValueArFile;
    public File mOldValueFile;
    public File mSaveDir;
    private CompareTaskListener mCompareTaskListener;
    public CompleteCompareTask(CompareTaskListener compareTaskListener, File valueFile, File valueArFile, File oldValueFile, File saveDir) {
        mCompareTaskListener = compareTaskListener;
        mValueFile = valueFile;
        mValueArFile = valueArFile;
        mOldValueFile = oldValueFile;
        mSaveDir = saveDir;
    }

    @Override
    public boolean isDone() {
        return mIsDone;
    }

    @Override
    public void run() {
        toGetTransLate();
        mIsDone = true;
    }

    private void toGetTransLate() {
        if (mCompareTaskListener != null)
            mCompareTaskListener.startGetTranslate(mValueFile, mValueArFile, mOldValueFile, mSaveDir);
    }

    public interface CompareTaskListener {
        void startGetTranslate(File valueFile, File valueArFile, File oldValueFile, File saveDir);
    }
}
