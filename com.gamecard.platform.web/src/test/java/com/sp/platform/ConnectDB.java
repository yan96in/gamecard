package com.sp.platform;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {

    private static final String MYSQL = "jdbc:mysql://";

    private static final String ORACLE = "jdbc:oracle:thin:@";

    private static final String SQLSERVER = "jdbc:microsoft:sqlserver://";

    private ConnectDB() {
    }

    public static Connection getConnection(String DBType, String url, String user, String password)
            throws SQLException {
        if ("mysql".equalsIgnoreCase(DBType))
            return getMySqlConn(url, user, password);
        if ("oracle".equalsIgnoreCase(DBType))
            return getOracleConn(url, user, password);
        if ("sqlserver".equals(DBType)) {
            return getSqlServerConn(url, user, password);
        }
        return null;
    }

    public static void closeConn(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static Connection getMySqlConn(String url, String user, String password) throws
            SQLException {
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");// 加载驱动
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        conn = DriverManager.getConnection(MYSQL + url, user, password);
        return conn;
    }

    private static Connection getOracleConn(String url, String user, String password) throws
            SQLException {
        Connection conn = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");// 加载驱动
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        conn = DriverManager.getConnection(ORACLE + url, user, password);

        return conn;
    }

    private static Connection getSqlServerConn(String url, String user, String password) throws
            SQLException {
        Connection conn = null;
        try {
            Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");// 加载
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        conn = DriverManager.getConnection(SQLSERVER + url, user, password);
        return conn;
    }

    public static void main(String[] args) {
        try {
            Connection conn = getConnection("MySQL", "115.29.37.75:3306/spff", "root", "adminff");
            if (conn == null) {
                System.out.println("Connection the database is failled !");
            } else {
                System.out.println(conn.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}

