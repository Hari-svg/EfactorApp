package com.sravan.efactorapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MYDEVICESPOJO {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("devices")
    @Expose
    private List<DeviceDATA> devices = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<DeviceDATA> getDevices() {
        return devices;
    }

    public void setDevices(List<DeviceDATA> devices) {
        this.devices = devices;
    }

    public class DeviceDATA {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("gateway_id")
        @Expose
        private String gatewayId;
        @SerializedName("device_id")
        @Expose
        private String deviceId;
        @SerializedName("room_id")
        @Expose
        private String roomId;
        @SerializedName("device_name")
        @Expose
        private String deviceName;
        @SerializedName("device_type")
        @Expose
        private String deviceType;
        @SerializedName("device_ver")
        @Expose
        private String deviceVer;
        @SerializedName("device_fw_ver")
        @Expose
        private String deviceFwVer;
        @SerializedName("device_manufacturer")
        @Expose
        private String deviceManufacturer;
        @SerializedName("device_model_version")
        @Expose
        private String deviceModelVersion;
        @SerializedName("device_model_name")
        @Expose
        private String deviceModelName;
        @SerializedName("device_model_desc")
        @Expose
        private String deviceModelDesc;
        @SerializedName("device_status_data")
        @Expose
        private String deviceStatusData;
        @SerializedName("state")
        @Expose
        private String state;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGatewayId() {
            return gatewayId;
        }

        public void setGatewayId(String gatewayId) {
            this.gatewayId = gatewayId;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }

        public String getDeviceVer() {
            return deviceVer;
        }

        public void setDeviceVer(String deviceVer) {
            this.deviceVer = deviceVer;
        }

        public String getDeviceFwVer() {
            return deviceFwVer;
        }

        public void setDeviceFwVer(String deviceFwVer) {
            this.deviceFwVer = deviceFwVer;
        }

        public String getDeviceManufacturer() {
            return deviceManufacturer;
        }

        public void setDeviceManufacturer(String deviceManufacturer) {
            this.deviceManufacturer = deviceManufacturer;
        }

        public String getDeviceModelVersion() {
            return deviceModelVersion;
        }

        public void setDeviceModelVersion(String deviceModelVersion) {
            this.deviceModelVersion = deviceModelVersion;
        }

        public String getDeviceModelName() {
            return deviceModelName;
        }

        public void setDeviceModelName(String deviceModelName) {
            this.deviceModelName = deviceModelName;
        }

        public String getDeviceModelDesc() {
            return deviceModelDesc;
        }

        public void setDeviceModelDesc(String deviceModelDesc) {
            this.deviceModelDesc = deviceModelDesc;
        }

        public String getDeviceStatusData() {
            return deviceStatusData;
        }

        public void setDeviceStatusData(String deviceStatusData) {
            this.deviceStatusData = deviceStatusData;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }
}
