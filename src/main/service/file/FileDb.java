package main.service.file;

import main.data.core.Function;
import main.data.core.Status;
import main.data.file.Date;
import main.data.file.Documents;
import main.data.index.Urls;
import main.db.EdgePreparedStatement;
import main.db.EdgeResultSet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import static main.tool.Database.statement;
import static main.tool.Validations.checkrow;

public class FileDb {
    public Status exists(Connection connection, final String url) {
        String sql = "SELECT * FROM \"SEARCH\".\"FILE\" WHERE \"URL\" = ? ";
        return statement.withStatement(connection,sql, new Function<PreparedStatement, Status>() {
            public Status apply(PreparedStatement preparedStatement) {
                EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                q.setString(1,url);
                EdgeResultSet z = new EdgeResultSet(q);
                if (z.next()){
                    return Status.BAD_REQUEST;
                }
                return Status.OK;
            }
        });
    }

    public Status insert(Connection connection, final Documents doc) {
        String sqlInsert = "INSERT INTO \"SEARCH\".\"FILE\"( NAME, EXT, MTIME, CTIME, ATIME, URL, LINKS, " +
                "REGFILE, OTHER, HIDDEN, GROUP_NAME, OWNER, PERMISSIONS, HASH, INDEXED ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return statement.withStatement(connection,sqlInsert, new Function<PreparedStatement, Status>() {
            public Status apply(PreparedStatement preparedStatement) {
                EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                q.setString(1, doc.name );
                q.setString(2, doc.ext );
                q.setLong(3, doc.times.mtime );
                q.setLong(4, doc.times.ctime );
                q.setLong(5, doc.times.atime );
                q.setString(6, doc.url );
                q.setInt(7, doc.links );
                q.setBoolean(8, doc.regfile );
                q.setBoolean(9, doc.other );
                q.setBoolean(10, doc.hidden );
                q.setString(11, doc.group );
                q.setString(12, doc.owner );
                q.setInt(13, doc.permissions );
                q.setString(14, doc.hash);
                q.setBoolean(15, doc.indexed);
                return checkrow(q.executeUpdate());
            }
        });
    }

    public Documents get (Connection connection, final String url){
        String sql = "SELECT * FROM \"SEARCH\".\"FILE\" WHERE \"URL\" = ?";
        return statement.withStatement(connection, sql, new Function<PreparedStatement, Documents>() {
            public Documents apply(PreparedStatement preparedStatement) {
                Documents r = null;
                EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                q.setString(1,url);
                EdgeResultSet z = new EdgeResultSet(q);
                if (z.next()){
                    r = (new Documents(z.getInt(1),z.getString(2),z.getString(3),new Date(z.getLong(4),z.getLong(5),z.getLong(6)),
                            z.getString(7),z.getInt(8),z.getBoolean(9),z.getBoolean(10),z.getBoolean(11),z.getString(12),
                            z.getString(13),z.getInt(14),z.getString(15), z.getBoolean(16)));
                }
                return r;
            }
        });
    }

    public List<Urls> getIndexUrls(Connection connection) {
        String sql = "SELECT ID, URL FROM \"SEARCH\".\"FILE\" WHERE INDEXED = FALSE";
        return statement.withStatement(connection, sql, new Function<PreparedStatement, List<Urls>>() {
            public List<Urls> apply(PreparedStatement preparedStatement) {
                List<Urls> r = new ArrayList<Urls>();
                EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                EdgeResultSet z = new EdgeResultSet(q);
                while (z.next()){
                    r.add(new Urls(z.getInt(1),z.getString(2)));
                }
                return r;
            }
        });
    }

    public Boolean update(Connection connection, final Documents docs) {
        String sqlUpdate = "UPDATE \"SEARCH\".\"FILE\" SET \"NAME\" = ?, \"EXT\" = ?, \"MTIME\" = ?, \"CTIME\" = ?, " +
                "\"ATIME\" = ?, \"URL\" = ?, \"LINKS\" = ?, \"REGFILE\" = ?, \"OTHER\" = ?, \"HIDDEN\" = ?, \"GROUP_NAME\" = ?," +
                " \"OWNER\" = ?, \"PERMISSIONS\" = ?, \"HASH\" = ?, \"INDEXED\" = ? WHERE \"ID\" = ?";
        return statement.withStatement(connection, sqlUpdate, new Function<PreparedStatement, Boolean>() {
            public Boolean apply(PreparedStatement preparedStatement) {
                EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                q.setString(1,docs.name);
                q.setString(2,docs.ext);
                q.setLong(3, docs.times.mtime);
                q.setLong(4, docs.times.ctime);
                q.setLong(5, docs.times.atime);
                q.setString(6, docs.url);
                q.setInt(7, docs.links);
                q.setBoolean(8, docs.regfile);
                q.setBoolean(9, docs.other);
                q.setBoolean(10, docs.hidden);
                q.setString(11, docs.group);
                q.setString(12, docs.owner);
                q.setInt(13, docs.permissions);
                q.setString(14, docs.hash);
                q.setBoolean(15, docs.indexed);
                q.setInt(16,docs.id);
                int i = q.executeUpdate();
                return i != 0;
            }
        });
    }


}
