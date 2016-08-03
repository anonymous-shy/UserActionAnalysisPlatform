package shy.sparkproject;

import shy.sparkproject.conf.ConfigurationManager;
import shy.sparkproject.dao.ITaskDao;
import shy.sparkproject.dao.factory.DaoFactory;
import shy.sparkproject.dao.impl.JDBCDAOImpl;
import shy.sparkproject.dao.impl.TaskDaoImpl;
import shy.sparkproject.domain.Task;
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

        Connection connection = JDBCUtils.getConnection();
        System.out.print(connection);

    }

    @org.junit.Test
    public void testJDBC() throws SQLException {
        Connection connection = JDBCUtils.getConnection();
//        String sql = "select * from world.country where code = ?";
        String sql = "select * from task where task_id = ?";
//        new JDBCDAOImpl().get(connection, sql, 1);
        JDBCDAOImpl<Task> taskJDBCDAO = new JDBCDAOImpl<Task>();
        Task task = taskJDBCDAO.get(connection, sql, 1);
        System.out.print(task.getTaskId() + " : " + task.getTaskName());
    }

    @org.junit.Test
    public void testDao() throws SQLException {
//        Task task = DaoFactory.getTaskDao().findByName("Test");
        ITaskDao taskDao = new TaskDaoImpl();
        Task task = taskDao.findByName("Test");
        System.out.print(task.getTaskId() + " : " + task.getTaskName());
    }

}
