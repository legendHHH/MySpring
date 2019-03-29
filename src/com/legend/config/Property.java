package com.legend.config;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Create By legend
 * @date 2019/3/26 11:15
 * 用于存放bean标签property的属性的类
 */
public class Property {
    private String name;
    private String value;
    private String ref;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }
}
