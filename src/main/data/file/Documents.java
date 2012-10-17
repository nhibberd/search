package main.data.file;

import java.util.ArrayList;
import java.util.List;

public class Documents {

    public String name;
    public String ext;
    public Date times;
    public String url;
    public List<Documents> children = new ArrayList<Documents>();

    public Documents(String name, String ext, Date times, String url, List<Documents> children) {
        this.name = name;
        this.ext = ext;
        this.times = times;
        this.url = url;
        this.children = children;
    }

    public Documents(String name, String ext, Date times, String url) {
        this.name = name;
        this.ext = ext;
        this.times = times;
        this.url = url;
    }
    
    public Boolean hasChildren(){
        return (this.children.size()>0);
    }
    
}



