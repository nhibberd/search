package main.search;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class FileSystem {
    private void list(File f) {
        try{
            List<String> list = new ArrayList<String>();
            for (File q : f.listFiles()){
                if (q.isDirectory())
                    list(q);
                else if (q.isFile()) {
                        list.add("z");
                    
                }
            }

        } catch (NullPointerException e){
            e.printStackTrace();
            //throw new ServerException(e);      
        }
    }
    
    
}
