package main.data.file;

import java.nio.file.attribute.FileTime;

public class Date {
    public long mtime;
    public long ctime;
    public long atime;

    public Date(long mtime, long ctime, long atime) {
        this.mtime = mtime;
        this.ctime = ctime;
        this.atime = atime;
    }
}
