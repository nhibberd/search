package main.schedule.jobs;

import main.data.core.Function;
import main.data.error.ServerException;
import main.data.file.*;
import main.db.EdgePreparedStatement;
import main.db.EdgeResultSet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import static main.tool.Base64.byteToBase64;
import static main.tool.CrapToMove.*;

public class FileSystem implements Runnable {


    public void run() {
        list(new File("/Users/dev/intellij/"));
    }

    private List<Documents> list(File f) {
        List<Documents> r = new ArrayList<Documents>();
        try{
            if (f.canRead()){
                if (f.isFile()) {
                    String ext = getExt(f.getAbsolutePath());
                    Path file = Paths.get(f.getAbsolutePath());
                    BasicFileAttributes a = Files.readAttributes(file, BasicFileAttributes.class);
                    System.out.println("a.creationTime() = " + a.creationTime().toMillis());
                    r.add(new Documents(trim(file.getFileName().toString()),ext,new Date(a.lastModifiedTime(),a.creationTime(),a.lastAccessTime()),f.getAbsolutePath()));
                } else {
                    for (File q : f.listFiles()){
                        System.out.println("q = " + q);
                            r.addAll(list(q));
                    }
                }
            }
        } catch (NullPointerException e){
            throw new ServerException(e);
        } catch (IOException e) {
            throw new ServerException(e);
        }
        show(r);
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
        return (path.contains(".")) ? path.substring(0,path.lastIndexOf(".")) : "";
    }




}
