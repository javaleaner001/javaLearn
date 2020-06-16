package com.fuxl.demo;

public enum EnumDemo {

    IN("0"),OK("1");
    public String code;

    private EnumDemo(String code) {
        this.code=code;
    }

    public String getCode() {
        return code;
    }

}
