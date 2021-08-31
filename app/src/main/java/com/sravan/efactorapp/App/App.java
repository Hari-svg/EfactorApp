package com.sravan.efactorapp.App;

import android.app.Application;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;

import com.polidea.rxandroidble2.RxBleClient;
import com.sravan.efactorapp.BLE.BLECommHandler;
import com.sravan.efactorapp.R;
import com.sravan.efactorapp.spf.SessionManager;
import com.sravan.efactorapp.utils.DataBase.DatabaseHandler;
import com.sravan.efactorapp.utils.DataBase.Model.DaoSession;
import com.sravan.efactorapp.utils.MDNSQueryHandler;
import com.sravan.efactorapp.utils.ReachabilityHandler;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by ubuntu on 6/12/17.
 */

public class App extends Application {

    private static EventBus eventBus = null;
    private static RxBleClient rxBleClient;
    private DaoSession daoSession;

    public void onCreate() {
        super.onCreate();
        eventBus = EventBus.getDefault();
        rxBleClient = RxBleClient.create(this);
        DatabaseHandler.init( this,"efactor");
        //     DaoMaster.DevOpenHelper helper= new DaoMaster.DevOpenHelper(this,"factor.db",null);
        //    Database db=helper.getWritableDb();
//        DaoMaster.createAllTables(this.daoSession.getDatabase(), true);
//        DatabaseHandler.getInstance().clearAll();
        // DaoMaster.createAllTables(DB_Name, true);
        MDNSQueryHandler.init(this);
        ReachabilityHandler.init(this);

       // CloudHandler.init(getApplicationContext());
        BLECommHandler.init(this);
    }

    public static EventBus getEventBus() {
        return eventBus;
    }

    public static RxBleClient getBleClient() {
        return rxBleClient;
    }
}
