package com.sravan.efactorapp.Enum;

public enum ThingReachability {
    NONE(0),
    LOCAL_WIFI(1),
    CLOUD(2);

    private int value;

    private ThingReachability(int v) {
        this.value = v;
    }

    public int getValue() {
        return this.value;
    }

    public boolean isEqual(ThingReachability reachability) {
        return this.value == reachability.getValue();
    }
}
