package org.excelparser.domain;

import java.util.function.Consumer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.poi.ss.usermodel.CellStyle;

/**
 * Excel write request class
 *
 * @author zacconding
 * @Date 2018-03-13
 * @GitHub : https://github.com/zacscoding
 */
@Getter
@Setter
@ToString
public class ExcelWriteRequest {

    // sheet name
    private String sheetName;
    // start row in excel
    private int startRow;
    // header cellstyle
    private Consumer<CellStyle> headerCellStyleConsumer;
    // date pattern in excel
    private String datePattern;
}
