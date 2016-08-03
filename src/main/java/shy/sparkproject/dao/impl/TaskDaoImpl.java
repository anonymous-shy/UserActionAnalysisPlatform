package shy.sparkproject.dao.impl;

import shy.sparkproject.dao.ITaskDao;
import shy.sparkproject.domain.Task;
import shy.sparkproject.utils.JDBCUtils;

import java.sql.SQLException;

/**
 * Created by Shy on 2016/6/29.
 */
public class TaskDaoImpl extends JDBCDAOImpl<Task> implements ITaskDao {

    TaskDaoImpl taskDao = new TaskDaoImpl();

    public Task findById(Long taskId) throws SQLException {
        String sql = "select * from task where task_id = ?";
            Task task = taskDao.get(JDBCUtils.getConnection(), sql, taskId);
            return task;
    }
}
