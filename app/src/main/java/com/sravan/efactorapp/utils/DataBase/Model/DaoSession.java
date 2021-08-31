package com.sravan.efactorapp.utils.DataBase.Model;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import java.util.Map;

public class DaoSession extends AbstractDaoSession {
    private final DeviceDao deviceDao ;
    private DaoConfig deviceDaoConfig;
    private final GatewayDao gatewayDao;
    private DaoConfig gatewayDaoConfig;
    private final UserDao userDao;
    private DaoConfig userDaoConfig;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig> daoConfigMap) {
        super(db);
        this.deviceDaoConfig = daoConfigMap.get(DeviceDao.class).clone();
        this.deviceDaoConfig.initIdentityScope(type);
        this.gatewayDaoConfig = daoConfigMap.get(GatewayDao.class).clone();
        this.gatewayDaoConfig.initIdentityScope(type);
        this.userDaoConfig = daoConfigMap.get(UserDao.class).clone();
        this.userDaoConfig.initIdentityScope(type);
        deviceDao = new DeviceDao(this.deviceDaoConfig, this);
        gatewayDao = new GatewayDao(this.gatewayDaoConfig, this);
        userDao = new UserDao(this.userDaoConfig, this);
        registerDao(Device.class, this.deviceDao);
        registerDao(Gateway.class, this.gatewayDao);
        registerDao(User.class, this.userDao);


    }

    public void clear() {
        this.deviceDaoConfig.clearIdentityScope();
        this.gatewayDaoConfig.clearIdentityScope();
        this.userDaoConfig.clearIdentityScope();
    }

    public DeviceDao getDeviceDao() {
        return this.deviceDao;
    }

    public GatewayDao getGatewayDao() {
        return this.gatewayDao;
    }

    public UserDao getUserDao() {
        return this.userDao;
    }
}
