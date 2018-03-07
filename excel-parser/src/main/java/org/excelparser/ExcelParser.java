package org.excelparser;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import org.excelparser.SampleDomain.Person;
import org.excelparser.annotation.ExcelField;
import org.excelparser.annotation.ExcelFieldType;
import org.excelparser.mapping.ExcelPersistentEntity;

/**
 * @author zaccoding
 * github : https://github.com/zacscoding
 */
public class ExcelParser {

    private static final Class<?>[] VOID_PARAM_TYPES = new Class<?>[]{};
    private static ExcelParser INSTANCE = new ExcelParser();

    private ConcurrentHashMap<Class<?>, List<ExcelPersistentEntity>> persistentMap = new ConcurrentHashMap<>();

    private ExcelParser() {
    }

    public static void main(String[] args) throws Exception {
        int count = 100;
        Thread[] thread = new Thread[count];
        for (int i = 0; i < count; i++) {
            thread[i] = new Thread(() -> {
                ExcelParser.INSTANCE.getExcelPersistents(Person.class);
            });
            thread[i].start();
        }
        for (int i = 0; i < count; i++) {
            thread[i].join();
        }
    }

    private static Object lock = new Object();

    private List<ExcelPersistentEntity> getExcelPersistents(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        List<ExcelPersistentEntity> persistents = null;
        boolean cache = true;

        if (cache) {
            persistents = persistentMap.get(clazz);
            if (persistents == null) {
                synchronized (lock) {
                    if ((persistents = persistentMap.get(clazz)) == null) {
                        persistents = getPersistentsWithRecursive(clazz);
                        persistentMap.put(clazz, persistents);
                    }

                }
            }
        }
        else {
            persistents = getPersistentsWithRecursive(clazz);
        }

        return persistents;
    }

    private List<ExcelPersistentEntity> getPersistentsWithRecursive(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }

        List<ExcelPersistentEntity> result = new ArrayList<>();
        PriorityQueue<ExcelPersistentEntity> persistentQueue = new PriorityQueue<>(Comparator.comparingInt(ExcelPersistentEntity::getCellOrder));

        // offer member fiels with priority
        for (Field field : clazz.getDeclaredFields()) {
            ExcelField excelField = null;
            if ((excelField = field.getAnnotation(ExcelField.class)) != null) {
                ExcelPersistentEntity e = parseExcelPersistentEntity(excelField);
                e.setField(field);
                e.setInvoker(clazz);
                persistentQueue.offer(e);
            }
        }

        while (!persistentQueue.isEmpty()) {
            ExcelPersistentEntity persistent = persistentQueue.poll();
            if (persistent.getFieldType() == ExcelFieldType.Object) {
                result.add(persistent);
                List<ExcelPersistentEntity> childs = getPersistentsWithRecursive(persistent.getField().getType());
                if (childs != null) {
                    result.addAll(childs);
                }
            }
            else if (persistent.getFieldType() == ExcelFieldType.Primitive) {
                Method[] invokeMethods = getInvokeMethods(persistent.getMethodsName(), clazz);
                if (invokeMethods != null) {
                    persistent.setMethods(invokeMethods);
                }

                result.add(persistent);
            }
        }

        return result;
    }

    private ExcelPersistentEntity parseExcelPersistentEntity(ExcelField excelField) {
        ExcelPersistentEntity entity = new ExcelPersistentEntity();
        entity.setCellOrder(excelField.cellOrder());
        entity.setFieldType(excelField.fieldType());

        if (excelField.fieldType() == ExcelFieldType.Primitive) {
            entity.setCellName(excelField.cellValue());
            entity.setNotNull(excelField.notNull());
            entity.setRegex(excelField.regex());
            entity.setMethodsName(excelField.valueInvokeMethod());
        }

        return entity;
    }

    private Method[] getInvokeMethods(String[] invokeMethods, Class<?> clazz) {
        if ((invokeMethods == null) || (invokeMethods.length != 2) || (isEmpty(invokeMethods[0]) && isEmpty(invokeMethods[1]))) {
            return null;
        }

        Method[] ret = new Method[2];
        // setter method
        if (isNotEmpty(invokeMethods[0])) {
            try {
                ret[0] = clazz.getMethod(invokeMethods[0], String.class);
            }
            catch (NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }
        }

        // getter method
        if (isNotEmpty(invokeMethods[1])) {
            try {
                ret[1] = clazz.getMethod(invokeMethods[1], VOID_PARAM_TYPES);
            }
            catch (NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }
        }

        return ret;
    }

    private boolean isEmpty(String value) {
        return (value == null) || (value.length() == 0);
    }

    private boolean isNotEmpty(String value) {
        return (value != null) && (value.length() > 0);
    }

}
