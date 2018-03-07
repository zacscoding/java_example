package org.excelparser.SampleDomain;

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
public class Company {

    @ExcelField(cellOrder = 1, cellValue = "Company name")
    private String name;
    @ExcelField(cellOrder = 2, cellValue = "Salary")
    private int salary;
}
