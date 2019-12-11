package com.lzq.selfdiscipline.ta.constant;

public enum BusinessCode {
    SUCCESS("0", "succes"),
    FAILURE("-1","failure"),
    EXCEPTION("-2", "exception");
    private String code;
    private String description;

    private BusinessCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
