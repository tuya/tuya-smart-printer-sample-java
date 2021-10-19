package com.tuya.appsdk.sample.printer;

import java.io.File;

public abstract class AbsFileUploader {

    protected File file;

    protected Callback callback;

    public void setFile(File file) {
        this.file = file;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    abstract boolean uploadStart();

    interface Callback {
        void onCallback(String url);
    }
}
