package shy.sparkproject.dao;

import shy.sparkproject.domain.Task;

import java.sql.SQLException;

/**
 * Created by Shy on 2016/6/29.
 */
public interface ITaskDao {

    /**
     * 根据 taskId 查找任务
     *
     * @param taskId
     * @return Task
     */
    Task findById(Long taskId) throws SQLException;
}
