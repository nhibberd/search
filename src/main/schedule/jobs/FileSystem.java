package main.schedule.jobs;

import main.data.core.Action;
import main.data.core.Status;
import main.data.error.ServerException;
import main.data.file.AllFiles;
import main.data.file.Date;
import main.data.file.Documents;
import main.data.file.Links;
import main.service.file.FileDb;
import main.service.file.LinksDb;
import main.tool.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static main.tool.CrapToMove.connector;
import static main.tool.CrapToMove.statement;

public class FileSystem implements Runnable {
   
    //todo check if file is in db
    //todo if it is, check times, check hash

    //note execuatable updates access time


    //todo check if file has links to it ( or dir )

    public void run() {
        final AllFiles files = list(new File("/home/dev/nick/notes"));
        System.out.println("size = " + files.size());

        connector.withConnection(new Action<Connection>() {
            public void apply(final Connection connection) {
                eval(connection, files.docs);
                evalLinks(connection, files.links);
            }
        });
    }

    private void eval(Connection connection, List<Documents> docs) {
        FileDb database = new FileDb();
        for (Documents doc : docs) {
            if (database.exists(connection,doc.url) == Status.OK){
                doc.hash = hash(doc.url);
                database.insert(connection,doc);
                break;
            }
            Documents dbdoc = database.get(connection, doc.url);

            //works
            if (doc.compare(dbdoc)){
                System.out.println("equals");
                //do nothing
            } else {
                System.out.println("don't equals");
                //find which has changed
                    //add to new db table

                //todo check if files have changed
                // atime
                // mtime
                // hidden
                // owner
                // group
                // permissions
                // create hash + check db's hash
                //todo store in new table if changes
            }


            //todo check links (job) = dont need to do this anymore
            

        }
    }
    
    private void evalLinks(Connection connection, List<Path> links){
        // working
        LinksDb database = new LinksDb();
        for (Path link : links) {
            String dir;
            try {
                dir = Files.readSymbolicLink(link).toFile().getAbsolutePath();
            } catch (IOException e) {
                throw new ServerException(e);
            }
            if (database.exists(connection,dir) == Status.OK){
                    database.insert(connection,new Links(dir,0));
            }

            Links dblink = database.get(connection, dir);
            //stuff



        }


        //todo check db | add to db
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
    
    private String hash(String path){
        try {
            MessageDigest z = MessageDigest.getInstance("SHA1");
            byte[] buffer = new byte[1024];
            FileInputStream fis = new FileInputStream(new File(path));
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
            throw new ServerException(e);
        } catch (IOException e) {
            throw new ServerException(e);
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
        return new Documents(trim(file.getFileName().toString()),ext,new Date(a.lastModifiedTime().toMillis(),
                a.creationTime().toMillis(),a.lastAccessTime().toMillis()),file.toFile().getAbsolutePath(),0/*links*/,a.isRegularFile(),
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
