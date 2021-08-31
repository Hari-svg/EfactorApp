package com.sravan.efactorapp.utils.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.sravan.efactorapp.utils.DataBase.Model.DaoMaster;


public class DBOpenHelper extends DaoMaster.OpenHelper {
    public DBOpenHelper(Context context, String name) {
        super(context, name);
    }

    @Override // org.greenrobot.greendao.database.DatabaseOpenHelper
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);

    }


}
