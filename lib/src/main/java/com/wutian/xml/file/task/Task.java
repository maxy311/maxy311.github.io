package com.wutian.xml.file.task;

/**
 * Created by maxy on 17/07/2017.
 */

public abstract  class Task implements Runnable {
    protected  boolean mIsDone;

    public abstract boolean isDone();
}
