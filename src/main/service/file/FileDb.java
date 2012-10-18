package main.service.file;

import main.data.core.Function;
import main.data.core.Status;
import main.data.file.Date;
import main.data.file.Documents;
import main.db.EdgePreparedStatement;
import main.db.EdgeResultSet;

import java.nio.file.attribute.FileTime;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import static main.tool.CrapToMove.statement;
import static main.tool.Validations.checkrownu;

public class FileDb {
    public Status exists(Connection connection, final String input) {
        String sql = "SELECT * FROM \"SEARCH\".\"FILE\" WHERE \"URL\" = ? ";
        return statement.withStatement(connection,sql, new Function<PreparedStatement, Status>() {
            public Status apply(PreparedStatement preparedStatement) {
                EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                q.setString(1,input);
                EdgeResultSet z = new EdgeResultSet(q);
                if (z.next()){
                    return Status.BAD_REQUEST;
                }
                return Status.OK;
            }
        });
    }

    //todo
    public Status insert(Connection connection, final Documents input) {
        String sqlInsert = "INSERT INTO \"SEARCH\".\"FILE\"( NAME, EXT, MTIME, CTIME, ATIME, URL, LINKS, " +
                "REGFILE, OTHER, HIDDEN, GROUPS, OWNER, PERMISSIONS ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return statement.withStatement(connection,sqlInsert, new Function<PreparedStatement, Status>() {
            public Status apply(PreparedStatement preparedStatement) {
                EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                q.setString(1, input.name );
                q.setString(2, input.ext );
                q.setLong(3, input.times.mtime );
                q.setLong(4, input.times.ctime );
                q.setLong(5, input.times.atime );
                q.setString(6, input.url );
                q.setInt(7, input.links );
                q.setBoolean(8, input.regfile );
                q.setBoolean(9, input.other );
                q.setBoolean(10, input.hidden );
                q.setString(11, input.group );
                q.setString(12, input.owner );
                q.setInt(13, input.permissions );
                return checkrownu(q.executeUpdate());
            }
        });
    }

    //todo
    private List<Documents> get (Connection connection){
        String sql = "SELECT * FROM \"SEARCH\".\"FILE\"";
        return statement.withStatement(connection, sql, new Function<PreparedStatement, List<Documents>>() {
            public List<Documents> apply(PreparedStatement preparedStatement) {
                List<Documents> list = new ArrayList<Documents>();
                EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                EdgeResultSet z = new EdgeResultSet(q);
                while (z.next()){
                    list.add(new Documents(z.getString(1),z.getString(2),new Date(z.getLong(3),z.getLong(4),z.getLong(5)),
                            z.getString(6),z.getInt(7),z.getBoolean(8),z.getBoolean(9),z.getBoolean(10),z.getString(11),
                            z.getString(12),z.getInt(13)));
                }
                return list;
            }
        });
    }

}
