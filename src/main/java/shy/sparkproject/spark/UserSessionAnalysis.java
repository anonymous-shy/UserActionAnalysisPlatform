package shy.sparkproject.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.hive.HiveContext;
import shy.sparkproject.conf.ConfigurationManager;
import shy.sparkproject.utils.MockData;

/**
 * 用户访问session分析
 * Created by Shy on 2016/6/27.
 */
public class UserSessionAnalysis {

    public static void main(String[] args) {
        ConfigurationManager cm = new ConfigurationManager();
        SparkConf conf = new SparkConf()
                .setAppName(cm.getProperty("spark-app.SESSION_AppName"))
                .setMaster(cm.getProperty("spark-ctx.master"));
        JavaSparkContext sc = new JavaSparkContext(conf);
        SQLContext sqlContext = getSQLContext(sc.sc(), cm.getProperty("spark-ctx.master"));

        mockData(sc, sqlContext, cm.getProperty("spark-ctx.master"));

        sc.close();
    }

    /**
     * 获取SQLContext
     * 如果是local mode，返回SQLContext
     * 如果是cluster mode，返回HiveContext
     *
     * @param sc
     * @return SQLContext
     */
    private static SQLContext getSQLContext(SparkContext sc, String mode) {
        if ("local".equals(mode))
            return new SQLContext(sc);
        else return new HiveContext(sc);
    }

    private static void mockData(JavaSparkContext sc, SQLContext sqlContext, String mode) {
        if ("local".equals(mode)) {
            MockData.mock(sc, sqlContext);
        }
    }
}
