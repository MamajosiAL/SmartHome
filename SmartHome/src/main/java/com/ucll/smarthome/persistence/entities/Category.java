package com.ucll.smarthome.persistence.entities;

import javax.persistence.*;

@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoryid")
    private Long categoryId;
    @Column(name = "name")
    private String name;

    public Category() {
    }

    private Category(Builder builder) {
        setCategoryId(builder.categoryId);
        setName(builder.name);
    }


    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static final class Builder {
        private Long categoryId;
        private String name;

        public Builder() {
        }

        public Builder categoryId(Long val) {
            categoryId = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Category build() {
            return new Category(this);
        }
    }
}
