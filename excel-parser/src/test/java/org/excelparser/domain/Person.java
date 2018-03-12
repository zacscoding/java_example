package org.excelparser.domain;


import java.util.ArrayList;
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
 * @author zaccoding github : https://github.com/zacscoding
 */
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<String> hobbies) {
        this.hobbies = hobbies;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "Person{" +
            "age=" + age +
            ", name='" + name + '\'' +
            ", hobbies=" + hobbies +
            ", address=" + address +
            ", company=" + company +
            '}';
    }
}
