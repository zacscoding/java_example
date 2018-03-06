package org.excelparser.domain;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zaccoding
 * github : https://github.com/zacscoding
 */
@Getter
@Setter
@ToString
public class Person {

    private int age;
    private String name;
    private List<String> hobbies;
    private Address address;
    private Company company;
}
