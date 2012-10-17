package main.data.file;

import java.nio.file.attribute.FileTime;

public class Date {
    public FileTime mtime;
    public FileTime ctime;
    public FileTime atime;

    public Date(FileTime mtime, FileTime ctime, FileTime atime) {
        this.mtime = mtime;
        this.ctime = ctime;
        this.atime = atime;
    }
}
