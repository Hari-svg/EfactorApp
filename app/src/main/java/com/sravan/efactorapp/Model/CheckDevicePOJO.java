package com.sravan.efactorapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CheckDevicePOJO {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private List<DeviceDatum> data = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<DeviceDatum> getData() {
        return data;
    }

    public void setData(List<DeviceDatum> data) {
        this.data = data;
    }

    public class DeviceDatum {

        @SerializedName("device_id")
        @Expose
        private String deviceId;
        @SerializedName("device_feature_data")
        @Expose
        private Object deviceFeatureData;
        @SerializedName("device_fw_ver")
        @Expose
        private String deviceFwVer;
        @SerializedName("device_ver")
        @Expose
        private String deviceVer;
        @SerializedName("device_manufacturer")
        @Expose
        private String deviceManufacturer;
        @SerializedName("device_model_desc")
        @Expose
        private String deviceModelDesc;
        @SerializedName("device_model_name")
        @Expose
        private String deviceModelName;
        @SerializedName("device_model_version")
        @Expose
        private String deviceModelVersion;
        @SerializedName("device_name")
        @Expose
        private String deviceName;
        @SerializedName("device_status_data")
        @Expose
        private String deviceStatusData;
        @SerializedName("device_type")
        @Expose
        private String deviceType;
        @SerializedName("updated_at")
        @Expose
        private Object updatedAt;

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public Object getDeviceFeatureData() {
            return deviceFeatureData;
        }

        public void setDeviceFeatureData(Object deviceFeatureData) {
            this.deviceFeatureData = deviceFeatureData;
        }

        public String getDeviceFwVer() {
            return deviceFwVer;
        }

        public void setDeviceFwVer(String deviceFwVer) {
            this.deviceFwVer = deviceFwVer;
        }

        public String getDeviceVer() {
            return deviceVer;
        }

        public void setDeviceVer(String deviceVer) {
            this.deviceVer = deviceVer;
        }

        public String getDeviceManufacturer() {
            return deviceManufacturer;
        }

        public void setDeviceManufacturer(String deviceManufacturer) {
            this.deviceManufacturer = deviceManufacturer;
        }

        public String getDeviceModelDesc() {
            return deviceModelDesc;
        }

        public void setDeviceModelDesc(String deviceModelDesc) {
            this.deviceModelDesc = deviceModelDesc;
        }

        public String getDeviceModelName() {
            return deviceModelName;
        }

        public void setDeviceModelName(String deviceModelName) {
            this.deviceModelName = deviceModelName;
        }

        public String getDeviceModelVersion() {
            return deviceModelVersion;
        }

        public void setDeviceModelVersion(String deviceModelVersion) {
            this.deviceModelVersion = deviceModelVersion;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getDeviceStatusData() {
            return deviceStatusData;
        }

        public void setDeviceStatusData(String deviceStatusData) {
            this.deviceStatusData = deviceStatusData;
        }

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }

        public Object getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(Object updatedAt) {
            this.updatedAt = updatedAt;
        }
    }
}
