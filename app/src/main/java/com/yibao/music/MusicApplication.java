package com.yibao.music;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.squareup.leakcanary.LeakCanary;
import com.yibao.music.model.greendao.DaoMaster;
import com.yibao.music.model.greendao.DaoSession;
import com.yibao.music.model.greendao.MusicBeanDao;
import com.yibao.music.model.greendao.MusicInfoDao;
import com.yibao.music.util.CrashHandler;
import com.yibao.music.util.RxBus;

/**
 * 作者：Stran on 2017/3/23 15:12
 * 描述：${TODO}
 * 邮箱：strangermy@outlook.com
 *
 * @author Stran
 */
public class MusicApplication
        extends Application {
    private static MusicApplication appContext;
    public static boolean isShowLog = true;

    private RxBus mRxBus;

    private DaoSession mDaoSession;
    private static MusicBeanDao musicBeanDao;

    public static MusicApplication getIntstance() {
        if (appContext == null) {
            appContext = new MusicApplication();
        }
        return appContext;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
        appContext = this;
        CrashHandler.getInstance()
                .init(this);
        setUpDataBase();
        mRxBus = new RxBus();
    }

    private void setUpDataBase() {
        DaoMaster.DevOpenHelper mHelper = new DaoMaster.DevOpenHelper(this, "favorite-db", null);
        SQLiteDatabase db = mHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        mDaoSession = daoMaster.newSession();


    }

    public MusicBeanDao getMusicDao() {
        if (musicBeanDao == null) {
            synchronized ("MusicApplication.class") {
                if (musicBeanDao == null) {
                    musicBeanDao = mDaoSession.getMusicBeanDao();
                }
            }
        }
        return musicBeanDao;

    }

    public MusicInfoDao getMusicInfoDao() {
        return mDaoSession.getMusicInfoDao();
    }

    public RxBus bus() {
        return mRxBus;
    }


}
