package shy.sparkproject.dao.factory;

import shy.sparkproject.dao.ITaskDao;
import shy.sparkproject.dao.impl.TaskDaoImpl;

/**
 * Created by Shy on 2016/6/29.
 */
public class DaoFactory {

    public static ITaskDao getTaskDao() {
        return new TaskDaoImpl();
    }

}
