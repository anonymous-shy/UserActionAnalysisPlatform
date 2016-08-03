package shy.sparkproject;

import shy.sparkproject.conf.ConfigurationManager;
import shy.sparkproject.dao.factory.DaoFactory;
import shy.sparkproject.dao.impl.JDBCDAOImpl;
import shy.sparkproject.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by AnonYmous_shY on 2016/8/3.
 */
public class Test {

    @org.junit.Test
    public void testConf() {
        ConfigurationManager cm = new ConfigurationManager();
        System.out.println(cm.getProperty("simple-app.answer"));
        System.out.println(cm.getProperty("simple-lib.foo"));
        System.out.println(cm.getProperty("simple-lib.whatever"));
    }

    @org.junit.Test
    public void testJdbsUtils() {
        try {
            Connection connection = JDBCUtils.getConnection();
            System.out.print(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @org.junit.Test
    public void testJDBC() throws SQLException {
        Connection connection = JDBCUtils.getConnection();
//        String sql = "select * from world.country where code = ?";
        String sql = "select * from task where task_id = ?";
        new JDBCDAOImpl().get(connection, sql, 1);
    }

    @org.junit.Test
    public void testDao() throws SQLException {
        System.out.print(DaoFactory.getTaskDao().findById(1L));
    }
}
