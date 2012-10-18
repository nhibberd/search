package main.data.file;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class AllFiles {
    public List<Documents> docs = new ArrayList<Documents>();
    public List<Path> links = new ArrayList<Path>();

    public AllFiles(List<Documents> docs, List<Path> links) {
        this.docs = docs;
        this.links = links;
    }

    private AllFiles copy() {
        List<Documents> d = new ArrayList<Documents>();
        List<Path> l = new ArrayList<Path>();
        d.addAll(this.docs);
        l.addAll(this.links);
        return new AllFiles(d,l);                
    }
    
    public AllFiles add(AllFiles list) {
        AllFiles copy = copy();
        copy.docs.addAll(list.docs);
        copy.links.addAll(list.links);
        return copy;
    }
    
    public AllFiles adddoc(Documents d) {
        AllFiles copy = copy();
        copy.docs.add(d);
        return copy;
    }

    public AllFiles addlink(Path p) {
        AllFiles copy = copy();
        copy.links.add(p);
        return copy;
    }


    public Integer size() {
        return docs.size() + links.size();
        
    }
}
