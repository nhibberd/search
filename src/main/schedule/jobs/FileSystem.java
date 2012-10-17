package main.schedule.jobs;

import main.data.core.Function;
import main.data.error.ServerException;
import main.data.file.FileDB;
import main.data.file.FormDB;
import main.data.file.Order;
import main.db.EdgePreparedStatement;
import main.db.EdgeResultSet;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import static main.tool.Base64.byteToBase64;
import static main.tool.CrapToMove.*;

public class FileSystem implements Runnable {

    //todo if file gets deleted from system, make db recognise this and remove it from db

    public void run() {
        list(new File(config.directory + "clients/"));
        listforms(new File(config.directory + "forms/"));
    }

    private void list(File f) {
        try{
            List<FileDB> list = new ArrayList<FileDB>();
            for (File q : f.listFiles()){
                if (q.isDirectory())
                    list(q);
                else if (q.isFile()) {
                    if (check(f.getName())){
                        FileDB file = new FileDB(normalise(q.getAbsolutePath()),createHash(q), grower(q),q.lastModified(),false,true,canonical(q),ordinal(q),false);
                        list.add(file);
                    }
                }
            }
            add( recent(list) );

        } catch (NullPointerException e){
            throw new ServerException(e);
        }
    }


    private List<FileDB> recent(List<FileDB> input) {
        List<FileDB> z = new ArrayList<FileDB>();
        FileDB latest = new FileDB("","",0,0,false,false,"","",false);
        for (FileDB file : input) {
            List<FileDB> similar = getords(file.ordinal, input);
            for (FileDB sfile : similar){
                if (sfile == file) {
                    sfile.mostrecent=true;
                    z.add(sfile);
                } else if (latest.timestamp <= sfile.timestamp){
                    latest = sfile;
                    sfile.mostrecent=true;
                    z.add(sfile);
                } else {
                    sfile.mostrecent=false;
                    z.add(sfile);
                }

            }
        }
        return z;
    }

    private List<FileDB> getords(String ordinal, List<FileDB> input) {
        List<FileDB> r = new ArrayList<FileDB>();
        for (FileDB file : input) {
            if (file.ordinal.equals(ordinal))
                r.add(file);
        }
        return r;
    }

    private void add(List<FileDB> list){

        for (FileDB file : list) {
            if (!exist(file))
                add(file);
            else if (exist(file) && !getHash(file).equals(file.hash))
                update(file.hash, file.filepath, file.timestamp, true);
            else if (isrecent(file) && !file.mostrecent)
                update(false, file.filepath);
        }

    }

    private void listforms(File f) {
        try{
            for (File q : f.listFiles()){
                if (q.isDirectory())
                    listforms(q);
                else if (q.isFile()) {
                    if (check(f.getName())){
                        FormDB form = new FormDB(normalise(q.getAbsolutePath()),createHash(q), q.lastModified(),false,true,canonical(q));
                        if (!formexist(form))
                            add(form);
                        else if (formexist(form) && !getformHash(form).equals(createHash(q)))
                            dbupdate(form.filepath,q.lastModified(),true);
                    }
                }
            }
        } catch (NullPointerException e){
            throw new ServerException(e);
        }
    }


    private String normalise(String absolutePath) {
        String normalPath = absolutePath.replace('\\', '/');
        int p = normalPath.lastIndexOf("/documents/");
        return normalPath.substring(p);
    }


    private String ordinal(File file) {
        String normalPath = file.getAbsolutePath().replace('\\', '/');
        int p = normalPath.lastIndexOf("/");
        String name = normalPath.substring(p+1);
        if (name.contains("_"))
            return cut(name);
        return name;
    }

    private String cut(String name) {
        int i = name.lastIndexOf("_");
        return name.substring(0,i);
    }

    private String canonical(File file) {
        String path = file.getAbsolutePath();
        String nupath = path.replace('\\','/');
        int i = nupath.lastIndexOf('/');
        return nupath.substring(0,i);
    }

    private Integer grower(File file) {
        String path = file.getAbsolutePath();
        String nupath = path.replace('\\','/');
        int i = nupath.lastIndexOf("/clients/");
        String grower = nupath.substring(i);
        String grower2 = grower.substring(9);
        int p = grower2.indexOf('/');
        return Integer.valueOf(grower2.substring(0,p));
    }


