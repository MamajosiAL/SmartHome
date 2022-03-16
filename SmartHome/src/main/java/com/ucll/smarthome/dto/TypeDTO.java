package com.ucll.smarthome.dto;

public class TypeDTO {

    private long typeid;
    private String typeName;

    public TypeDTO() {
    }

    private TypeDTO(Builder builder) {
        setTypeid(builder.typeid);
        setTypeName(builder.typeName);
    }

    public long getTypeid() {
        return typeid;
    }

    public void setTypeid(long typeid) {
        this.typeid = typeid;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public static final class Builder {
        private long typeid;
        private String typeName;

        public Builder() {
        }

        public Builder typeid(long val) {
            typeid = val;
            return this;
        }

        public Builder typeName(String val) {
            typeName = val;
            return this;
        }

        public TypeDTO build() {
            return new TypeDTO(this);
        }
    }
}
