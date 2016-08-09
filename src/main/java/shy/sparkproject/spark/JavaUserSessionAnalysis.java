package shy.sparkproject.spark;

import com.alibaba.fastjson.JSONObject;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.hive.HiveContext;
import shy.sparkproject.conf.ConfigurationManager;
import shy.sparkproject.dao.ITaskDao;
import shy.sparkproject.dao.factory.DaoFactory;
import shy.sparkproject.domain.Task;
import shy.sparkproject.utils.MockData;
import shy.sparkproject.utils.ParamUtils;

/**
 * 用户访问session分析
 * Spark作业接收用户创建的任务，J2EE品台接收到用户创建的任务后，将记录插入task表中，
 * 参数以JSON格式封装在task_param字段中，接着J2EE调用Spark-submit shell脚本启动作业
 * 参数task_id，task_param会传递进spark main中
 * Created by Shy on 2016/6/27.
 */
public class JavaUserSessionAnalysis {

    public static void main(String[] args) {
        ConfigurationManager cm = new ConfigurationManager();
        SparkConf conf = new SparkConf()
                .setAppName(cm.getProperty("spark-app.SESSION_AppName"))
                .setMaster(cm.getProperty("spark-ctx.master"));
        JavaSparkContext sc = new JavaSparkContext(conf);
        SQLContext sqlContext = getSQLContext(sc.sc(), cm.getProperty("spark-ctx.master"));

        //模拟数据生成
        mockData(sc, sqlContext, cm.getProperty("spark-ctx.master"));

        /**
         * 按照session粒度进行聚合,从user_visit_action表中,查询出指定范围的行为数据
         * 1,时间范围过滤：起始时间-结束时间
         * 2,性别
         * 3,年龄范围
         * 4,城市(多选)
         * 5,搜索词：多个搜索词
         * 6,点击品类：多个品类
         */
        //拿到指定行为参数
        ITaskDao taskDao = DaoFactory.getTaskDao();
        Task task = taskDao.findById(ParamUtils.getTaskIdFromArgs(args, ""));
        JSONObject taskParam = JSONObject.parseObject(task.getTask_Param());



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
