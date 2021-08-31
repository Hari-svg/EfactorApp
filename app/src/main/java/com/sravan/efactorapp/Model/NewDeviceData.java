package com.sravan.efactorapp.Model;

import android.location.Location;

import com.sravan.efactorapp.utils.Constants;
import com.sravan.efactorapp.utils.Utilities;

public class NewDeviceData {
    private String apn;
    private String authCode;
    private String featureData;
    private String fw_ver;
    private String gateway_id;
    private String id;
    private String ip;
    private Location location;
    private String mac;
    private String manufacturer;
    private String model_desc;
    private String model_name;
    private String model_version;
    private String name;
    private String secretKey;
    private boolean selected;
    private String statusData;
    private String timezone;
    private String type;
    private String ver;
    private String wifi_password;
    private String wifi_ssid;
    private String[] gatewayId;

    public NewDeviceData(String apn2, String ver2) {
        this.timezone = "UTC+05:30";
        this.apn = apn2;
        this.name = null;
        this.ver = ver2;
        this.selected = false;
        this.location = null;
        this.id = null;
        this.authCode = null;
        this.wifi_ssid = "";
        this.wifi_password = "";
        this.secretKey = "SAMPLE_KEY";
        this.featureData = "{}";
        this.statusData = "{}";
        this.type = Constants.NONE_GATEWAY_VALUE;
    }

    public NewDeviceData() {
        this.timezone = "UTC+05:30";
        this.id = null;
        this.gateway_id = null;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getVer() {
        return this.ver;
    }

    public void setVer(String ver2) {
        this.ver = ver2;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip2) {
        this.ip = ip2;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setSelected(boolean selected2) {
        this.selected = selected2;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id2) {
        this.id = id2;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location2) {
        this.location = location2;
    }

    public String getAuthCode() {
        return this.authCode;
    }

    /*public void setAuthCode() {
        this.authCode = Utilities.generateCode(40);
    }*/

    public String getApn() {
        return this.apn;
    }

    public void setApn(String apn2) {
        this.apn = apn2;
    }

    public String getMac() {
        return this.mac;
    }

    public void setMac(String mac2) {
        this.mac = mac2;
    }

    public String getWifi_ssid() {
        return this.wifi_ssid;
    }

    public void setWifi_ssid(String wifi_ssid2) {
        this.wifi_ssid = wifi_ssid2;
    }

    public void setAuthCode() {
        this.authCode = Utilities.generateCode(40);

    }

    public String getSecretKey() {
        return this.secretKey;
    }

    public void setSecretKey(String secretKey2) {
        this.secretKey = secretKey2;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type2) {
        this.type = type2;
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

    public String getTimezone() {
        return this.timezone;
    }

    public void setTimezone(String timezone2) {
        this.timezone = timezone2;
    }

    public String getFeatureData() {
        return this.featureData;
    }

    public void setFeatureData(String featureData2) {
        this.featureData = featureData2;
    }

    public String getStatusData() {
        return this.statusData;
    }

    public void setStatusData(String statusData2) {
        this.statusData = statusData2;
    }

    public String getGateway_id() {
        return this.gateway_id;
    }

    public void setGateway_id(String gateway_id2) {
        this.gateway_id = gateway_id2;
    }

    public String getWifi_password() {
        return this.wifi_password;
    }

    public void setWifi_password(String wifi_password2) {
        this.wifi_password = wifi_password2;
    }

    public void setId(String[] gatewayId) {
        this.gatewayId = new String[]{"3684680538cf4d4c9ddb73c4ecefb03e"};
    }
}

