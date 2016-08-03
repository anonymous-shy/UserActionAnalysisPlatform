package shy.sparkproject.dao.impl;

import shy.sparkproject.dao.ITaskDao;
import shy.sparkproject.domain.Task;
import shy.sparkproject.utils.JDBCUtils;

import java.sql.SQLException;

/**
 * Created by Shy on 2016/6/29.
 */
public class TaskDaoImpl extends JDBCDAOImpl<Task> implements ITaskDao {

    public Task findById(Long taskId) {
        String sql = "select * from task where task_id = ?";
            Task task = super.get(JDBCUtils.getConnection(), sql, taskId);
            System.out.println(task);
            System.out.println("=================================");
            return task;

    }

    public Task findByName(String taskName) {
        String sql = "select * from task where task_name = ?";
        Task task = get(JDBCUtils.getConnection(), sql, taskName);
        return task;
    }
}
