package main.service.file;

import main.data.error.ServerException;
import main.data.file.Date;
import main.data.file.Documents;
import main.tool.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

public class FileFunctions {


    public static String hash(String path){
        //todo throwing errors - removeing .properties from file??

        try {
            MessageDigest z = MessageDigest.getInstance("SHA1");
            byte[] buffer = new byte[1024];
            FileInputStream fis = new FileInputStream(new java.io.File(path));
            try {
                int length;
                while ((length = fis.read(buffer)) >= 0)
                    z.update(buffer, 0, length);
            } finally {
                fis.close();
            }
            byte[] result = z.digest();
            return Base64.byteToBase64(result);
        } catch (NoSuchAlgorithmException e) {
            throw new ServerException(e);
        } catch (FileNotFoundException e) {
            //Only possible if temporary file has been deleted between crawl and evaluation
            return "";
            //throw new ServerException(e);
        } catch (IOException e) {
            throw new ServerException(e);
        }
    }


    public static String trim(String path){
        return (path.startsWith(".") ? path : ((path.contains(".")) ? path.substring(0,path.lastIndexOf(".")) : path));
    }


    public static String getExt(String path){
        int i = path.lastIndexOf(".");
        int slash = path.lastIndexOf("/");
        String r = (slash<i) ? path.substring(i+1,path.length()) : "" ;
        return (i<0) ? "" : r;
    }


    public static Documents addFile(Path file){
        final String ext = getExt(file.toFile().getAbsolutePath());
        final BasicFileAttributes a;
        final PosixFileAttributes z;
        try {
            a = Files.readAttributes(file, BasicFileAttributes.class);
            z = Files.readAttributes(file,PosixFileAttributes.class);
        } catch (IOException e) {
            throw new ServerException(e);
        }
        return new Documents(trim(file.getFileName().toString()),ext,new Date(a.lastModifiedTime().toMillis(),
                a.creationTime().toMillis(),a.lastAccessTime().toMillis()),file.toFile().getAbsolutePath(),0/*links*/,a.isRegularFile(),
                a.isOther(),file.toFile().isHidden(),z.group().getName(),z.owner().getName(),getPermissions(z.permissions()));

    }


    public static Integer getPermissions(Set<PosixFilePermission> permissions) {
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