    private Boolean check(String name) {
        return !(name.contains("GID") || name.contains("GCF")); //GIDvtSpecial_999_1_numbers or GCF-115803-001
    }


    private String createHash(File file) {
        byte[] r;
        try {
            r = createChecksum(file);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return byteToBase64(r);
    }

    private static byte[] createChecksum(File file) throws Exception {
        InputStream fis =  new FileInputStream(file);

        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance("MD5");
        int numRead;

        do {
            numRead = fis.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while (numRead != -1);

        fis.close();
        return complete.digest();
    }

    private Boolean exist(final FileDB file){
        return connector.withConnection(new Function<Connection, Boolean>() {
            public Boolean apply(final Connection connection) {
                String sqlInsert = "SELECT * FROM \"AGPRO\".\"FILE\" WHERE FILEPATH = ?";
                return statement.withStatement(connection, sqlInsert, new Function<PreparedStatement, Boolean>() {
                    public Boolean apply(PreparedStatement preparedStatement) {
                        EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                        q.setString(1, file.filepath);
                        EdgeResultSet z = new EdgeResultSet(q);
                        return z.next();
                    }
                });
            }
        });
    }

    private Boolean isrecent(final FileDB file){
        return connector.withConnection(new Function<Connection, Boolean>() {
            public Boolean apply(final Connection connection) {
                String sqlInsert = "SELECT * FROM \"AGPRO\".\"FILE\" WHERE FILEPATH = ?";
                return statement.withStatement(connection, sqlInsert, new Function<PreparedStatement, Boolean>() {
                    public Boolean apply(PreparedStatement preparedStatement) {
                        EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                        q.setString(1, file.filepath);
                        EdgeResultSet z = new EdgeResultSet(q);
                        if (z.next()){
                            return z.getBoolean(9);
                        }
                        return null;
                    }
                });
            }
        });
    }

    private String getHash(final FileDB file){
        return connector.withConnection(new Function<Connection, String>() {
            public String apply(final Connection connection) {
                String sqlInsert = "SELECT * FROM \"AGPRO\".\"FILE\" WHERE FILEPATH = ?";
                return statement.withStatement(connection, sqlInsert, new Function<PreparedStatement, String>() {
                    public String apply(PreparedStatement preparedStatement) {
                        EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                        q.setString(1, file.filepath);
                        EdgeResultSet z = new EdgeResultSet(q);
                        if (z.next()){
                            return z.getString(2);
                        }
                        return null;
                    }
                });
            }
        });
    }

    private Boolean update(final String hash, final String filepath, final long time, final boolean b) {
        return connector.withConnection(new Function<Connection, Boolean>() {
            public Boolean apply(final Connection connection) {
                String sqlUpdate = "UPDATE \"AGPRO\".\"FILE\" SET \"HASH\" = ?,\"TIMESTAMP\" = ?, \"MODIFIED\" = ? WHERE \"FILEPATH\" = ?";
                return statement.withStatement(connection, sqlUpdate, new Function<PreparedStatement, Boolean>() {
                    public Boolean apply(PreparedStatement preparedStatement) {
                        EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                        q.setString(1,hash);
                        q.setLong(2, time);
                        q.setBoolean(3, b);
                        q.setString(4, filepath);
                        int i = q.executeUpdate();
                        return i != 0;
                    }
                });
            }
        });
    }

    private Boolean update(final Boolean recent, final String filepath) {
        return connector.withConnection(new Function<Connection, Boolean>() {
            public Boolean apply(final Connection connection) {
                String sqlUpdate = "UPDATE \"AGPRO\".\"FILE\" SET \"MOSTRECENT\" = ? WHERE \"FILEPATH\" = ?";
                return statement.withStatement(connection, sqlUpdate, new Function<PreparedStatement, Boolean>() {
                    public Boolean apply(PreparedStatement preparedStatement) {
                        EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                        q.setBoolean(1, recent);
                        q.setString(2, filepath);
                        int i = q.executeUpdate();
                        return i != 0;
                    }
                });
            }
        });
    }

    private Boolean add(final FileDB file){
        return connector.withConnection(new Function<Connection, Boolean>() {
            public Boolean apply(final Connection connection) {
                String sqlInsert = "INSERT INTO \"AGPRO\".\"FILE\"( FILEPATH, HASH, GROWER, TIMESTAMP, MODIFIED, NEW, CANCONICAL, ORDINAL, MOSTRECENT ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                return statement.withStatement(connection, sqlInsert, new Function<PreparedStatement, Boolean>() {
                    public Boolean apply(PreparedStatement preparedStatement) {
                        EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                        q.setString(1, file.filepath);
                        q.setString(2, file.hash);
                        q.setInt(3, file.grower);
                        q.setLong(4, file.timestamp);
                        q.setBoolean(5, file.modified);
                        q.setBoolean(6, file.nu);
                        q.setString(7, file.canonical);
                        q.setString(8, file.ordinal);
                        q.setBoolean(9, file.mostrecent);
                        int i = q.executeUpdate();
                        return i != 0;
                    }
                });
            }
        });
    }

