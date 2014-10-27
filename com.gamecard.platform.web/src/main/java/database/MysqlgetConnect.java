package database;

import java.sql.Connection;

public class MysqlgetConnect {
    public static MysqlConnect mysqlcon = null;

    public static Connection getConncet() {
        Connection conn = null;
        try {
            if (mysqlcon == null) {
                mysqlcon = new MysqlConnect();
                mysqlcon.createPool();
                mysqlcon.start();
            }
            if (mysqlcon != null) {
                conn = mysqlcon.getConnection();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static Connection backConncet(Connection conn) {
        try {
            if (mysqlcon != null) {
                mysqlcon.returnConnection(conn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}
