package com.sravan.efactorapp.utils.DataBase.Model;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;



import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

public class UserDao extends AbstractDao<User, Long> {
    public static final String TABLENAME = "USER";

    public static class Properties {
        public static final Property Address = new Property(5, String.class, "address", false, "ADDRESS");
        public static final Property Email = new Property(6, String.class, "email", false, "EMAIL");
        public static final Property Id = new Property(0, Long.class, "id", true, "_id");
        public static final Property Name = new Property(3, String.class, "name", false, "NAME");
        public static final Property Phone = new Property(7, String.class, "phone", false, "PHONE");
        public static final Property Sub_uuid = new Property(2, String.class, "sub_uuid", false, "SUB_UUID");
        public static final Property Updated_at = new Property(4, Long.class, "updated_at", false, "UPDATED_AT");
        public static final Property Username = new Property(1, String.class, "username", false, "USERNAME");
    }

    public UserDao(DaoConfig config) {
        super(config);
    }

    public UserDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists ? "IF NOT EXISTS " : "";
        db.execSQL("CREATE TABLE " + constraint + "\"USER\" (\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," +
                "\"USERNAME\" TEXT NOT NULL ," +
                "\"SUB_UUID\" TEXT," +
                "\"NAME\" TEXT NOT NULL ," +
                "\"UPDATED_AT\" INTEGER," +
                "\"ADDRESS\" TEXT," +
                "\"EMAIL\" TEXT," +
                "\"PHONE\" TEXT);");
        db.execSQL("CREATE UNIQUE INDEX " + constraint + "IDX_USER_USERNAME ON \"USER\" (\"USERNAME\" ASC);");
    }

    public static void dropTable(Database db, boolean ifExists) {
        StringBuilder sb = new StringBuilder();
        sb.append("DROP TABLE ");
        sb.append(ifExists ? "IF EXISTS " : "");
        sb.append("\"USER\"");
        db.execSQL(sb.toString());
    }

    /* access modifiers changed from: protected */
    public final void bindValues(DatabaseStatement stmt, User entity) {
        stmt.clearBindings();
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id.longValue());
        }
        stmt.bindString(2, entity.getUsername());
        String sub_uuid = entity.getSub_uuid();
        if (sub_uuid != null) {
            stmt.bindString(3, sub_uuid);
        }
        stmt.bindString(4, entity.getName());
        Long updated_at = entity.getUpdated_at();
        if (updated_at != null) {
            stmt.bindLong(5, updated_at.longValue());
        }
        String address = entity.getAddress();
        if (address != null) {
            stmt.bindString(6, address);
        }
        String email = entity.getEmail();
        if (email != null) {
            stmt.bindString(7, email);
        }
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(8, phone);
        }
    }

    /* access modifiers changed from: protected */
    public final void bindValues(SQLiteStatement stmt, User entity) {
        stmt.clearBindings();
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id.longValue());
        }
        stmt.bindString(2, entity.getUsername());
        String sub_uuid = entity.getSub_uuid();
        if (sub_uuid != null) {
            stmt.bindString(3, sub_uuid);
        }
        stmt.bindString(4, entity.getName());
        Long updated_at = entity.getUpdated_at();
        if (updated_at != null) {
            stmt.bindLong(5, updated_at.longValue());
        }
        String address = entity.getAddress();
        if (address != null) {
            stmt.bindString(6, address);
        }
        String email = entity.getEmail();
        if (email != null) {
            stmt.bindString(7, email);
        }
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(8, phone);
        }
    }

    @Override // org.greenrobot.greendao.AbstractDao
    public Long readKey(Cursor cursor, int offset) {
        if (cursor.isNull(offset + 0)) {
            return null;
        }
        return Long.valueOf(cursor.getLong(offset + 0));
    }

    @Override // org.greenrobot.greendao.AbstractDao
    public User readEntity(Cursor cursor, int offset) {
        return new User(cursor.isNull(offset + 0) ? null : Long.valueOf(cursor.getLong(offset + 0)), cursor.getString(offset + 1), cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), cursor.getString(offset + 3), cursor.isNull(offset + 4) ? null : Long.valueOf(cursor.getLong(offset + 4)), cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
    }

    public void readEntity(Cursor cursor, User entity, int offset) {
        String str = null;
        entity.setId(cursor.isNull(offset + 0) ? null : Long.valueOf(cursor.getLong(offset + 0)));
        entity.setUsername(cursor.getString(offset + 1));
        entity.setSub_uuid(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setName(cursor.getString(offset + 3));
        entity.setUpdated_at(cursor.isNull(offset + 4) ? null : Long.valueOf(cursor.getLong(offset + 4)));
        entity.setAddress(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setEmail(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        if (!cursor.isNull(offset + 7)) {
            str = cursor.getString(offset + 7);
        }
        entity.setPhone(str);
    }

    /* access modifiers changed from: protected */
    public final Long updateKeyAfterInsert(User entity, long rowId) {
        entity.setId(Long.valueOf(rowId));
        return Long.valueOf(rowId);
    }

    public Long getKey(User entity) {
        if (entity != null) {
            return entity.getId();
        }
        return null;
    }

    public boolean hasKey(User entity) {
        return entity.getId() != null;
    }

    /* access modifiers changed from: protected */
    @Override // org.greenrobot.greendao.AbstractDao
    public final boolean isEntityUpdateable() {
        return true;
    }
}
