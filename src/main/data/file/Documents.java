package main.data.file;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Documents implements Comparable<Documents> {
    // DocumentsDS [  {} {} {} {name, type, url} ]

    public String name;
    public FileType type;
    public String url;
    public Date date;
    public List<Documents> children = new ArrayList<Documents>();


    public Documents(String name, FileType type, Date date, String url) {
        this.name = name;
        this.type = type;
        this.date = date;
        this.url = url;
    }

    @Override
    public String toString() {
        return "Documents{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", url='" + url + '\'' +
                ", date=" + date +
                '}';
    }

    public boolean equals(Object o) {
        if (!(o instanceof Documents))
           return false;
        Documents d = (Documents) o;
        if (!(name.equals(d.name) && type.equals(d.type) && url.equals(d.url) && date.equals(d.date)))
            return false;
        Documents[] c1 = children.toArray(new Documents[children.size()]);
        Documents[] c2 = d.children.toArray(new Documents[children.size()]);
        Arrays.sort(c1);
        Arrays.sort(c2);
        return Arrays.equals(c1, c2);
    }

    public int compareTo(Documents o) {
        if (equals(o))
            return 0;
        if (!name.equals(o.name))
            return name.compareTo(o.name);
        if (!type.equals(o.type))
            return type.compareTo(o.type);
        if (!url.equals(o.url))
            return url.compareTo(o.url);
        if (!date.equals(o.date))
            return date.compareTo(o.date);
        Documents[] c1 = children.toArray(new Documents[children.size()]);
        Documents[] c2 = o.children.toArray(new Documents[children.size()]);
        Arrays.sort(c1);
        Arrays.sort(c2);
        List<Documents> l1 = Arrays.asList(c1);
        List<Documents> l2 = Arrays.asList(c2);
        //return l1.comp;
        return 0;
    }
}

