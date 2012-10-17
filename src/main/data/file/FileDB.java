package main.data.file;

public class FileDB {
    public FileDB(String filepath, String hash, Integer grower, long timestamp, Boolean modified, Boolean nu, String canonical, String ordinal, Boolean mostrecent) {
        this.filepath = filepath;
        this.hash = hash;
        this.grower = grower;
        this.timestamp = timestamp;
        this.modified = modified;
        this.nu = nu;
        this.canonical = canonical;
        this.ordinal = ordinal;
        this.mostrecent = mostrecent;
    }

    public String filepath;
    public String hash;
    public Integer grower;
    public long timestamp;
    public Boolean modified;
    public Boolean nu;
    public String canonical;
    public String ordinal;
    public Boolean mostrecent;

    @Override
    public String toString() {
        return "FileDB{" +
                "filepath='" + filepath + '\'' +
                ", mostrecent=" + mostrecent +
                '}';
    }
}
