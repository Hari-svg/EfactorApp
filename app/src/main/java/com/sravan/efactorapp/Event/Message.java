package com.sravan.efactorapp.Event;

import org.json.JSONObject;

public class Message {
    private final EventType eventType;
    private JSONObject jsonData;

    public Message(EventType eventType2) {
        this.eventType = eventType2;
        this.jsonData = null;
    }

    public Message(EventType eventType2, JSONObject jsonData2) {
        this.eventType = eventType2;
        this.jsonData = jsonData2;
    }

    public EventType getEventType() {
        return this.eventType;
    }

    public JSONObject getJsonData() {
        return this.jsonData;
    }

    public void setJsonData(JSONObject jsonData2) {
        this.jsonData = jsonData2;
    }
}
