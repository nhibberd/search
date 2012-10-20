package main.service.index;

import main.data.core.Function;
import main.data.core.Status;
import main.data.file.State;
import main.data.index.Urls;
import main.data.index.Word;
import main.db.EdgePreparedStatement;
import main.db.EdgeResultSet;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static main.tool.Database.statement;
import static main.tool.Validations.checkrow;

public class IndexDb {



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

    public Boolean update(Connection connection, final Word data) {
        System.out.println("data.id_file_count = " + data.word + " : " + data.id_file_count);
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