    private Boolean formexist(final FormDB file){
        return connector.withConnection(new Function<Connection, Boolean>() {
            public Boolean apply(final Connection connection) {
                String sqlInsert = "SELECT * FROM \"AGPRO\".\"FORM\" WHERE FILEPATH = ?";
                return statement.withStatement(connection, sqlInsert, new Function<PreparedStatement, Boolean>() {
                    public Boolean apply(PreparedStatement preparedStatement) {
                        EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                        q.setString(1, file.filepath);
                        EdgeResultSet z = new EdgeResultSet(q);
                        return z.next();
                    }
                });
            }
        });
    }

    private String getformHash(final FormDB form){
        return connector.withConnection(new Function<Connection, String>() {
            public String apply(final Connection connection) {
                String sqlInsert = "SELECT * FROM \"AGPRO\".\"FORM\" WHERE \"FILEPATH\" = ?";
                return statement.withStatement(connection, sqlInsert, new Function<PreparedStatement, String>() {
                    public String apply(PreparedStatement preparedStatement) {
                        EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                        q.setString(1, form.filepath);
                        EdgeResultSet z = new EdgeResultSet(q);
                        if (z.next()){
                            return z.getString(2);
                        }
                        return null;
                    }
                });
            }
        });
    }


    private Boolean dbupdate(final String filepath, final long time, final boolean b) {
        return connector.withConnection(new Function<Connection, Boolean>() {
            public Boolean apply(final Connection connection) {
                String sqlUpdate = "UPDATE \"AGPRO\".\"FORM\" SET \"TIMESTAMP\" = ?, \"MODIFIED\" = ? WHERE \"FILEPATH\" = ?";
                return statement.withStatement(connection, sqlUpdate, new Function<PreparedStatement, Boolean>() {
                    public Boolean apply(PreparedStatement preparedStatement) {
                        EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                        q.setLong(1, time);
                        q.setBoolean(2, b);
                        q.setString(3, filepath);
                        int i = q.executeUpdate();
                        return i != 0;
                    }
                });
            }
        });
    }

    private Boolean add(final FormDB file){
        return connector.withConnection(new Function<Connection, Boolean>() {
            public Boolean apply(final Connection connection) {
                String sqlInsert = "INSERT INTO \"AGPRO\".\"FORM\"( FILEPATH, HASH, TIMESTAMP, MODIFIED, NEW, CANCONICAL ) VALUES (?, ?, ?, ?, ?, ?)";
                return statement.withStatement(connection, sqlInsert, new Function<PreparedStatement, Boolean>() {
                    public Boolean apply(PreparedStatement preparedStatement) {
                        EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                        q.setString(1, file.filepath);
                        q.setString(2, file.hash);
                        q.setLong(3, file.timestamp);
                        q.setBoolean(4, file.modified);
                        q.setBoolean(5, file.nu);
                        q.setString(6, file.canonical);
                        int i = q.executeUpdate();
                        return i != 0;
                    }
                });
            }
        });
    }

