package shy.sparkproject.dao.impl;

import org.junit.Test;
import shy.sparkproject.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Shy on 2016/6/28.
 */
public class EmpDAOTest {

    EmpDAO empDAO = new EmpDAO();

    @Test
    public void testGet() throws SQLException {
        Connection connection = JDBCUtils.getConnection();
        String sql = "select * from emp where empno = ?";
        Emp emp = empDAO.get(connection, sql, 7839);
        System.out.println(emp);
    }

    @Test
    public void testGetList() throws SQLException {
        Connection connection = JDBCUtils.getConnection();
        String sql = "select * from emp";
        List<Emp> list = empDAO.getList(connection, sql);
        for (Emp emp : list) {
            System.out.println(emp);
        }
    }
}