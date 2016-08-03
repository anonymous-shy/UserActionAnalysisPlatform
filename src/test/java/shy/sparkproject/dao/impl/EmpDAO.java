package shy.sparkproject.dao.impl;

import shy.sparkproject.utils.JDBCUtils;

/**
 * Created by Shy on 2016/6/28.
 */
public class EmpDAO extends JDBCDAOImpl<Emp> {

    public Emp findById(Integer id) {
        String sql = "select * from test.emp where empno = ?";
        Emp emp = get(JDBCUtils.getConnection(), sql, id);
        return emp;
    }
}
