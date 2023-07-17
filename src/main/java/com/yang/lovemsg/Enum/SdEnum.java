package com.yang.lovemsg.Enum;

public enum SdEnum {
    one("chp"), two("pyq"), three("du");

    private String type;

    SdEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
