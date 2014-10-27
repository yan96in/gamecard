package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlConnect
{
    private static String jdbcDriver = "com.mysql.jdbc.Driver";

    private static String dbUrl = "jdbc:mysql://localhost:9004/ivr";

    private static String dbUsername = "ivruser";

    private static String dbPassword = "skyd@ivruser";

    public static Connection newConnection()
            throws SQLException
    {
        Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);

        return conn;
    }

    public static void closeConnection(Connection conn)
    {
        try
        {
            conn.close();
        }
        catch (SQLException e)
        {
            System.out.println(" 关闭数据库连接出错： " + e.getMessage());
        }
    }
}
