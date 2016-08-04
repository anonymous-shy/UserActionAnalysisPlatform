package shy.sparkproject.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.hive.HiveContext;
import shy.sparkproject.conf.ConfigurationManager;

/**
 * 用户访问session分析
 * Created by Shy on 2016/6/27.
 */
public class UserSessionAnalysis {

    public static void main(String[] args) {
        ConfigurationManager cm = new ConfigurationManager();
        SparkConf conf = new SparkConf()
                .setAppName(cm.getProperty("spark-app.SESSION_AppName"))
                .setMaster(cm.getProperty("spark-ctx.local"));
        JavaSparkContext sc = new JavaSparkContext(conf);
        SQLContext sqlContext = getSQLContext(sc.sc(), cm.getProperty("spark-ctx.local"));

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
        if (mode == "local")
            return new SQLContext(sc);
        else return new HiveContext(sc);
    }
}
