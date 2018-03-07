package org.excelparser.SampleDomain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.excelparser.annotation.ExcelField;

/**
 * @author zaccoding
 * github : https://github.com/zacscoding
 */
@Getter
@Setter
@ToString
public class Address {

    @ExcelField(cellOrder = 1, cellValue = "Zip-Code")
    private String zipCode;
    private String city;
}
