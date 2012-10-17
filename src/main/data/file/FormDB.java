package main.data.file;

public class FormDB {
    public FormDB(String filepath, String hash, long timestamp, Boolean modified, Boolean nu, String canonical) {
        this.filepath = filepath;
        this.hash = hash;
        this.timestamp = timestamp;
        this.modified = modified;
        this.nu = nu;
        this.canonical = canonical;
    }

    public String filepath;
    public String hash;
    public long timestamp;
    public Boolean modified;
    public Boolean nu;
    public String canonical;

}
