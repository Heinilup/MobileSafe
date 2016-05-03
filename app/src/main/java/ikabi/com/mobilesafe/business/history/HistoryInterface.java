package ikabi.com.mobilesafe.business.history;

import android.content.Context;

import com.huangjiang.dao.DFile;
import com.huangjiang.dao.DFileDao;
import com.huangjiang.dao.DaoMaster;
import com.huangjiang.utils.Logger;

import java.util.List;

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
