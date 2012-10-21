package main.service.file;

import main.data.core.Function;
import main.data.core.Status;
import main.data.file.Links;
import main.db.EdgePreparedStatement;
import main.db.EdgeResultSet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import static main.tool.Database.statement;
import static main.tool.Validations.checkrow;

/**
 * LinksDb - Database access and manipulation with LINKS table
 */

public class LinksDb {
    public Status exists(Connection connection, final String dir) {
        String sql = "SELECT * FROM \"SEARCH\".\"LINKS\" WHERE \"DIR\" = ? ";
        return statement.withStatement(connection,sql, new Function<PreparedStatement, Status>() {
            public Status apply(PreparedStatement preparedStatement) {
                EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                q.setString(1,dir);
                EdgeResultSet z = new EdgeResultSet(q);
                if (z.next()){
                    return Status.BAD_REQUEST;
                }
                return Status.OK;
            }
        });
    }

    public Status insert(Connection connection, final Links link) {
        String sqlInsert = "INSERT INTO \"SEARCH\".\"LINKS\"( DIR, LINKS, DONE ) VALUES (?, ?, ?)";
        return statement.withStatement(connection,sqlInsert, new Function<PreparedStatement, Status>() {
            public Status apply(PreparedStatement preparedStatement) {
                EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                q.setString(1, link.dir );
                q.setInt(2, link.links);
                q.setBoolean(3, link.done);
                return checkrow(q.executeUpdate());
            }
        });
    }

    public Status delete(Connection connection, final Links link) {
        String sqlInsert = "DELETE FROM \"SEARCH\".\"LINKS\" WHERE \"DIR\" = ?)";
        return statement.withStatement(connection,sqlInsert, new Function<PreparedStatement, Status>() {
            public Status apply(PreparedStatement preparedStatement) {
                EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                q.setString(1, link.dir );
                return checkrow(q.executeUpdate());
            }
        });
    }

    public Links get (Connection connection, final String dir){
        String sql = "SELECT * FROM \"SEARCH\".\"LINKS\" WHERE \"DIR\" = ?";
        return statement.withStatement(connection, sql, new Function<PreparedStatement, Links>() {
            public Links apply(PreparedStatement preparedStatement) {
                Links r = null;
                EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                q.setString(1, dir);
                EdgeResultSet z = new EdgeResultSet(q);
                if (z.next()){
                    r = (new Links(z.getString(1),z.getInt(2),z.getBoolean(3)));
                }
                return r;
            }
        });
    }

    public List<Links> getAll (Connection connection){
        String sql = "SELECT * FROM \"SEARCH\".\"LINKS\" WHERE \"DONE\" = FALSE";
        return statement.withStatement(connection, sql, new Function<PreparedStatement, List<Links>>() {
            public List<Links> apply(PreparedStatement preparedStatement) {
                List<Links> r = new ArrayList<Links>();
                EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                EdgeResultSet z = new EdgeResultSet(q);
                while (z.next()){
                    r.add(new Links(z.getString(1),z.getInt(2),z.getBoolean(3)));
                }
                return r;
            }
        });
    }

    public Boolean update(Connection connection, final Links data) {
        String sqlUpdate = "UPDATE \"SEARCH\".\"LINKS\" SET \"DONE\" = ?, \"LINKS\" = ? WHERE \"DIR\" = ?";
        return statement.withStatement(connection, sqlUpdate, new Function<PreparedStatement, Boolean>() {
            public Boolean apply(PreparedStatement preparedStatement) {
                EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                q.setBoolean(1, data.done);
                q.setInt(2, data.links);
                q.setString(3,data.dir);
                int i = q.executeUpdate();
                return i != 0;
            }
        });
    }
}