    private Boolean deleteForm(final String hash){
        return connector.withConnection(new Function<Connection, Boolean>() {
            public Boolean apply(final Connection connection) {
                String sqlInsert = "DELETE FROM \"AGPRO\".\"FORM\" WHERE HASH = ?";
                return statement.withStatement(connection, sqlInsert, new Function<PreparedStatement, Boolean>() {
                    public Boolean apply(PreparedStatement preparedStatement) {
                        EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                        q.setString(1, hash);
                        int i = q.executeUpdate();
                        return i != 0;
                    }
                });
            }
        });
    }

    private Boolean delete(final String hash){
        return connector.withConnection(new Function<Connection, Boolean>() {
            public Boolean apply(final Connection connection) {
                String sqlInsert = "DELETE FROM \"AGPRO\".\"FILE\" WHERE HASH = ?";
                return statement.withStatement(connection, sqlInsert, new Function<PreparedStatement, Boolean>() {
                    public Boolean apply(PreparedStatement preparedStatement) {
                        EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                        q.setString(1, hash);
                        int i = q.executeUpdate();
                        return i != 0;
                    }
                });
            }
        });
    }

    private List<Order> get(final String ordinal){
        return connector.withConnection(new Function<Connection, List<Order>>() {
            public List<Order> apply(final Connection connection) {
                String sqlInsert = "SELECT * FROM \"AGPRO\".\"FILE\" WHERE ORDINAL = ?";
                return statement.withStatement(connection, sqlInsert, new Function<PreparedStatement, List<Order>>() {
                    public List<Order> apply(PreparedStatement preparedStatement) {
                        List<Order> list = new ArrayList<Order>();
                        EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                        q.setString(1, ordinal);
                        EdgeResultSet z = new EdgeResultSet(q);
                        while (z.next()){
                            String hash = z.getString(2);
                            long time = z.getLong(4);
                            list.add(new Order(hash,time));
                        }
                        return list;
                    }
                });
            }
        });
    }




    private List<FileDB> getlist() {
        return connector.withConnection(new Function<Connection, List<FileDB>>() {
            public List<FileDB> apply(final Connection connection) {
                String sql = "SELECT * FROM \"AGPRO\".\"FILE\"";
                return statement.withStatement(connection, sql, new Function<PreparedStatement, List<FileDB>>() {
                    public List<FileDB> apply(PreparedStatement preparedStatement) {
                        EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                        EdgeResultSet z = new EdgeResultSet(q);
                        Integer grower;
                        String filepath = null, hash = null, canonical = null, ordinal = null;
                        long timestamp;
                        Boolean modified = null, nu = null, recent;
                        List<FileDB> list = new ArrayList<FileDB>();
                        while (z.next()) {
                            filepath = z.getString(1);
                            hash = z.getString(2);
                            grower = z.getInt(3);
                            timestamp = z.getLong(4);
                            modified = z.getBoolean(5);
                            nu = z.getBoolean(6);
                            canonical = z.getString(7);
                            ordinal = z.getString(8);
                            recent = z.getBoolean(9);

                            list.add(new FileDB(filepath, hash, grower, timestamp, modified, nu, canonical, ordinal,recent));
                        }
                        return list;
                    }

                });
            }
        });
    }

    private List<FormDB> getformlist() {
        return connector.withConnection(new Function<Connection, List<FormDB>>() {
            public List<FormDB> apply(final Connection connection) {
                String sql = "SELECT * FROM \"AGPRO\".\"FORM\"";
                return statement.withStatement(connection, sql, new Function<PreparedStatement, List<FormDB>>() {
                    public List<FormDB> apply(PreparedStatement preparedStatement) {
                        EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                        EdgeResultSet z = new EdgeResultSet(q);
                        String filepath = null, hash = null, canonical = null;
                        long timestamp;
                        Boolean modified = null, nu = null;
                        List<FormDB> list = new ArrayList<FormDB>();
                        while (z.next()) {
                            filepath = z.getString(1);
                            hash = z.getString(2);
                            timestamp = z.getLong(3);
                            modified = z.getBoolean(4);
                            nu = z.getBoolean(5);
                            canonical = z.getString(6);

                            list.add(new FormDB(filepath, hash, timestamp, modified, nu, canonical));
                        }
                        return list;
                    }

                });
            }
        });
    }

}
