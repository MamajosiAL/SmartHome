package com.ucll.smarthome.persistence.entities;

import javax.persistence.*;

@Entity
@Table(name = "type",schema = "smarthome")
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",updatable = false,nullable = false)
    private Long typeid;
    @Column(name ="name")
    private String name;

    public Type() {
    }

    private Type(Builder builder) {
        setTypeid(builder.typeid);
        setName(builder.name);
    }

    public Long getTypeid() {
        return typeid;
    }

    public void setTypeid(Long typeid) {
        this.typeid = typeid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static final class Builder {
        private Long typeid;
        private String name;

        public Builder() {
        }

        public Builder typeid(Long val) {
            typeid = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Type build() {
            return new Type(this);
        }
    }
}
