package com.sravan.efactorapp.utils.DataBase.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.greenrobot.greendao.AbstractDaoMaster;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseOpenHelper;
import org.greenrobot.greendao.database.StandardDatabase;
import org.greenrobot.greendao.identityscope.IdentityScopeType;

public class DaoMaster extends AbstractDaoMaster {
    public static final int SCHEMA_VERSION = 1;

    public static void createUser(Database db, boolean ifNotExists) {

        //    GatewayDao.createTable(db, ifNotExists);

        UserDao.createTable(db, ifNotExists);

        // DeviceDao.createTable(db, ifNotExists);
        //  delay(2000);
    }
    public static void createGateway(Database db, boolean ifNotExists) {

        GatewayDao.createTable(db, ifNotExists);


    }
    public static void createDevice(Database db, boolean ifNotExists) {



        DeviceDao.createTable(db, ifNotExists);

    }

    private static void delay(int i) {
    }

    public static void dropAllTables(Database db, boolean ifExists) {
        DeviceDao.dropTable(db, ifExists);
        GatewayDao.dropTable(db, ifExists);
        UserDao.dropTable(db, ifExists);
    }

    public static DaoSession newDevSession(Context context, String name) {
        return new DaoMaster(new DevOpenHelper(context, name).getWritableDb()).newSession();
    }

    public DaoMaster(SQLiteDatabase db) {
        this(new StandardDatabase(db));
    }

    public DaoMaster(Database db) {
        super(db, 1);
        registerDaoClass(DeviceDao.class);
        registerDaoClass(GatewayDao.class);
        registerDaoClass(UserDao.class);
    }

    @Override // org.greenrobot.greendao.AbstractDaoMaster
    public DaoSession newSession() {
        return new DaoSession(this.db, IdentityScopeType.Session, this.daoConfigMap);

    }

    @Override // org.greenrobot.greendao.AbstractDaoMaster
    public DaoSession newSession(IdentityScopeType type) {
        return new DaoSession(this.db, type, this.daoConfigMap);
    }

    public static abstract class OpenHelper extends DatabaseOpenHelper {
        public OpenHelper(Context context, String name) {
            super(context, name, 1);
        }

        public OpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
            super(context, name, factory, 1);
        }

        @Override // org.greenrobot.greendao.database.DatabaseOpenHelper
        public void onCreate(Database db) {
            Log.i("greenDAO", "Creating tables for schema version 1");
            //DaoMaster.createAllTables(db, false);
            DaoMaster.createUser(db, false);
            DaoMaster.createGateway(db, false);
            DaoMaster.createDevice(db, false);


        }
    }

    public static class DevOpenHelper extends OpenHelper {
        public DevOpenHelper(Context context, String name) {
            super(context, name);
        }

        public DevOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
            super(context, name, factory);
        }

        @Override // org.greenrobot.greendao.database.DatabaseOpenHelper
        public void onUpgrade(Database db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            DaoMaster.dropAllTables(db, true);
            onCreate(db);
        }
    }
}
