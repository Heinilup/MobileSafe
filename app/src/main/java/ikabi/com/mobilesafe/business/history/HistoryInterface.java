package ikabi.com.mobilesafe.business.history;

import android.content.Context;

import java.util.List;

import ikabi.com.mobilesafe.dao.DFile;
import ikabi.com.mobilesafe.dao.DFileDao;
import ikabi.com.mobilesafe.dao.DaoMaster;
import ikabi.com.mobilesafe.utils.Logger;

/**
 * 历史消息
 */
public class HistoryInterface {

    private Logger logger = Logger.getLogger(HistoryInterface.class);

    private Context mContext;
    private DFileDao dFileDao;

    public HistoryInterface(Context context) {
        this.mContext = context;
        dFileDao = DaoMaster.getInstance().newSession().getDFileDao();
    }

    public List<DFile> getHistory() {
        return dFileDao.queryBuilder().orderDesc(DFileDao.Properties.Id).list();
    }
}
