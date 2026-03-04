package org.example;

import java.sql.DriverManager;
import java.sql.SQLException;

public class OrderBookDao {
    public boolean delete(Integer id){
        try {
            var connection= DriverManager.getConnection("somestr");
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
