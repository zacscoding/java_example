package org.excelparser;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import org.excelparser.annotation.ExcelField;
import org.excelparser.mapping.ExcelPersistentEntity;

/**
 * @author zaccoding
 * github : https://github.com/zacscoding
 */
public class ExcelService {

    private ConcurrentHashMap<Class<?>, List<ExcelPersistentEntity>> persistentMap = new ConcurrentHashMap<>();

    private List<ExcelPersistentEntity> getExcelPersistents(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }

        List<ExcelPersistentEntity> metaDatas = null;
        boolean cache = clazz.getGenericInterfaces() == null;
        if (cache) {
            metaDatas = persistentMap.get(clazz);
        }

        if (metaDatas == null) {

        }

        return metaDatas;
    }

    private List<ExcelPersistentEntity> makeTree(Class<?> clazz) {
        return null;
    }

    private ExcelPersistentEntity parseExcelPersistentEntity(ExcelField excelField) {
        ExcelPersistentEntity entity = new ExcelPersistentEntity();

        entity.setCellOrder(excelField.cellOrder());
        entity.setCellName(excelField.cellValue());
        entity.setNotNull(excelField.notNull());
        entity.setFieldType(excelField.fieldType());

        return entity;
    }

}
