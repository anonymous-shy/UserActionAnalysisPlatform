package shy.sparkproject.dao.impl;

import shy.sparkproject.dao.ITaskDao;
import shy.sparkproject.domain.Task;
import shy.sparkproject.utils.JDBCUtils;

import java.sql.SQLException;

/**
 * Created by Shy on 2016/6/29.
 */
public class TaskDaoImpl extends JDBCDAOImpl<Task> implements ITaskDao {

    @Override
    public Task findById(Integer taskId) {
        String sql = "SELECT * FROM uaap.`task` WHERE task_id = ?";
        return get(JDBCUtils.getConnection(), sql, taskId);
    }

}
