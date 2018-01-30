package org.jerseydemo.domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zacconding
 * @Date 2018-01-30
 * @GitHub : https://github.com/zacscoding
 */
@Getter
@Setter
@XmlRootElement
public class Person {
    private static final Gson gson = new GsonBuilder().serializeNulls().create();
    private String id;
    private String name;
    private int age;
    private String job;

    @Override
    public String toString() {
        return gson.toJson(this);
    }
}
