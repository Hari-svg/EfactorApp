package com.sravan.efactorapp.utils.DataBase.Model;

import org.json.JSONObject;

public class Gateway {
    private String apn;
    private String feature_data;
    private String fw_ver;
    private String gateway_id;
    private Long id;
    private boolean is_deleted;
    private double latitude;
    private double longitude;
    private String mac;
    private String manufacturer;
    private String model_desc;
    private String model_name;
    private String model_version;
    private String name;
    private String secret_key;
    private String status_data;
    private String timezone;
    private String type;
    private long updated_at;
    private String wifi_ssid;

    public static Gateway toGateway(JSONObject jsonObject) {
        try {
            Gateway gateway = new Gateway();
            gateway.setGateway_id(jsonObject.getString("gateway_id"));
            gateway.setUpdated_at(jsonObject.getLong("updated_at"));
            gateway.setName(jsonObject.getString("name"));
            gateway.setMac(jsonObject.getString("mac"));
            gateway.setApn(jsonObject.getString("apn"));
            gateway.setSecret_key(jsonObject.getString("secret_key"));
            gateway.setType(jsonObject.getString("type"));
            gateway.setLatitude(jsonObject.getDouble("latitude"));
            gateway.setLongitude(jsonObject.getDouble("longitude"));
            gateway.setTimezone(jsonObject.getString("timezone"));
            gateway.setWifi_ssid(jsonObject.getString("wifi_ssid"));
            gateway.setModel_version(jsonObject.getString("model_ver"));
            gateway.setModel_name(jsonObject.getString("model_name"));
            gateway.setModel_desc(jsonObject.getString("model_desc"));
            gateway.setFw_ver(jsonObject.getString("fw_ver"));
            gateway.setManufacturer(jsonObject.getString("manufacturer"));
            gateway.setFeature_data(jsonObject.getString("feature_data"));
            gateway.setStatus_data(jsonObject.getString("status_data"));
            gateway.setIs_deleted(false);
            return gateway;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String toString() {
        return "Gateway{id=" + this.id + ", gateway_id='" + this.gateway_id + '\'' + ", updated_at=" + this.updated_at + ", name='" + this.name + '\'' + ", mac='" + this.mac + '\'' + ", apn='" + this.apn + '\'' + ", secret_key='" + this.secret_key + '\'' + ", type='" + this.type + '\'' + ", latitude=" + this.latitude + ", longitude=" + this.longitude + ", timezone='" + this.timezone + '\'' + ", wifi_ssid='" + this.wifi_ssid + '\'' + ", model_version='" + this.model_version + '\'' + ", fw_ver='" + this.fw_ver + '\'' + ", model_desc='" + this.model_desc + '\'' + ", model_name='" + this.model_name + '\'' + ", manufacturer='" + this.manufacturer + '\'' + ", feature_data='" + this.feature_data + '\'' + ", status_data='" + this.status_data + '\'' + ", is_deleted=" + this.is_deleted + '}';
    }

    public Gateway(Long id2, String gateway_id2, long updated_at2, String name2, String mac2, String apn2, String secret_key2, String type2, double latitude2, double longitude2, String timezone2, String wifi_ssid2, String model_version2, String fw_ver2, String model_desc2, String model_name2, String manufacturer2, String feature_data2, String status_data2, boolean is_deleted2) {
        this.id = id2;
        this.gateway_id = gateway_id2;
        this.updated_at = updated_at2;
        this.name = name2;
        this.mac = mac2;
        this.apn = apn2;
        this.secret_key = secret_key2;
        this.type = type2;
        this.latitude = latitude2;
        this.longitude = longitude2;
        this.timezone = timezone2;
        this.wifi_ssid = wifi_ssid2;
        this.model_version = model_version2;
        this.fw_ver = fw_ver2;
        this.model_desc = model_desc2;
        this.model_name = model_name2;
        this.manufacturer = manufacturer2;
        this.feature_data = feature_data2;
        this.status_data = status_data2;
        this.is_deleted = is_deleted2;
    }

    public Gateway() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id2) {
        this.id = id2;
    }

    public String getGateway_id() {
        return this.gateway_id;
    }

    public void setGateway_id(String gateway_id2) {
        this.gateway_id = gateway_id2;
    }

    public long getUpdated_at() {
        return this.updated_at;
    }

    public void setUpdated_at(long updated_at2) {
        this.updated_at = updated_at2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getMac() {
        return this.mac;
    }

    public void setMac(String mac2) {
        this.mac = mac2;
    }

    public String getApn() {
        return this.apn;
    }

    public void setApn(String apn2) {
        this.apn = apn2;
    }

    public String getSecret_key() {
        return this.secret_key;
    }

    public void setSecret_key(String secret_key2) {
        this.secret_key = secret_key2;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type2) {
        this.type = type2;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude2) {
        this.latitude = latitude2;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude2) {
        this.longitude = longitude2;
    }

    public String getTimezone() {
        return this.timezone;
    }

    public void setTimezone(String timezone2) {
        this.timezone = timezone2;
    }

    public String getWifi_ssid() {
        return this.wifi_ssid;
    }

    public void setWifi_ssid(String wifi_ssid2) {
        this.wifi_ssid = wifi_ssid2;
    }

    public String getModel_version() {
        return this.model_version;
    }

    public void setModel_version(String model_version2) {
        this.model_version = model_version2;
    }

    public String getFw_ver() {
        return this.fw_ver;
    }

    public void setFw_ver(String fw_ver2) {
        this.fw_ver = fw_ver2;
    }

    public String getModel_desc() {
        return this.model_desc;
    }

    public void setModel_desc(String model_desc2) {
        this.model_desc = model_desc2;
    }

    public String getModel_name() {
        return this.model_name;
    }

    public void setModel_name(String model_name2) {
        this.model_name = model_name2;
    }

    public String getManufacturer() {
        return this.manufacturer;
    }

    public void setManufacturer(String manufacturer2) {
        this.manufacturer = manufacturer2;
    }

    public String getFeature_data() {
        return this.feature_data;
    }

    public void setFeature_data(String feature_data2) {
        this.feature_data = feature_data2;
    }

    public String getStatus_data() {
        return this.status_data;
    }

    public void setStatus_data(String status_data2) {
        this.status_data = status_data2;
    }

    public boolean getIs_deleted() {
        return this.is_deleted;
    }

    public void setIs_deleted(boolean is_deleted2) {
        this.is_deleted = is_deleted2;
    }
}
