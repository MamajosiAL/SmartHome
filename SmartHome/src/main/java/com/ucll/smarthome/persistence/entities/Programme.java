package com.ucll.smarthome.persistence.entities;

import javax.persistence.*;

@Entity
@Table(name = "programme",schema = "smarthome")
public class Programme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",updatable = false,nullable = false)
    private Long programid;
    @Column(name ="name")
    private String name;
    @ManyToOne
    @JoinColumn(name= "type_id")
    private Type type;
    public Programme() {
    }

    private Programme(Builder builder) {
        setProgramid(builder.programid);
        setName(builder.name);
        setType(builder.type);
    }


    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setProgramid(Long programid) {
        this.programid = programid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getProgramid() {
        return programid;
    }

    public String getName() {
        return name;
    }


    public static final class Builder {
        private Long programid;
        private String name;
        private Type type;

        public Builder() {
        }

        public Builder programid(Long val) {
            programid = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder type(Type val) {
            type = val;
            return this;
        }

        public Programme build() {
            return new Programme(this);
        }
    }
}
