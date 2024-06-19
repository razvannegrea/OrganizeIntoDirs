package org.dirs;

import java.io.File;
import java.io.FileFilter;

public class JpegFileFilter implements FileFilter {
    @Override
    public boolean accept(File filename) {
        return filename.getName().endsWith(".jpg");
    }
}
