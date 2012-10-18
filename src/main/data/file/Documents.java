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
    public String hash;


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

    public Documents(Integer id, String name, String ext, Date times, String url, Integer links, boolean regfile, boolean other, boolean hidden, String group, String owner, Integer permissions, String hash) {
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
        this.hash = hash;
    }


    public boolean compare(Documents documents) {
        if (hidden != documents.hidden) return false;
        if (other != documents.other) return false;
        if (regfile != documents.regfile) return false;
        if (ext != null ? !ext.equals(documents.ext) : documents.ext != null) return false;
        if (group != null ? !group.equals(documents.group) : documents.group != null) return false;
        if (links != null ? !links.equals(documents.links) : documents.links != null) return false;
        if (name != null ? !name.equals(documents.name) : documents.name != null) return false;
        if (owner != null ? !owner.equals(documents.owner) : documents.owner != null) return false;
        if (permissions != null ? !permissions.equals(documents.permissions) : documents.permissions != null)
            return false;
        if (times.atime != 0 ? times.atime != documents.times.atime : documents.times.atime != 0) return false;
        if (times.mtime != 0 ? times.mtime != documents.times.mtime : documents.times.mtime != 0) return false;
        if (times.ctime != 0 ? times.ctime != documents.times.ctime : documents.times.ctime != 0) return false;
        if (url != null ? !url.equals(documents.url) : documents.url != null) return false;

        return true;
    }

}



