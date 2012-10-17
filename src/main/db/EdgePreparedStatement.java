package main.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EdgePreparedStatement {
    private final PreparedStatement delegate;

    public EdgePreparedStatement(PreparedStatement delegate) {
        this.delegate = delegate;
    }

    public void setInt(int index, int v) {
        try {
            delegate.setInt(index, v);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setString(int index, String v) {
        try {
            delegate.setString(index, v);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setBoolean(int index, Boolean v) {
        try {
            delegate.setBoolean(index,v);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setLong(int index, Long v) {
        try {
            delegate.setLong(index,v);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void setObject(int index, Object o) {
        try {
            delegate.setObject(index, o);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void set(Object... os) {
        for (int i = 1; i <= os.length; ++i)
            setObject(i, os[i]);
    }

    public int executeUpdate() {
        try {
            return delegate.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet executeQuery() {
        try {
            return delegate.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
}
