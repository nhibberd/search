package main.config.database;

import main.config.Config;
import main.config.ConfigSetup;
import main.data.core.Function;
import main.data.error.ServerException;
import main.db.Connector;
import main.db.EdgePreparedStatement;
import main.db.EdgeResultSet;
import main.db.Statement;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class VersionOne {
    Config config = new ConfigSetup().stuff();
    private final Connector connector = new Connector("jdbc:hsqldb:file:" + config.dblocation, config.dbuser, config.dbpassword);
    private final Statement statement = new Statement();

    public Boolean setup(){
        final String createuser = "CREATE USER admin PASSWORD agpro";

        final String schema = "CREATE SCHEMA search AUTHORIZATION admin\n" +
                "CREATE TABLE IF NOT EXISTS link ( filepath VARCHAR(255), links INTEGER )\n" +
                "CREATE TABLE IF NOT EXISTS linkdir ( dir VARCHAR(255), links INTEGER )\n" +
                "CREATE TABLE IF NOT EXISTS file ( id INTEGER PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY, name VARCHAR(255), " +
                "ext VARCHAR(255), mtime BIGINT, ctime BIGINT, atime BIGINT, url VARCHAR(255), links INTEGER, regfile BOOLEAN, " +
                "other BOOLEAN, hidden BOOLEAN, group VARCHAR(255), owner VARCHAR(255), permissions INTEGER ,hash VARCHAR(255) )\n" +
                "CREATE TABLE IF NOT EXISTS time ( id_file INTEGER PRIMARY KEY, mod INTEGER, access INTEGER )\n" +
                "CREATE TABLE IF NOT EXISTS schema_version ( version VARCHAR(25) PRIMARY KEY )\n" +
                "GRANT ALL ON link TO admin\n" +
                "GRANT ALL ON linkdir TO admin\n" +
                "GRANT ALL ON file TO admin\n" +
                "GRANT ALL ON schema_version TO admin;";

        final String schemaversion = "INSERT INTO main.SCHEMA_VERSION (VERSION) VALUES (1);";
        final String selectversion = "SELECT * FROM main.SCHEMA_VERSION;";


        return connector.withConnection(new Function<Connection, Boolean>() {
            public Boolean apply(final Connection connection) {
                Boolean schemareturn = true;
                Boolean userreturn = true;
                Boolean version = true;
                Boolean selectv = true;
                try{
                    userreturn = create(connection, createuser);
                } catch (ServerException e){
                    userreturn = false;
                }
                try{
                    schemareturn = create(connection, schema);
                } catch (IllegalArgumentException e){
                    schemareturn = false;
                }
                try{
                    selectv = select(connection, selectversion);
                } catch (IllegalArgumentException e){
                    selectv = false;
                }

                if (!selectv){
                    try{
                        version = create(connection, schemaversion);
                    } catch (IllegalArgumentException e){
                        version = false;
                    }
                }
                return true;
            }
        });
    }

    private Boolean create(Connection connection, String sql){
        return statement.withStatement(connection, sql, new Function<PreparedStatement, Boolean>() {
            public Boolean apply(PreparedStatement preparedStatement) {
                EdgePreparedStatement z = new EdgePreparedStatement(preparedStatement);
                return z.executeUpdate() == 1;
            }
        });
    }

    private Boolean select(Connection connection, String sql){
        return statement.withStatement(connection, sql, new Function<PreparedStatement, Boolean>() {
            public Boolean apply(PreparedStatement preparedStatement) {
                EdgePreparedStatement z = new EdgePreparedStatement(preparedStatement);
                EdgeResultSet resultSet = new EdgeResultSet(z);
                return resultSet.next();
            }
        });
    }
}