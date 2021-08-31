package com.sravan.efactorapp.utils.DataBase.Model;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

public class GatewayDao extends AbstractDao<Gateway, Long> {
    public static final String TABLENAME = "GATEWAY";

    public static class Properties {
        public static final Property Apn = new Property(5, String.class, "apn", false, "APN");
        public static final Property Feature_data = new Property(17, String.class, "feature_data", false, "FEATURE_DATA");
        public static final Property Fw_ver = new Property(13, String.class, "fw_ver", false, "FW_VER");
        public static final Property Gateway_id = new Property(1, String.class, "gateway_id", false, "GATEWAY_ID");
        public static final Property Id = new Property(0, Long.class, "id", true, "_id");
        public static final Property Is_deleted = new Property(19, Boolean.TYPE, "is_deleted", false, "IS_DELETED");
        public static final Property Latitude = new Property(8, Double.TYPE, "latitude", false, "LATITUDE");
        public static final Property Longitude = new Property(9, Double.TYPE, "longitude", false, "LONGITUDE");
        public static final Property Mac = new Property(4, String.class, "mac", false, "MAC");
        public static final Property Manufacturer = new Property(16, String.class, "manufacturer", false, "MANUFACTURER");
        public static final Property Model_desc = new Property(14, String.class, "model_desc", false, "MODEL_DESC");
        public static final Property Model_name = new Property(15, String.class, "model_name", false, "MODEL_NAME");
        public static final Property Model_version = new Property(12, String.class, "model_version", false, "MODEL_VERSION");
        public static final Property Name = new Property(3, String.class, "name", false, "NAME");
        public static final Property Secret_key = new Property(6, String.class, "secret_key", false, "SECRET_KEY");
        public static final Property Status_data = new Property(18, String.class, "status_data", false, "STATUS_DATA");
        public static final Property Timezone = new Property(10, String.class, "timezone", false, "TIMEZONE");
        public static final Property Type = new Property(7, String.class, "type", false, "TYPE");
        public static final Property Updated_at = new Property(2, Long.TYPE, "updated_at", false, "UPDATED_AT");
        public static final Property Wifi_ssid = new Property(11, String.class, "wifi_ssid", false, "WIFI_SSID");
    }

    public GatewayDao(DaoConfig config) {
        super(config);
    }

