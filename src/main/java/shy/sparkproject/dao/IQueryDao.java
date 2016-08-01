package shy.sparkproject.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO查询接口
 * Created by Shy on 2016/6/28.
 *
 * @param <T>: DAO处理的实体类的类型
 */
public interface IQueryDao<T> {

    /**
     * INSERT, UPDATE, DELETE
     *
     * @param connection
     * @param sql
     * @param args       填充占位符可变参数
     */
    void update(Connection connection, String sql, Object... args) throws SQLException;

    /**
     * @param connection
     * @param sql
     * @param args
     * @return T 对象
     */
    T get(Connection connection, String sql, Object... args) throws SQLException;

    /**
     * @param connection
     * @param sql
     * @param args
     * @return T 的 list 集合
     */
    List<T> getList(Connection connection, String sql, Object... args) throws SQLException;

    /**
     * @param connection
     * @param sql
     * @param args
     * @param <E>
     * @return 一个具体的值
     */
    <E> E getValue(Connection connection, String sql, Object... args) throws SQLException;

    /**
     * 批量处理
     * @param connection
     * @param sql
     * @param args 填充占位符的Object[]
     */
    void batch(Connection connection, String sql, Object[]... args) throws SQLException;
}
