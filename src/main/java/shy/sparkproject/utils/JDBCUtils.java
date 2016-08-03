package shy.sparkproject.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * JDBC工具类
 * Created by Shy on 2016/6/14.
 */
public class JDBCUtils {

    private static DataSource dataSource = null;

    static {
        dataSource = new ComboPooledDataSource("c3p0");
    }

    public static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void releaseDB(ResultSet resultSet, Statement statement,
                                 Connection connection) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                //数据库连接池的 Connection 对象进行 close 时
                //并不是真的进行关闭, 而是把该数据库连接会归还到数据库连接池中.
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
