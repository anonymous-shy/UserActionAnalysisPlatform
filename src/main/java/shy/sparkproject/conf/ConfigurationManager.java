package shy.sparkproject.conf;

import java.io.InputStream;
import java.util.Properties;

/**
 * 配置管理组件
 * Created by Shy on 2016/6/29.
 */
public class ConfigurationManager {

    private static Properties props = new Properties();

    /**
     * 静态代码块：第一次初始化就加载
     */
    static {
        try {
            InputStream inputStream =
                    ConfigurationManager.class.getClassLoader().getResourceAsStream("project.properties");
            props.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return props.getProperty(key);
    }
}
