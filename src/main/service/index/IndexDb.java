package main.service.index;

import main.data.core.Function;
import main.data.core.Status;
import main.data.index.Word;
import main.db.EdgePreparedStatement;
import main.db.EdgeResultSet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import static main.tool.Database.statement;
import static main.tool.Validations.checkrow;

/**
 * IndexDb - Database access and manipulation with INDEX table
 */

public class IndexDb {

    /**
     *
     * @param connection SQL database connection
     * @param word Seach term
     * @return Status.OK - no result set, Status.BAD_REQUEST - result set exists
     */
    public Status exists(Connection connection, final String word) {
        String sql = "SELECT * FROM \"SEARCH\".\"INDEX\" WHERE \"WORD\" = ? ";
        return statement.withStatement(connection,sql, new Function<PreparedStatement, Status>() {
            public Status apply(PreparedStatement preparedStatement) {
                EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                q.setString(1,word);
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
     * @param data main.data.index.Word
     * @return Status
     */
    public Status insert(Connection connection, final Word data) {
        String sqlInsert = "INSERT INTO \"SEARCH\".\"INDEX\"( WORD, ID_FILE_COUNT ) VALUES (?, ?)";
        return statement.withStatement(connection,sqlInsert, new Function<PreparedStatement, Status>() {
            public Status apply(PreparedStatement preparedStatement) {
                EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                q.setString(1, data.word);
                q.setString(2, data.id_file_count);
                return checkrow(q.executeUpdate());
            }
        });
    }

    /**
     *
     * @param connection SQL database connection
     * @param word Seach term
     * @return String - List of (file_id : count). Count is the number of times the word is in the corresponding file url
     */
    public String get (Connection connection, final String word){
        String sql = "SELECT * FROM \"SEARCH\".\"INDEX\" WHERE \"WORD\" = ?";
        return statement.withStatement(connection, sql, new Function<PreparedStatement, String>() {
            public String apply(PreparedStatement preparedStatement) {
                String r = "";
                EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                q.setString(1,word);
                EdgeResultSet z = new EdgeResultSet(q);
                if (z.next()){
                    r = z.getString(2);
                }
                return r;
            }
        });
    }

    /**
     *
     * @param connection SQL database connection
     * @param word Seach term
     * @return String - List of (file_id : count). Count is the number of times the word is in the corresponding file url
     */
    public List<String> getLike (Connection connection, final String word){
        String like = "%";
        final String use = like.concat(word.concat("%"));
        String sql = "SELECT * FROM \"SEARCH\".\"INDEX\" WHERE \"WORD\" LIKE ?";
        return statement.withStatement(connection, sql, new Function<PreparedStatement, List<String>>() {
            public List<String> apply(PreparedStatement preparedStatement) {
                List<String> r = new ArrayList<String>();
                EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                q.setString(1,use);
                EdgeResultSet z = new EdgeResultSet(q);
                while (z.next()){
                    r.add(z.getString(2));
                }
                return r;
            }
        });
    }

    /**
     *
     * @param connection SQL database connection
     * @param data main.data.index.Word
     * @return Boolean - correct update
     */
    public Boolean update(Connection connection, final Word data) {
        String sqlUpdate = "UPDATE \"SEARCH\".\"INDEX\" SET \"ID_FILE_COUNT\" = ? WHERE \"WORD\" = ?";
        return statement.withStatement(connection, sqlUpdate, new Function<PreparedStatement, Boolean>() {
            public Boolean apply(PreparedStatement preparedStatement) {
                EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                q.setString(1, data.id_file_count);
                q.setString(2, data.word);
                int i = q.executeUpdate();
                return i != 0;
            }
        });
    }
}
