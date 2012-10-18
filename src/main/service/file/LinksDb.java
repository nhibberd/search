package main.service.file;

import main.data.core.Function;
import main.data.core.Status;
import main.data.file.Date;
import main.data.file.Documents;
import main.data.file.Links;
import main.db.EdgePreparedStatement;
import main.db.EdgeResultSet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import static main.tool.CrapToMove.statement;
import static main.tool.Validations.checkrow;

public class LinksDb {
    public Status exists(Connection connection, final String input) {
        String sql = "SELECT * FROM \"SEARCH\".\"LINKS\" WHERE \"DIR\" = ? ";
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

    public Status insert(Connection connection, final Links input) {
        String sqlInsert = "INSERT INTO \"SEARCH\".\"LINKS\"( DIR, LINKS ) VALUES (?, ?)";
        return statement.withStatement(connection,sqlInsert, new Function<PreparedStatement, Status>() {
            public Status apply(PreparedStatement preparedStatement) {
                EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                q.setString(1, input.dir );
                q.setInt(2, input.links);
                return checkrow(q.executeUpdate());
            }
        });
    }

    public List<Links> get (Connection connection){
        String sql = "SELECT * FROM \"SEARCH\".\"LINKS\"";
        return statement.withStatement(connection, sql, new Function<PreparedStatement, List<Links>>() {
            public List<Links> apply(PreparedStatement preparedStatement) {
                List<Links> list = new ArrayList<Links>();
                EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                EdgeResultSet z = new EdgeResultSet(q);
                while (z.next()){
                    list.add(new Links(z.getString(1),z.getInt(2)));
                }
                return list;
            }
        });
    }

}
