package com.carlosobispo.postconsulting.models.enums;

import lombok.Getter;

@Getter
public enum Type {
    post("post"),
    comment("comment");

    private String description;

    Type(String description) {
        this.description = description;
    }
}
