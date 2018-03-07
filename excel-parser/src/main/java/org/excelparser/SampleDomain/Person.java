package org.excelparser.SampleDomain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.excelparser.annotation.ExcelField;
import org.excelparser.annotation.ExcelFieldType;

/**
 * @author zaccoding
 * github : https://github.com/zacscoding
 */
@Getter
@Setter
@ToString
public class Person {

    @ExcelField(cellOrder = 2, cellValue = "age")
    private int age;
    @ExcelField(cellOrder = 1, cellValue = "name")
    private String name;
    @ExcelField(cellOrder = 3, cellValue = "hobbies", valueInvokeMethod = {"setHobbiesValue", "getHobbiesValue"})
    private List<String> hobbies;
    @ExcelField(cellOrder = 5, fieldType = ExcelFieldType.Object)
    private Address address;
    @ExcelField(cellOrder = 4, fieldType = ExcelFieldType.Object)
    private Company company;

    // getter for excel parsing
    public String getHobbiesValue() {
        if (hobbies == null) {
            return "[]";
        }

        return hobbies.stream().collect(Collectors.joining(",", "[", "]"));
    }

    // setter for excel parsing
    public void setHobbiesValue(String hobbiesValue) {
        if (hobbiesValue == null) {
            return;
        }
        int len = hobbiesValue.length();
        if (len == 0 || (hobbiesValue.charAt(0) != '[') || (hobbiesValue.charAt(len - 1) != ']')) {
            return;
        }

        StringTokenizer stringTokenizer = new StringTokenizer(hobbiesValue.substring(1, len - 1), ",");
        int tokenSize = stringTokenizer.countTokens();
        if (tokenSize == 0) {
            hobbies = Collections.emptyList();
            return;
        }

        hobbies = new ArrayList<>(tokenSize);
        while (stringTokenizer.hasMoreElements()) {
            hobbies.add(stringTokenizer.nextToken());
        }
    }


    public static void main(String[] args) {
        System.out.println("getter...........");
        Person p1 = new Person();
        p1.setHobbies(Arrays.asList("test1", "test2"));
        System.out.println(p1.getHobbiesValue());
        p1.setHobbies(null);
        System.out.println(p1.getHobbiesValue());
        p1.setHobbies(Arrays.asList("test1"));
        System.out.println(p1.getHobbiesValue());
        p1.setHobbies(Collections.emptyList());
        System.out.println(p1.getHobbiesValue());
        System.out.println("setter...........");
        Person p2 = new Person();
        p2.setHobbies(null);
        System.out.println(p2.getHobbiesValue());
        p2.setHobbiesValue("[test1,test2]");
        System.out.println(p2.getHobbiesValue());
        p2.setHobbiesValue("[]");
        System.out.println(p2.getHobbiesValue());
        p2.setHobbiesValue("[test1,test2)");
        System.out.println(p2.getHobbiesValue());
    }


}
