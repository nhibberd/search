package main.service.file;

import main.data.core.Function;
import main.data.core.Status;
import main.data.file.State;
import main.db.EdgePreparedStatement;
import main.db.EdgeResultSet;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static main.tool.Database.statement;
import static main.tool.Validations.checkrow;

/**
 * StateDb - Database access and manipulation with STATE table
 */

public class StateDb {



    public Status exists(Connection connection, final String url) {
        String sql = "SELECT * FROM \"SEARCH\".\"STATE\" WHERE \"URL\" = ? ";
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

    public Status insert(Connection connection, final State data) {
        String sqlInsert = "INSERT INTO \"SEARCH\".\"STATE\"( URL, MTIME, ATIME, GROUP_NAME, OWNER, PERMISSIONS ) VALUES (?, ?, ?, ?, ?, ?)";
        return statement.withStatement(connection,sqlInsert, new Function<PreparedStatement, Status>() {
            public Status apply(PreparedStatement preparedStatement) {
                EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                q.setString(1, data.url);
                q.setInt(2, data.mtime);
                q.setInt(3, data.atime);
                q.setInt(4, data.group);
                q.setInt(5, data.owner);
                q.setInt(6, data.permissions);
                return checkrow(q.executeUpdate());
            }
        });
    }

    public State get (Connection connection, final String url){
        String sql = "SELECT * FROM \"SEARCH\".\"STATE\" WHERE \"URL\" = ?";
        return statement.withStatement(connection, sql, new Function<PreparedStatement, State>() {
            public State apply(PreparedStatement preparedStatement) {
                State r = null;
                EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                q.setString(1,url);
                EdgeResultSet z = new EdgeResultSet(q);
                if (z.next()){
                    r = (new State(url,z.getInt(2),z.getInt(3),z.getInt(4),z.getInt(5),z.getInt(6)));
                }
                return r;
            }
        });
    }

    public Integer getCount (Connection connection, final String url){
        String sql = "SELECT (\"MTIME\" + \"ATIME\" + \"GROUP_NAME\" + \"OWNER\" + \"PERMISSIONS\") As calc FROM \"SEARCH\".\"STATE\" WHERE \"URL\" = ?";
        return statement.withStatement(connection, sql, new Function<PreparedStatement, Integer>() {
            public Integer apply(PreparedStatement preparedStatement) {
                Integer r = null;
                EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                q.setString(1,url);
                EdgeResultSet z = new EdgeResultSet(q);
                if (z.next()){
                    r = z.getInt(1);
                }
                return r;
            }
        });
    }

    public Boolean update(Connection connection, final State input) {
        String sqlUpdate = "UPDATE \"SEARCH\".\"STATE\" SET \"MTIME\" = ?, \"ATIME\" = ?, \"GROUP_NAME\" = ?, \"OWNER\" = ?, " +
                "\"PERMISSIONS\" = ? WHERE \"URL\" = ?";
        return statement.withStatement(connection, sqlUpdate, new Function<PreparedStatement, Boolean>() {
            public Boolean apply(PreparedStatement preparedStatement) {
                EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                q.setInt(1, input.mtime);
                q.setInt(2, input.atime);
                q.setInt(3, input.group);
                q.setInt(4, input.owner);
                q.setInt(5, input.permissions);
                q.setString(6, input.url);
                int i = q.executeUpdate();
                return i != 0;
            }
        });
    }
}
