package shy.sparkproject;

import shy.sparkproject.conf.ConfigurationManager;
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
        /*String sql = "select * from emp where empno = ?";
        Emp query = queryRunner.query(connection, sql, new BeanHandler<Emp>(Emp.class), 7839);
        System.out.print(query);*/

        String sql = "select * from emp";
        List list = new JDBCDAOImpl().getList(connection, sql);
        System.out.print(list);
//        String sql = "insert into dept values(?,?,?)";
//        new JDBCDAOImpl().update(connection, sql, 50, "test", "test");
    }
}
