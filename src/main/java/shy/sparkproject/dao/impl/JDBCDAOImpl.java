package shy.sparkproject.dao.impl;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import shy.sparkproject.dao.IQueryDao;
import shy.sparkproject.utils.ReflectionUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * 使用 QueryRunner 提供实现
 * Created by Shy on 2016/6/28.
 */
public class JDBCDAOImpl<T> implements IQueryDao<T> {

    private QueryRunner queryRunner = null;
    private Class<T> type;

    public JDBCDAOImpl() {
        queryRunner = new QueryRunner();
        type = ReflectionUtils.getSuperGenericType(getClass());
    }

    public void update(Connection connection, String sql, Object... args) {
        try {
            queryRunner.update(connection, sql, args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public T get(Connection connection, String sql, Object... args) {
        try {
            return queryRunner.query(connection, sql, new BeanHandler<T>(type), args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List getList(Connection connection, String sql, Object... args) {
        try {
            return queryRunner.query(connection, sql, new BeanListHandler<T>(type), args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void batch(Connection connection, String sql, Object[]... args) throws SQLException {
        queryRunner.batch(connection, sql, args);
    }

    public <E> E getValue(Connection connection, String sql, Object... args) throws SQLException {
        return (E) queryRunner.query(connection, sql, new ScalarHandler(), args);
    }
}
