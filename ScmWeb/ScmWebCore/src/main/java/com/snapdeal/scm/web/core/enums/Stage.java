package com.snapdeal.scm.web.core.enums;


/**
 * Created by Harsh Gupta on 29/02/16.
 */
public enum Stage {

    ONE("ONE"),TWO("TWO"),THREE("THREE"),FOUR("FOUR"),FIVE("FIVE"),SIX("SIX");

    private String code;

    private Stage(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "Stage{" +
                "code='" + code + '\'' +
                '}';
    }

    public static Stage fromCode(String code){
        if (code==null) return null;

        for (Stage stage : values()) {
            if(stage.code.equalsIgnoreCase(code)){
                return stage;
            }
        }
        return null;
    }
}
