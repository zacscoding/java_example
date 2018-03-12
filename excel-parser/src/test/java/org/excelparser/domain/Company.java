package org.excelparser.domain;

import org.excelparser.annotation.ExcelField;

/**
 * @author zacconding
 * @Date 2018-03-13
 * @GitHub : https://github.com/zacscoding
 */
public class Company {

    @ExcelField(cellOrder = 1, cellValue = "Company name")
    private String name;
    @ExcelField(cellOrder = 2, cellValue = "Salary")
    private int salary;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Company{" +
            "name='" + name + '\'' +
            ", salary=" + salary +
            '}';
    }
}
