package com.dmdev.entity;

import java.io.Serializable;

public interface BaseEntity<K extends Serializable> {

    K getId();

    void setId(K id);
}