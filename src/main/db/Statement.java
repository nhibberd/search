package main.db;

import main.data.core.Action;
import main.data.core.Function;
import main.data.error.DatabaseException;
import main.data.error.ServerException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Statement {
    public Connection connection;
    public String sql;


    public Statement() {
    }

    public Statement(Connection connection, String sql) {
        this.connection = connection;
        this.sql = sql;
    }

    public <A> A withStatement(Connection connection, String sql, Function<PreparedStatement, A> thunk) {
        this.connection = connection;
        this.sql = sql;
        PreparedStatement statement = statement();
        try {
            return thunk.apply(statement);
        } catch (Exception e) {
            rollback(statement);
            throw new DatabaseException(e);
        } finally {
            close(statement);
        }
    }

    public void withStatement(Connection connection, String sql, Action<PreparedStatement> thunk) {
        this.connection = connection;
        this.sql = sql;
        PreparedStatement statement = statement();
        try {
            thunk.apply(statement);
        } catch (Exception e) {
            rollback(statement);
            throw new DatabaseException(e);
        } finally {
            close(statement);
        }
    }

    private void close(PreparedStatement statement) {
        try {
            statement.close();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private void rollback(PreparedStatement statement) {
        try {
            statement.cancel();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private PreparedStatement statement() {
        try {
            return connection.prepareStatement(sql);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
