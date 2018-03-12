package org.excelparser.domain.mapping;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import lombok.ToString;
import org.excelparser.annotation.ExcelFieldType;

/**
 * @author zaccoding github : https://github.com/zacscoding
 */
public class ExcelPersistentEntity {

    // cell order
    private int cellOrder;
    // field
    private Field field;
    // invoke method(setter, getter)
    private Method[] methods;
    // setter, getter method name
    private String[] methodsName;
    // 셀 이름
    private String cellName;
    // 정규식
    private String regex;
    // not null 불리언
    private boolean notNull;
    // field type
    private ExcelFieldType fieldType;
    // 필드 invoker
    private Class<?> invoker;

    /* ========================================
     * Getters , Setters , toString
     * ======================================= */
    public Method getterMethod() {
        return (methods != null && methods.length == 2) ? methods[1] : null;
    }

    public Method setterMethod() {
        return (methods != null && methods.length == 2) ? methods[0] : null;
    }

    public int getCellOrder() {
        return cellOrder;
    }

    public void setCellOrder(int cellOrder) {
        this.cellOrder = cellOrder;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public String getCellName() {
        return cellName;
    }

    public void setCellName(String cellName) {
        this.cellName = cellName;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }

    public ExcelFieldType getFieldType() {
        return fieldType;
    }

    public void setFieldType(ExcelFieldType fieldType) {
        this.fieldType = fieldType;
    }

    public Class<?> getInvoker() {
        return invoker;
    }

    public Method[] getMethods() {
        return methods;
    }

    public void setMethods(Method[] methods) {
        this.methods = methods;
    }

    public void setInvoker(Class<?> invoker) {
        this.invoker = invoker;
    }

    public String[] getMethodsName() {
        return methodsName;
    }

    public void setMethodsName(String[] methodsName) {
        this.methodsName = methodsName;
    }

    @Override
    public String toString() {
        return "ExcelPersistentEntity{" +
            "cellOrder=" + cellOrder +
            ", field=" + field +
            ", methods=" + Arrays.toString(methods) +
            ", methodsName=" + Arrays.toString(methodsName) +
            ", cellName='" + cellName + '\'' +
            ", regex='" + regex + '\'' +
            ", notNull=" + notNull +
            ", fieldType=" + fieldType +
            ", invoker=" + invoker +
            '}';
    }
}
