package shy.sparkproject;

import org.junit.Test;
import shy.sparkproject.dao.impl.JDBCDAOImpl;
import shy.sparkproject.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Shy on 2016/6/28.
 */
public class TestPro {

    @Test
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
