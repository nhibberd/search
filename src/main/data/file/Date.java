package main.data.file;

public class Date {
    public long mtime;
    public long ctime;
    public long atime;

    public Date(long mtime, long ctime, long atime) {
        this.mtime = mtime;
        this.ctime = ctime;
        this.atime = atime;
    }

    @Override
    public String toString() {
        return "Date{" +
                "mtime=" + mtime +
                ", ctime=" + ctime +
                ", atime=" + atime +
                '}';
    }

    public boolean compare (String type, long time){
        if (type.equals("mtime"))
            return (this.mtime==time);
        else if (type.equals("ctime"))
            return (this.ctime==time);
        else if (type.equals("atime"))
            return (this.atime==time);
        return false;
    }

}
