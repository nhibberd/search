package main.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EdgeResultSet {
    private ResultSet resultSet = null;

    public EdgeResultSet(EdgePreparedStatement delegate) {
        resultSet = delegate.executeQuery();
    }

    public Boolean next(){
        try {
            return this.resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    public String getString(String label) {
        try {
            return resultSet.getString(label);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getString(int index) {
        try {
            return resultSet.getString(index);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean getBoolean(int index) {
        try {
            return resultSet.getBoolean(index);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Integer getInt(int index) {
        try {
            return resultSet.getInt(index);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Long getLong(int index) {
        try {
            return resultSet.getLong(index);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    public Object getObject(int index) {
        try {
            return resultSet.getObject(index);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
}
