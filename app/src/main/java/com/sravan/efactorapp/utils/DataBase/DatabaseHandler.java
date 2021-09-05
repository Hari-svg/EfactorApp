package com.sravan.efactorapp.utils.DataBase;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sravan.efactorapp.spf.SessionManager;
import com.sravan.efactorapp.utils.Constants;
import com.sravan.efactorapp.utils.DataBase.Model.DaoMaster;
import com.sravan.efactorapp.utils.DataBase.Model.DaoSession;
import com.sravan.efactorapp.utils.DataBase.Model.Device;
import com.sravan.efactorapp.utils.DataBase.Model.DeviceDao;
import com.sravan.efactorapp.utils.DataBase.Model.Gateway;
import com.sravan.efactorapp.utils.DataBase.Model.GatewayDao;
import com.sravan.efactorapp.utils.DataBase.Model.User;
import com.sravan.efactorapp.utils.DataBase.Model.UserDao;

import org.greenrobot.greendao.query.WhereCondition;

import java.util.HashMap;
import java.util.List;

public class DatabaseHandler {
    private static final String DB_NAME = "factor.dbdemo";
    private static final String IS_FIRST_TIME = "IS_FIRST_TIME";
    private static final String LOCAL_IP_KEY = "com.efactor.factor_app.localip";
    private static final String SHARED_PREF = "com.efactor.factor_app";
    private static final String TAG = DatabaseHandler.class.getSimpleName();
    private static DatabaseHandler instance = null;
    private SessionManager sessionManager;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private DeviceDao deviceDao;
    private GatewayDao gatewayDao;
    private UserDao userDao;
    private SharedPreferences sharedPreferences;
   /* private DaoSession daoSession = this.daoMaster.newSession();
  private DeviceDao deviceDao = this.daoSession.getDeviceDao();
    private GatewayDao gatewayDao = this.daoSession.getGatewayDao();

    private UserDao userDao = this.daoSession.getUserDao();
    */

    public static void init(Context context, String password) {
        if (instance == null) {
            instance = new DatabaseHandler(context, password);
        }
    }

    public static DatabaseHandler getInstance() {
        return instance;
    }

    private DatabaseHandler(Context context, String password) {
        this.sharedPreferences = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        sessionManager=new SessionManager(context.getApplicationContext());
//       this.daoMaster = new DaoMaster(new DBOpenHelper(context, DB_NAME).getEncryptedWritableDb(password));
       /*  daoSession = this.daoMaster.newSession();
         deviceDao = this.daoSession.getDeviceDao();
         gatewayDao = this.daoSession.getGatewayDao();
         userDao = this.daoSession.getUserDao();*/
        this.daoMaster = new DaoMaster(new DBOpenHelper(context, DB_NAME).getWritableDb());


        //  DaoMaster.DevOpenHelper helper= new DaoMaster.DevOpenHelper(Context,DB_NAME,null);
        //   Database db=helper.getWritableDb();

        daoSession = this.daoMaster.newSession();
        deviceDao = this.daoSession.getDeviceDao();
        gatewayDao = this.daoSession.getGatewayDao();
        userDao = this.daoSession.getUserDao();

    }

    public void clearAll() {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();

        editor.clear();
        editor.apply();
        deleteAllGateways();
        deleteAllDevices();
        DaoMaster daoMaster2 = this.daoMaster;
        DaoMaster.dropAllTables(this.daoSession.getDatabase(), true);
        DaoMaster daoMaster3 = this.daoMaster;
        DaoMaster.createUser(this.daoSession.getDatabase(), true);
        DaoMaster.createGateway(this.daoSession.getDatabase(), true);
        DaoMaster.createDevice(this.daoSession.getDatabase(), true);

        this.daoSession.clear();
    }

    public void clearSession() {
        this.daoSession.clear();
    }

    public User getUser() {
        List<User> _list = this.userDao.loadAll();
        if (_list == null || _list.size() == 0) {
            return null;
        }
        return _list.get(0);
    }

    public void addUser(User user) {
        this.userDao.insert(user);
    }

    public void updateUser(User user) {
        this.userDao.update(user);
    }

    public void addGateway(Gateway gateway) {
        this.gatewayDao.insert(gateway);
    }

    public List<Gateway> getGateways() {
        return this.gatewayDao.loadAll();
    }

    public List<Gateway> getGateways(boolean isDeleted) {
        return this.gatewayDao.queryBuilder().where(GatewayDao.Properties.Is_deleted.eq(isDeleted ? Boolean.TRUE : Boolean.FALSE), new WhereCondition[0]).list();
    }

    public Gateway getGateway(String gatewayId) {
        return this.gatewayDao.queryBuilder().where(GatewayDao.Properties.Gateway_id.eq(gatewayId), new WhereCondition[0]).unique();
    }

