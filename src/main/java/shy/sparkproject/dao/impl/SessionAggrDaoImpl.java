package shy.sparkproject.dao.impl;

import shy.sparkproject.dao.ISessionAggrDao;
import shy.sparkproject.domain.SessionAggrRate;
import shy.sparkproject.utils.JDBCUtils;

/**
 * Created by AnonYmous_shY on 2016/8/18.
 */
public class SessionAggrDaoImpl extends JDBCDAOImpl<SessionAggrRate> implements ISessionAggrDao {

    @Override
    public void insert(SessionAggrRate sessionAggrRate) {
        String sql = "insert into table uaap.`session_aggr_rate` values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        update(JDBCUtils.getConnection(), sql, sessionAggrRate.getTask_Id(), sessionAggrRate.getSession_Count(),
                sessionAggrRate.getVisit_Length_1s_3s_rate(), sessionAggrRate.getVisit_Length_4s_6s_rate(),
                sessionAggrRate.getVisit_Length_7s_9s_rate(), sessionAggrRate.getVisit_Length_10s_30s_rate(),
                sessionAggrRate.getVisit_Length_30s_60s_rate(), sessionAggrRate.getVisit_Length_1m_3m_rate(),
                sessionAggrRate.getVisit_Length_3m_10m_rate(), sessionAggrRate.getVisit_Length_10m_30m_rate(),
                sessionAggrRate.getVisit_Length_30m_rate(), sessionAggrRate.getStep_Length_1_3_rate(),
                sessionAggrRate.getStep_Length_4_6_rate(), sessionAggrRate.getStep_Length_7_9_rate(),
                sessionAggrRate.getStep_Length_10_30_rate(), sessionAggrRate.getStep_Length_30_60_rate(),
                sessionAggrRate.getStep_Length_60_rate()
        );
    }
}
