package main.config;

import main.data.core.Action;
import main.db.Connector;
import main.db.EdgePreparedStatement;
import main.db.EdgeResultSet;
import main.db.Statement;
import main.tool.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class Schema {
    Connector connector = Database.connector;

    public void apply (){
        connector.withConnection(new Action<Connection>() {
            private final Statement statement = new Statement();
            public void apply(Connection connection) {
                statement.withStatement(connection, "SELECT * FROM \"SEARCH\".\"SCHEMA_VERSION\"", new Action<PreparedStatement>() {
                    public void apply(PreparedStatement preparedStatement) {
                        EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                        EdgeResultSet resultSet = new EdgeResultSet(q);
                        String r = "";
                        if (resultSet.next()) {
                            r = resultSet.getString(1);
                        }
                        if (!r.equals("1"))
                            System.exit(1);
                    }
                });
            }
        });
    }
}
