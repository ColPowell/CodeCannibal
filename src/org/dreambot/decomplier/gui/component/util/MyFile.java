package org.dreambot.decomplier.gui.component.util;

import java.io.File;

public class MyFile {
    private final File mFile;

    public MyFile(final File pFile) {
        mFile = pFile;
    }

    public boolean isDirectory() {
        return mFile.isDirectory();
    }

    public MyFile[] listFiles() {
        final File[] files = mFile.listFiles();
        if (files == null) return null;
        if (files.length < 1) return new MyFile[0];

        final MyFile[] ret = new MyFile[files.length];
        for (int i = 0; i < ret.length; i++) {
            final File f = files[i];
            ret[i] = new MyFile(f);
        }
        return ret;
    }

    public File getFile() {
        return mFile;
    }

    @Override public String toString() {
        return mFile.getName();
    }
}