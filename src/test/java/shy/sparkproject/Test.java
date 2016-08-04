package shy.sparkproject;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
        System.out.println(cm.getProperty("spark-ctx.master"));
        if (cm.getProperty("spark-ctx.master").equals("local"))
            System.out.println(cm.getProperty("spark-app.SESSION_AppName"));
    }

    @org.junit.Test
    public void testFastJSON() {
        String json = "{\"time\":\"2015-09-12T00:46:58.771Z\",\"channel\":\"#en.wikipedia\",\"cityName\":null,\"comment\":\"added project\",\"countryIsoCode\":null,\"countryName\":null,\"isAnonymous\":false,\"isMinor\":false,\"isNew\":false,\"isRobot\":false,\"isUnpatrolled\":false,\"metroCode\":null,\"namespace\":\"Talk\",\"page\":\"Talk:Oswald Tilghman\",\"regionIsoCode\":null,\"regionName\":null,\"user\":\"GELongstreet\",\"delta\":36,\"added\":36,\"deleted\":0}";
        JSONObject jsonObject = JSONArray.parseObject(json);
        System.out.println(jsonObject.getString("time"));
    }

    @org.junit.Test
    public void testDao() throws SQLException {
        Task task = DaoFactory.getTaskDao().findById(1);

        System.out.print(task);
    }

}
