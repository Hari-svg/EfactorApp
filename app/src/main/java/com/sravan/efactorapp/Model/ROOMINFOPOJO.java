package com.sravan.efactorapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ROOMINFOPOJO {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("rooms")
    @Expose
    private List<Room> rooms = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public class Room {
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("gateway_id")
        @Expose
        private String gatewayId;
        @SerializedName("roomname")
        @Expose
        private String roomname;

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

        public String getRoomname() {
            return roomname;
        }

        public void setRoomname(String roomname) {
            this.roomname = roomname;
        }
    }
}
