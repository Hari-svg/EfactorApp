package com.sravan.efactorapp.utils.DataBase.Model;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

public class DeviceDao extends AbstractDao<Device, Long> {
    public static final String TABLENAME = "DEVICE";

    public static class Properties {
        public static final Property Device_id = new Property(1, String.class, "device_id", false, "DEVICE_ID");
        public static final Property Feature_data = new Property(12, String.class, "feature_data", false, "FEATURE_DATA");
        public static final Property Fw_ver = new Property(8, String.class, "fw_ver", false, "FW_VER");
        public static final Property Gateway_id = new Property(5, String.class, "gateway_id", false, "GATEWAY_ID");
        public static final Property Id = new Property(0, Long.class, "id", true, "_id");
        public static final Property Is_deleted = new Property(15, Boolean.TYPE, "is_deleted", false, "IS_DELETED");
        public static final Property Manufacturer = new Property(11, String.class, "manufacturer", false, "MANUFACTURER");
        public static final Property Model_desc = new Property(9, String.class, "model_desc", false, "MODEL_DESC");
        public static final Property Model_name = new Property(10, String.class, "model_name", false, "MODEL_NAME");
        public static final Property Model_version = new Property(6, String.class, "model_version", false, "MODEL_VERSION");
        public static final Property Name = new Property(3, String.class, "name", false, "NAME");
        public static final Property Status_data = new Property(13, String.class, "status_data", false, "STATUS_DATA");
        public static final Property Status_updated_at = new Property(14, Long.TYPE, "status_updated_at", false, "STATUS_UPDATED_AT");
        public static final Property Type = new Property(4, String.class, "type", false, "TYPE");
        public static final Property Updated_at = new Property(2, Long.TYPE, "updated_at", false, "UPDATED_AT");
        public static final Property Ver = new Property(7, String.class, "ver", false, "VER");
    }

    public DeviceDao(DaoConfig config) {
        super(config);
    }

