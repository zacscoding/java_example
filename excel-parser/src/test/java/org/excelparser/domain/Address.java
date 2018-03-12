package org.excelparser.domain;

import org.excelparser.annotation.ExcelField;

/**
 * @author zacconding
 * @Date 2018-03-13
 * @GitHub : https://github.com/zacscoding
 */
public class Address {

    @ExcelField(cellOrder = 1, cellValue = "Zip-Code")
    private String zipCode;
    private String city;

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Address{" +
            "zipCode='" + zipCode + '\'' +
            ", city='" + city + '\'' +
            '}';
    }
}