    public GatewayDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists ? "IF NOT EXISTS " : "";
        db.execSQL("CREATE TABLE " + constraint + "\"GATEWAY\" (\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," +
                "\"GATEWAY_ID\" TEXT NOT NULL ," +
                "\"UPDATED_AT\" INTEGER NOT NULL ," +
                "\"NAME\" TEXT,\"MAC\" TEXT," +
                "\"APN\" TEXT," +
                "\"SECRET_KEY\" TEXT," +
                "\"TYPE\" TEXT," +
                "\"LATITUDE\" REAL NOT NULL ," +
                "\"LONGITUDE\" REAL NOT NULL ," +
                "\"TIMEZONE\" TEXT,\"WIFI_SSID\" TEXT," +
                "\"MODEL_VERSION\" TEXT,\"FW_VER\" TEXT," +
                "\"MODEL_DESC\" TEXT,\"MODEL_NAME\" TEXT," +
                "\"MANUFACTURER\" TEXT," +
                "\"FEATURE_DATA\" TEXT," +
                "\"STATUS_DATA\" TEXT," +
                "\"IS_DELETED\" INTEGER NOT NULL );");
        db.execSQL("CREATE UNIQUE INDEX " + constraint + "IDX_GATEWAY_GATEWAY_ID ON \"GATEWAY\" (\"GATEWAY_ID\" ASC);");
    }

    public static void dropTable(Database db, boolean ifExists) {
        StringBuilder sb = new StringBuilder();
        sb.append("DROP TABLE ");
        sb.append(ifExists ? "IF EXISTS " : "");
        sb.append("\"GATEWAY\"");
        db.execSQL(sb.toString());
    }

    /* access modifiers changed from: protected */
    public final void bindValues(DatabaseStatement stmt, Gateway entity) {
        stmt.clearBindings();
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id.longValue());
        }
        stmt.bindString(2, entity.getGateway_id());
        stmt.bindLong(3, entity.getUpdated_at());
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(4, name);
        }
        String mac = entity.getMac();
        if (mac != null) {
            stmt.bindString(5, mac);
        }
        String apn = entity.getApn();
        if (apn != null) {
            stmt.bindString(6, apn);
        }
        String secret_key = entity.getSecret_key();
        if (secret_key != null) {
            stmt.bindString(7, secret_key);
        }
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(8, type);
        }
        stmt.bindDouble(9, entity.getLatitude());
        stmt.bindDouble(10, entity.getLongitude());
        String timezone = entity.getTimezone();
        if (timezone != null) {
            stmt.bindString(11, timezone);
        }
        String wifi_ssid = entity.getWifi_ssid();
        if (wifi_ssid != null) {
            stmt.bindString(12, wifi_ssid);
        }
        String model_version = entity.getModel_version();
        if (model_version != null) {
            stmt.bindString(13, model_version);
        }
        String fw_ver = entity.getFw_ver();
        if (fw_ver != null) {
            stmt.bindString(14, fw_ver);
        }
        String model_desc = entity.getModel_desc();
        if (model_desc != null) {
            stmt.bindString(15, model_desc);
        }
        String model_name = entity.getModel_name();
        if (model_name != null) {
            stmt.bindString(16, model_name);
        }
        String manufacturer = entity.getManufacturer();
        if (manufacturer != null) {
            stmt.bindString(17, manufacturer);
        }
        String feature_data = entity.getFeature_data();
        if (feature_data != null) {
            stmt.bindString(18, feature_data);
        }
        String status_data = entity.getStatus_data();
        if (status_data != null) {
            stmt.bindString(19, status_data);
        }
        stmt.bindLong(20, entity.getIs_deleted() ? 1 : 0);
    }

    /* access modifiers changed from: protected */
    public final void bindValues(SQLiteStatement stmt, Gateway entity) {
        stmt.clearBindings();
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id.longValue());
        }
        stmt.bindString(2, entity.getGateway_id());
        stmt.bindLong(3, entity.getUpdated_at());
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(4, name);
        }
        String mac = entity.getMac();
        if (mac != null) {
            stmt.bindString(5, mac);
        }
        String apn = entity.getApn();
        if (apn != null) {
            stmt.bindString(6, apn);
        }
        String secret_key = entity.getSecret_key();
        if (secret_key != null) {
            stmt.bindString(7, secret_key);
        }
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(8, type);
        }
        stmt.bindDouble(9, entity.getLatitude());
        stmt.bindDouble(10, entity.getLongitude());
        String timezone = entity.getTimezone();
        if (timezone != null) {
            stmt.bindString(11, timezone);
        }
        String wifi_ssid = entity.getWifi_ssid();
        if (wifi_ssid != null) {
            stmt.bindString(12, wifi_ssid);
        }
        String model_version = entity.getModel_version();
        if (model_version != null) {
            stmt.bindString(13, model_version);
        }
        String fw_ver = entity.getFw_ver();
        if (fw_ver != null) {
            stmt.bindString(14, fw_ver);
        }
        String model_desc = entity.getModel_desc();
        if (model_desc != null) {
            stmt.bindString(15, model_desc);
        }
        String model_name = entity.getModel_name();
        if (model_name != null) {
            stmt.bindString(16, model_name);
        }
        String manufacturer = entity.getManufacturer();
        if (manufacturer != null) {
            stmt.bindString(17, manufacturer);
        }
        String feature_data = entity.getFeature_data();
        if (feature_data != null) {
            stmt.bindString(18, feature_data);
        }
        String status_data = entity.getStatus_data();
        if (status_data != null) {
            stmt.bindString(19, status_data);
        }
        stmt.bindLong(20, entity.getIs_deleted() ? 1 : 0);
    }

    @Override // org.greenrobot.greendao.AbstractDao
    public Long readKey(Cursor cursor, int offset) {
        if (cursor.isNull(offset + 0)) {
            return null;
        }
        return Long.valueOf(cursor.getLong(offset + 0));
    }

    @Override // org.greenrobot.greendao.AbstractDao
    public Gateway readEntity(Cursor cursor, int offset) {
        return new Gateway(cursor.isNull(offset + 0) ? null : Long.valueOf(cursor.getLong(offset + 0)), cursor.getString(offset + 1), cursor.getLong(offset + 2), cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), cursor.getDouble(offset + 8), cursor.getDouble(offset + 9), cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), cursor.getShort(offset + 19) != 0);
    }

    public void readEntity(Cursor cursor, Gateway entity, int offset) {
        String str = null;
        entity.setId(cursor.isNull(offset + 0) ? null : Long.valueOf(cursor.getLong(offset + 0)));
        entity.setGateway_id(cursor.getString(offset + 1));
        entity.setUpdated_at(cursor.getLong(offset + 2));
        entity.setName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setMac(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setApn(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setSecret_key(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setType(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setLatitude(cursor.getDouble(offset + 8));
        entity.setLongitude(cursor.getDouble(offset + 9));
        entity.setTimezone(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setWifi_ssid(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setModel_version(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setFw_ver(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setModel_desc(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setModel_name(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setManufacturer(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setFeature_data(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        if (!cursor.isNull(offset + 18)) {
            str = cursor.getString(offset + 18);
        }
        entity.setStatus_data(str);
        entity.setIs_deleted(cursor.getShort(offset + 19) != 0);
    }

    /* access modifiers changed from: protected */
    public final Long updateKeyAfterInsert(Gateway entity, long rowId) {
        entity.setId(Long.valueOf(rowId));
        return Long.valueOf(rowId);
    }

    public Long getKey(Gateway entity) {
        if (entity != null) {
            return entity.getId();
        }
        return null;
    }

    public boolean hasKey(Gateway entity) {
        return entity.getId() != null;
    }

    /* access modifiers changed from: protected */
    @Override // org.greenrobot.greendao.AbstractDao
    public final boolean isEntityUpdateable() {
        return true;
    }
}
