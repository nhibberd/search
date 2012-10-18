package main.schedule.jobs;

import main.data.error.ServerException;
import main.data.file.Date;
import main.data.file.Documents;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FileSystem implements Runnable {



    //todo check if file is in db
    //todo if it is, check times, check hash

    //note execuatable updates access time


    //todo check if file has links to it ( or dir )

    public void run() {
        List<Documents> docs = list(new File("/home/dev/nick/"));
        eval(docs);
    }

    private void eval(List<Documents> docs) {
        //todo check if files have changed
        // atime
        // mtime
        // hidden
        // owner
        // group
        // permissions

        // create hash + check db's hash


    }


    private List<Documents> list(File f) {
        List<Documents> r = new ArrayList<Documents>();
        try{
            if (f.canRead()){
                if (f.isFile()) {
                    final String ext = getExt(f.getAbsolutePath());
                    Path file = Paths.get(f.getAbsolutePath());
                    BasicFileAttributes a = Files.readAttributes(file, BasicFileAttributes.class);
                    PosixFileAttributes z = Files.readAttributes(file,PosixFileAttributes.class);
                    r.add(new Documents(trim(file.getFileName().toString()),ext,new Date(a.lastModifiedTime(),
                            a.creationTime(),a.lastAccessTime()),f.getAbsolutePath(),0/*links*/,a.isRegularFile(),
                            a.isOther(),f.isHidden(),z.group().getName(),z.owner().getName(),getPermissions(z.permissions())));
                } else {
                    for (File q : f.listFiles()){
                        r.addAll(list(q));
                    }
                }
            }
        } catch (NullPointerException e){
            throw new ServerException(e);
        } catch (IOException e) {
            throw new ServerException(e);
        }
        //show(r);
        return r;
    }

    private Integer getPermissions(Set<PosixFilePermission> permissions) {
        Integer r = 0;
        String s = permissions.toString();

        if (s.contains("OWNER_WRITE"))
            r+= 200;
        if (s.contains("OWNER_READ"))
            r+= 400;
        if (s.contains("OWNER_EXECUTE"))
            r+= 100;
        if (s.contains("GROUP_WRITE"))
            r+= 20;
        if (s.contains("GROUP_READ"))
            r+= 40;
        if (s.contains("GROUP_EXECUTE"))
            r+= 10;
        if (s.contains("OTHERS_WRITE"))
            r+= 2;
        if (s.contains("OTHERS_READ"))
            r+= 4;
        if (s.contains("OTHERS_EXECUTE"))
            r+= 1;
        return r;
    }

    private void show(List<Documents> documents){
        for (Documents document : documents) {
            System.out.println("document = " + document.name);
        }
    }

    private String getExt(String path){
        int i = path.lastIndexOf(".");
        return (i<0) ? "" : path.substring(i,path.length());
    }

    private String trim(String path){
        return (path.startsWith(".") ? path : ((path.contains(".")) ? path.substring(0,path.lastIndexOf(".")) : path));
    }

}