    public DeviceDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists ? "IF NOT EXISTS " : "";
        db.execSQL("CREATE TABLE " + constraint + "\"DEVICE\" (\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," +
                "\"DEVICE_ID\" TEXT NOT NULL ," +
                "\"UPDATED_AT\" INTEGER NOT NULL ," +
                "\"NAME\" TEXT," +
                "\"TYPE\" TEXT," +
                "\"GATEWAY_ID\" TEXT," +
                "\"MODEL_VERSION\" TEXT," +
                "\"VER\" TEXT," +
                "\"FW_VER\" TEXT," +
                "\"MODEL_DESC\" TEXT," +
                "\"MODEL_NAME\" TEXT," +
                "\"MANUFACTURER\" TEXT," +
                "\"FEATURE_DATA\" TEXT," +
                "\"STATUS_DATA\" TEXT," +
                "\"STATUS_UPDATED_AT\" INTEGER NOT NULL ," +
                "\"IS_DELETED\" INTEGER NOT NULL );");
        db.execSQL("CREATE UNIQUE INDEX " + constraint + "IDX_DEVICE_DEVICE_ID ON \"DEVICE\" (\"DEVICE_ID\" ASC);");
    }

    public static void dropTable(Database db, boolean ifExists) {
        StringBuilder sb = new StringBuilder();
        sb.append("DROP TABLE ");
        sb.append(ifExists ? "IF EXISTS " : "");
        sb.append("\"DEVICE\"");
        db.execSQL(sb.toString());
    }

    /* access modifiers changed from: protected */
    public final void bindValues(DatabaseStatement stmt, Device entity) {
        stmt.clearBindings();
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id.longValue());
        }
        stmt.bindString(2, entity.getDevice_id());
        stmt.bindLong(3, entity.getUpdated_at());
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(4, name);
        }
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(5, type);
        }
        String gateway_id = entity.getGateway_id();
        if (gateway_id != null) {
            stmt.bindString(6, gateway_id);
        }
        String model_version = entity.getModel_version();
        if (model_version != null) {
            stmt.bindString(7, model_version);
        }
        String ver = entity.getVer();
        if (ver != null) {
            stmt.bindString(8, ver);
        }
        String fw_ver = entity.getFw_ver();
        if (fw_ver != null) {
            stmt.bindString(9, fw_ver);
        }
        String model_desc = entity.getModel_desc();
        if (model_desc != null) {
            stmt.bindString(10, model_desc);
        }
        String model_name = entity.getModel_name();
        if (model_name != null) {
            stmt.bindString(11, model_name);
        }
        String manufacturer = entity.getManufacturer();
        if (manufacturer != null) {
            stmt.bindString(12, manufacturer);
        }
        String feature_data = entity.getFeature_data();
        if (feature_data != null) {
            stmt.bindString(13, feature_data);
        }
        String status_data = entity.getStatus_data();
        if (status_data != null) {
            stmt.bindString(14, status_data);
        }
        stmt.bindLong(15, entity.getStatus_updated_at());
        stmt.bindLong(16, entity.getIs_deleted() ? 1 : 0);
    }

    /* access modifiers changed from: protected */
    public final void bindValues(SQLiteStatement stmt, Device entity) {
        stmt.clearBindings();
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id.longValue());
        }
        stmt.bindString(2, entity.getDevice_id());
        stmt.bindLong(3, entity.getUpdated_at());
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(4, name);
        }
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(5, type);
        }
        String gateway_id = entity.getGateway_id();
        if (gateway_id != null) {
            stmt.bindString(6, gateway_id);
        }
        String model_version = entity.getModel_version();
        if (model_version != null) {
            stmt.bindString(7, model_version);
        }
        String ver = entity.getVer();
        if (ver != null) {
            stmt.bindString(8, ver);
        }
        String fw_ver = entity.getFw_ver();
        if (fw_ver != null) {
            stmt.bindString(9, fw_ver);
        }
        String model_desc = entity.getModel_desc();
        if (model_desc != null) {
            stmt.bindString(10, model_desc);
        }
        String model_name = entity.getModel_name();
        if (model_name != null) {
            stmt.bindString(11, model_name);
        }
        String manufacturer = entity.getManufacturer();
        if (manufacturer != null) {
            stmt.bindString(12, manufacturer);
        }
        String feature_data = entity.getFeature_data();
        if (feature_data != null) {
            stmt.bindString(13, feature_data);
        }
        String status_data = entity.getStatus_data();
        if (status_data != null) {
            stmt.bindString(14, status_data);
        }
        stmt.bindLong(15, entity.getStatus_updated_at());
        stmt.bindLong(16, entity.getIs_deleted() ? 1 : 0);
    }

    @Override // org.greenrobot.greendao.AbstractDao
    public Long readKey(Cursor cursor, int offset) {
        if (cursor.isNull(offset + 0)) {
            return null;
        }
        return Long.valueOf(cursor.getLong(offset + 0));
    }

    @Override // org.greenrobot.greendao.AbstractDao
    public Device readEntity(Cursor cursor, int offset) {
        return new Device(cursor.isNull(offset + 0) ? null : Long.valueOf(cursor.getLong(offset + 0)), cursor.getString(offset + 1), cursor.getLong(offset + 2), cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), cursor.getLong(offset + 14), cursor.getShort(offset + 15) != 0);
    }

    public void readEntity(Cursor cursor, Device entity, int offset) {
        String str = null;
        entity.setId(cursor.isNull(offset + 0) ? null : Long.valueOf(cursor.getLong(offset + 0)));
        entity.setDevice_id(cursor.getString(offset + 1));
        entity.setUpdated_at(cursor.getLong(offset + 2));
        entity.setName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setType(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setGateway_id(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setModel_version(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setVer(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setFw_ver(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setModel_desc(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setModel_name(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setManufacturer(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setFeature_data(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        if (!cursor.isNull(offset + 13)) {
            str = cursor.getString(offset + 13);
        }
        entity.setStatus_data(str);
        entity.setStatus_updated_at(cursor.getLong(offset + 14));
        entity.setIs_deleted(cursor.getShort(offset + 15) != 0);
    }

    /* access modifiers changed from: protected */
    public final Long updateKeyAfterInsert(Device entity, long rowId) {
        entity.setId(Long.valueOf(rowId));
        return Long.valueOf(rowId);
    }

    public Long getKey(Device entity) {
        if (entity != null) {
            return entity.getId();
        }
        return null;
    }

    public boolean hasKey(Device entity) {
        return entity.getId() != null;
    }

    /* access modifiers changed from: protected */
    @Override // org.greenrobot.greendao.AbstractDao
    public final boolean isEntityUpdateable() {
        return true;
    }
}