    public Gateway getGatewayFromMAC(String mac) {
        return this.gatewayDao.queryBuilder().where(GatewayDao.Properties.Mac.eq(mac), new WhereCondition[0]).unique();
    }

    public void insertGateway(Gateway gateway) {
        Gateway g = getGateway(gateway.getGateway_id());
        if (g == null) {
            addGateway(gateway);
        } else if (g.getUpdated_at() < gateway.getUpdated_at()) {
            deleteGateway(g);
            addGateway(gateway);
        } else {
            g.setIs_deleted(false);
            updateGateway(g);
        }
    }

    public void deleteGateway(Gateway gateway) {
       /* List<Device> deviceList = this.deviceDao.queryBuilder().where(DeviceDao.Properties.Gateway_id.eq(gateway.getGateway_id()), new WhereCondition[0]).list();
        if (deviceList != null) {
            for (Device device : deviceList) {
                device.setGateway_id(Constants.NONE_GATEWAY_VALUE);
                updateDevice(device);
            }
        }*/
        this.gatewayDao.delete(gateway);
        this.daoSession.clear();
    }

    public void updateGateway(Gateway gateway) {
        this.gatewayDao.update(gateway);
    }

    public void removeDeletedGateways() {
        this.gatewayDao.queryBuilder().where(GatewayDao.Properties.Is_deleted.eq(Boolean.TRUE), new WhereCondition[0]).buildDelete().executeDeleteWithoutDetachingEntities();
        this.daoSession.clear();
    }

    public void deleteAllGateways() {
        List<Gateway> gatewayList = this.gatewayDao.loadAll();
        if (gatewayList != null) {
            for (Gateway gateway : gatewayList) {
                deleteGateway(gateway);
            }
        }
    }

    public boolean isGatewayFound() {
        return this.deviceDao.count() != 0;
    }

    public List<Device> getDevices() {
        return this.deviceDao.loadAll();
    }

    public List<Device> getDevices(boolean isDeleted) {
        return this.deviceDao.queryBuilder().where(DeviceDao.Properties.Is_deleted.eq(isDeleted ? Boolean.TRUE : Boolean.FALSE), new WhereCondition[0]).list();
    }

    public Device getDevice(String deviceId) {
        return this.deviceDao.queryBuilder().where(DeviceDao.Properties.Device_id.eq(deviceId), new WhereCondition[0]).unique();
    }

    public List<Device> getDevices(String gatewayId) {
        return this.deviceDao.queryBuilder().where(DeviceDao.Properties.Gateway_id.eq(gatewayId), new WhereCondition[0]).list();
    }

    public List<Device> getActiveDevices() {

        return this.deviceDao.queryBuilder().where(this.deviceDao.queryBuilder().and(DeviceDao.Properties.Is_deleted.eq(Boolean.FALSE), DeviceDao.Properties.Gateway_id.notEq(Constants.NONE_GATEWAY_VALUE), new WhereCondition[0]), new WhereCondition[0]).list();

    }

    public void deleteDevice(Device device) {
        this.deviceDao.delete(device);
    }

    public void addDevice(Device device) {
        this.deviceDao.insert(device);
    }

    public void insertDevice(Device device) {
        Device d = getDevice(device.getDevice_id());
        if (d == null) {
            addDevice(device);
            return;
        }
        if (d.getStatus_updated_at() > device.getStatus_updated_at()) {
            device.setStatus_data(d.getStatus_data());
            device.setStatus_updated_at(d.getStatus_updated_at());
        }
        if (d.getUpdated_at() != device.getUpdated_at()) {
            deleteDevice(d);
            addDevice(device);
            return;
        }
        d.setIs_deleted(false);
        updateDevice(d);
    }

    public void updateDevice(Device device) {
        this.deviceDao.update(device);
    }

    public void removeDeletedDevices() {
        this.deviceDao.queryBuilder().where(DeviceDao.Properties.Is_deleted.eq(Boolean.TRUE), new WhereCondition[0]).buildDelete().executeDeleteWithoutDetachingEntities();
        this.daoSession.clear();
    }

    public void deleteAllDevices() {
        List<Device> deviceList = this.deviceDao.loadAll();
        if (deviceList != null) {
            for (Device device : deviceList) {
                deleteDevice(device);
            }
        }
    }

    public boolean isDeviceFound() {
        String str = TAG;
        Log.d(str, "Device Count: " + this.deviceDao.count());
        return this.deviceDao.count() != 0;
    }

    public void saveLocalIPDetails(HashMap<String, String> map) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(LOCAL_IP_KEY, new Gson().toJson(map));
        editor.apply();
    }

    public HashMap<String, String> restoreLocalIPDetails() {
        String data = this.sharedPreferences.getString(LOCAL_IP_KEY, null);

        if (data == null) {
            return null;
        }
        return (HashMap) new Gson().fromJson(data, new TypeToken<HashMap<String, String>>() {

        }.getType());
    }
}
