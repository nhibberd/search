package main.data.file;

public class State {

    public String url;
    public Integer mtime;
    public Integer atime;
    public Integer group;
    public Integer owner;
    public Integer permissions;


    public State(String url, Integer mtime, Integer atime, Integer group, Integer owner, Integer permissions) {
        this.url = url;
        this.mtime = mtime;
        this.atime = atime;
        this.group = group;
        this.owner = owner;
        this.permissions = permissions;
    }

    public State(String url) {
        this.url = url;
        this.mtime = 0;
        this.atime = 0;
        this.group = 0;
        this.owner = 0;
        this.permissions = 0;
    }
}
