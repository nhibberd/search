package main.data.file;

public class Documents {


    public Integer id;
    public String name;
    public String ext;
    public Date times;
    public String url;
    public Integer links;
    public boolean regfile;
    public boolean other;
    public boolean hidden;
    public String group;
    public String owner;
    public Integer permissions;


    public Documents(String name, String ext, Date times, String url, Integer links, boolean regfile, boolean other, boolean hidden, String group, String owner, Integer permissions) {
        this.name = name;
        this.ext = ext;
        this.times = times;
        this.url = url;
        this.links = links;
        this.regfile = regfile;
        this.other = other;
        this.hidden = hidden;
        this.group = group;
        this.owner = owner;
        this.permissions = permissions;
    }

    public Documents(Integer id, String name, String ext, Date times, String url, Integer links, boolean regfile, boolean other, boolean hidden, String group, String owner, Integer permissions) {
        this.id = id;
        this.name = name;
        this.ext = ext;
        this.times = times;
        this.url = url;
        this.links = links;
        this.regfile = regfile;
        this.other = other;
        this.hidden = hidden;
        this.group = group;
        this.owner = owner;
        this.permissions = permissions;
    }
}



