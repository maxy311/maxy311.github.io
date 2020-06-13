package com.wutian.xml.file.task;

import java.io.File;

public class AddTranslateTask extends Task {

    private File mTranslateFile;
    private File mOriginFile;
    private AddTranslateListener mAddTranslateListener;

    public AddTranslateTask (AddTranslateListener listener, File translateFile, File originFile){
        mAddTranslateListener = listener;
        mTranslateFile = translateFile;
        mOriginFile = originFile;
    }

    @Override
    public void run() {
        if (mAddTranslateListener != null)
            mAddTranslateListener.doAddTranslate(mTranslateFile, mOriginFile);

        mIsDone = true;
    }

    @Override
    public boolean isDone() {
        return mIsDone;
    }

    public interface AddTranslateListener {
        void doAddTranslate(File translateFile, File originFile);
    }
}
