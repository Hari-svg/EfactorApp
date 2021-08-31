package com.sravan.efactorapp.utils.DataBase.Model;

import org.json.JSONObject;

public class Device {
    private String device_id;
    private String feature_data;
    private String fw_ver;
    private String gateway_id;
    private Long id;
    private boolean is_deleted;
    private String manufacturer;
    private String model_desc;
    private String model_name;
    private String model_version;
    private String name;
    private String status_data;
    private long status_updated_at = 0;
    private String type;
    private long updated_at;
    private String ver;

    public Device(Long id2, String device_id2, long updated_at2, String name2, String type2, String gateway_id2, String model_version2, String ver2, String fw_ver2, String model_desc2, String model_name2, String manufacturer2, String feature_data2, String status_data2, long status_updated_at2, boolean is_deleted2) {
        this.id = id2;
        this.device_id = device_id2;
        this.updated_at = updated_at2;
        this.name = name2;
        this.type = type2;
        this.gateway_id = gateway_id2;
        this.model_version = model_version2;
        this.ver = ver2;
        this.fw_ver = fw_ver2;
        this.model_desc = model_desc2;
        this.model_name = model_name2;
        this.manufacturer = manufacturer2;
        this.feature_data = feature_data2;
        this.status_data = status_data2;
        this.status_updated_at = status_updated_at2;
        this.is_deleted = is_deleted2;
    }

    public Device() {
    }

    public static Device toDevice(JSONObject jsonObject) {
        try {
            Device device = new Device();
            device.setGateway_id(jsonObject.getString("gateway_id"));
            device.setDevice_id(jsonObject.getString("device_id"));
            device.setUpdated_at(jsonObject.getLong("updated_at"));
            device.setName(jsonObject.getString("name"));
            device.setType(jsonObject.getString("type"));
            device.setModel_version(jsonObject.getString("model_ver"));
            device.setModel_name(jsonObject.getString("model_name"));
            device.setModel_desc(jsonObject.getString("model_desc"));
            device.setFw_ver(jsonObject.getString("fw_ver"));
            device.setVer(jsonObject.getString("ver"));
            device.setManufacturer(jsonObject.getString("manufacturer"));
            device.setFeature_data(jsonObject.getString("feature_data"));
            device.setStatus_data(jsonObject.getString("status_data"));
            if (jsonObject.has("status_updated_at")) {
                device.setStatus_updated_at(jsonObject.getLong("status_updated_at"));
            }
            device.setIs_deleted(false);
            return device;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String toString() {
        return "Device{id=" + this.id + ", device_id='" + this.device_id + '\'' + ", updated_at=" + this.updated_at + ", name='" + this.name + '\'' + ", type='" + this.type + '\'' + ", gateway_id='" + this.gateway_id + '\'' + ", model_version='" + this.model_version + '\'' + ", ver='" + this.ver + '\'' + ", fw_ver='" + this.fw_ver + '\'' + ", model_desc='" + this.model_desc + '\'' + ", model_name='" + this.model_name + '\'' + ", manufacturer='" + this.manufacturer + '\'' + ", feature_data='" + this.feature_data + '\'' + ", status_data='" + this.status_data + '\'' + ", status_updated_at='" + this.status_updated_at + '\'' + ", is_deleted=" + this.is_deleted + '}';
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id2) {
        this.id = id2;
    }

    public String getDevice_id() {
        return this.device_id;
    }

    public void setDevice_id(String device_id2) {
        this.device_id = device_id2;
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

    public String getType() {
        return this.type;
    }

    public void setType(String type2) {
        this.type = type2;
    }

    public String getGateway_id() {
        return this.gateway_id;
    }

    public void setGateway_id(String gateway_id2) {
        this.gateway_id = gateway_id2;
    }

    public String getModel_version() {
        return this.model_version;
    }

    public void setModel_version(String model_version2) {
        this.model_version = model_version2;
    }

    public String getVer() {
        return this.ver;
    }

    public void setVer(String ver2) {
        this.ver = ver2;
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

    public long getStatus_updated_at() {
        return this.status_updated_at;
    }

    public void setStatus_updated_at(long status_updated_at2) {
        this.status_updated_at = status_updated_at2;
    }

    public boolean getIs_deleted() {
        return this.is_deleted;
    }

    public void setIs_deleted(boolean is_deleted2) {
        this.is_deleted = is_deleted2;
    }
}
