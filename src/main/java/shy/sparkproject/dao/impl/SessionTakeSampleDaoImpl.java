package shy.sparkproject.dao.impl;

import shy.sparkproject.dao.ISessionTakeSampleDao;
import shy.sparkproject.domain.SessionTakeSample;
import shy.sparkproject.utils.JDBCUtils;

/**
 * Created by AnonYmous_shY on 2016/8/24.
 */
public class SessionTakeSampleDaoImpl extends JDBCDAOImpl implements ISessionTakeSampleDao {
    @Override
    public void insert(SessionTakeSample sessionTakeSample) {
        String sql = "insert into table uaap.`session_random_extract` values (?,?,?,?,?,?,?)";
        update(JDBCUtils.getConnection(), sql, sessionTakeSample.getTask_id(),
                sessionTakeSample.getSession_id(), sessionTakeSample.getStart_time(),
                sessionTakeSample.getEnd_time(), sessionTakeSample.getSearch_keywords(),
                sessionTakeSample.getClick_category_ids(), sessionTakeSample.getClick_product_ids());
    }
}
