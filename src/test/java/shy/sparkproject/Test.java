package shy.sparkproject;

import shy.sparkproject.conf.ConfigurationManager;
import shy.sparkproject.dao.factory.DaoFactory;
import shy.sparkproject.domain.Task;

import java.sql.SQLException;

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
    public void testDao() throws SQLException {
        Task task = DaoFactory.getTaskDao().findById(1);

        System.out.print(task);
    }

}
