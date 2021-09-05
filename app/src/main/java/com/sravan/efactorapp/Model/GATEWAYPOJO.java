package com.sravan.efactorapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sravan.efactorapp.utils.DataBase.Model.Gateway;

import java.io.Serializable;
import java.util.List;

public class GATEWAYPOJO implements Serializable {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("gateways")
    @Expose
    private List<Gateway> gateways = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Gateway> getGateways() {
        return gateways;
    }

    public void setGateways(List<Gateway> gateways) {
        this.gateways = gateways;
    }

    /*public class Gateway implements Serializable {


        @SerializedName("gateway_mac")
        @Expose
        private String gatewayMac;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("gateway_id")
        @Expose
        private String gatewayId;
        @SerializedName("gateway_name")
        @Expose
        private String gatewayName;
        @SerializedName("gateway_wifi_ssid")
        @Expose
        private String gatewayWifiSsid;
        @SerializedName("gateway_type")
        @Expose
        private String gatewayType;
        @SerializedName("gateway_manufacturer")
        @Expose
        private String gatewayManufacturer;
        @SerializedName("gateway_fw_ver")
        @Expose
        private String gatewayFwVer;
        @SerializedName("gateway_ip")
        @Expose
        private String gatewayIp;

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

        public String getGatewayName() {
            return gatewayName;
        }

        public void setGatewayName(String gatewayName) {
            this.gatewayName = gatewayName;
        }

        public String getGatewayWifiSsid() {
            return gatewayWifiSsid;
        }

        public void setGatewayWifiSsid(String gatewayWifiSsid) {
            this.gatewayWifiSsid = gatewayWifiSsid;
        }

        public String getGatewayType() {
            return gatewayType;
        }

        public void setGatewayType(String gatewayType) {
            this.gatewayType = gatewayType;
        }

        public String getGatewayManufacturer() {
            return gatewayManufacturer;
        }

        public void setGatewayManufacturer(String gatewayManufacturer) {
            this.gatewayManufacturer = gatewayManufacturer;
        }

        public String getGatewayFwVer() {
            return gatewayFwVer;
        }

        public void setGatewayFwVer(String gatewayFwVer) {
            this.gatewayFwVer = gatewayFwVer;
        }

        public String getGatewayIp() {
            return gatewayIp;
        }

        public void setGatewayIp(String gatewayIp) {
            this.gatewayIp = gatewayIp;
        }

        public String getGatewayMac() {
            return gatewayMac;
        }
        public void setGatewayMac(String gatewayMac) {
            this.gatewayMac = gatewayMac;
        }
    }*/
}
