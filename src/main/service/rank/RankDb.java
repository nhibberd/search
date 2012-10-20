package main.service.rank;

import main.data.core.Function;
import main.data.core.Status;
import main.data.index.Word;
import main.data.rank.Score;
import main.db.EdgePreparedStatement;
import main.db.EdgeResultSet;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static main.tool.Database.statement;
import static main.tool.Validations.checkrow;

/**
 * RankDb - Database access and manipulation with RANK table
 */

public class RankDb {

    /**
     *
     * @param connection  SQL database connection
     * @param id  ID from FILE table
     * @return Status.OK - no result set, Status.BAD_REQUEST - result set exists
     */
    public Status exists(Connection connection, final Integer id) {
        String sql = "SELECT * FROM \"SEARCH\".\"RANK\" WHERE \"ID_FILE\" = ? ";
        return statement.withStatement(connection,sql, new Function<PreparedStatement, Status>() {
            public Status apply(PreparedStatement preparedStatement) {
                EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                q.setInt(1, id);
                EdgeResultSet z = new EdgeResultSet(q);
                if (z.next()){
                    return Status.BAD_REQUEST;
                }
                return Status.OK;
            }
        });
    }

    /**
     *
     * @param connection SQL database connection
     * @param data main.data.rank.Score
     * @return Status
     */
    public Status insert(Connection connection, final Score data) {
        String sqlInsert = "INSERT INTO \"SEARCH\".\"RANK\"( ID_FILE, URL, SCORE ) VALUES (?, ?, ?)";
        return statement.withStatement(connection,sqlInsert, new Function<PreparedStatement, Status>() {
            public Status apply(PreparedStatement preparedStatement) {
                EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                q.setInt(1, data.id_file);
                q.setString(2, data.url);
                q.setInt(3, data.score);
                return checkrow(q.executeUpdate());
            }
        });
    }


    /**
     *
     * @param connection SQL database connection
     * @param id File ID from FILE table
     * @return Status
     */
    public Integer getScore(Connection connection, final Integer id){
        String sql = "SELECT * FROM \"SEARCH\".\"RANK\" WHERE \"ID_FILE\" = ?";
        return statement.withStatement(connection, sql, new Function<PreparedStatement, Integer>() {
            public Integer apply(PreparedStatement preparedStatement) {
                Integer r = null;
                EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                q.setInt(1, id);
                EdgeResultSet z = new EdgeResultSet(q);
                if (z.next()){
                    r = z.getInt(3);
                }
                return r;
            }
        });
    }


    /**
     *
     * @param connection SQL database connection
     * @param data main.data.rank.Score
     * @return true - updated correctly.
     */
    public Boolean update(Connection connection, final Score data) {
        String sqlUpdate = "UPDATE \"SEARCH\".\"RANK\" SET \"SCORE\" = ? WHERE \"ID_FILE\" = ?";
        return statement.withStatement(connection, sqlUpdate, new Function<PreparedStatement, Boolean>() {
            public Boolean apply(PreparedStatement preparedStatement) {
                EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                q.setInt(1, data.score);
                q.setInt(2, data.id_file);
                int i = q.executeUpdate();
                return i != 0;
            }
        });
    }
}
