package shy.sparkproject.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SQLContext;

/**
 * Created by Shy on 2016/6/27.
 */
public class UserSessionAnalysis {

    public static void main(String[] args) {

        SparkConf conf = new SparkConf().setAppName("").setMaster("");
        JavaSparkContext sc = new JavaSparkContext(conf);
        SQLContext sqlContext = new SQLContext(sc);

        sc.close();
    }
}
