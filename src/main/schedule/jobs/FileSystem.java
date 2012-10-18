package main.schedule.jobs;

import main.data.error.ServerException;
import main.data.file.AllFiles;
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
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class FileSystem implements Runnable {
   
    //todo check if file is in db
    //todo if it is, check times, check hash

    //note execuatable updates access time


    //todo check if file has links to it ( or dir )

    public void run() {
        AllFiles files = list(new File("/home/dev/nick/"));
        System.out.println("files.size() = " + files.size());
        eval(files.docs);
        evalLinks(files.links);

        //files.links
    }

    private void eval(List<Documents> docs) {
        for (Documents doc : docs) {
            //System.out.println("doc.name = " + doc.name);
        }
        //todo check if files have changed
        // atime
        // mtime
        // hidden
        // owner
        // group
        // permissions
        // create hash + check db's hash

        //todo check links
    }
    
    private void evalLinks(List<Path> links){
        for (Path link : links) {
            System.out.println("link.toFile().getName() = " + link.toFile().getName());
        }
        //todo add to db

    }


    private AllFiles list(File f) {
        return list(f, new AllFiles(new ArrayList<Documents>(), new ArrayList<Path>()));
    }

    private AllFiles list(File f, AllFiles acc) {
        Path file = null;
        if (!f.canRead())
            return acc;
        else if (f.canRead())
            file = Paths.get(f.getAbsolutePath());
        if (f.isFile())
            return acc.adddoc(addFile(file));
        else if (Files.isSymbolicLink(file))
            return acc.addlink(file);
        else {
            AllFiles r = acc;
            for (File q : f.listFiles())
                r = r.add(list(q, acc));
            return r;
        }
    }


    private String trim(String path){
        return (path.startsWith(".") ? path : ((path.contains(".")) ? path.substring(0,path.lastIndexOf(".")) : path));
    }


    private String getExt(String path){
        int i = path.lastIndexOf(".");
        return (i<0) ? "" : path.substring(i,path.length());
    }

    
    private Documents addFile(Path file){
        final String ext = getExt(file.toFile().getAbsolutePath());
        final BasicFileAttributes a;
        final PosixFileAttributes z;
        try {
            a = Files.readAttributes(file, BasicFileAttributes.class);
            z = Files.readAttributes(file,PosixFileAttributes.class);
        } catch (IOException e) {
            throw new ServerException(e);
        }
        return new Documents(trim(file.getFileName().toString()),ext,new Date(a.lastModifiedTime(),
                a.creationTime(),a.lastAccessTime()),file.toFile().getAbsolutePath(),0/*links*/,a.isRegularFile(),
                a.isOther(),file.toFile().isHidden(),z.group().getName(),z.owner().getName(),getPermissions(z.permissions()));

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

}
