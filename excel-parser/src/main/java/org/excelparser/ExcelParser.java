package org.excelparser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.DateFormatConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.excelparser.annotation.ExcelField;
import org.excelparser.annotation.ExcelFieldType;
import org.excelparser.domain.ExcelWriteRequest;
import org.excelparser.domain.mapping.ExcelPersistentEntity;

/**
 * @author zaccoding github : https://github.com/zacscoding
 */
public class ExcelParser {

    private static final Class<?>[] VOID_PARAM_TYPES = new Class<?>[]{};
    private static final Object[] VOID_PARAMS = new Object[]{};
    public static ExcelParser INSTANCE = new ExcelParser();
    private static Object lock = new Object();

    private ConcurrentHashMap<Class<?>, List<ExcelPersistentEntity>> persistentMap = new ConcurrentHashMap<>();

    private ExcelParser() {
    }

    /**
     * write list to excel rows from file
     */
    public <T> void buildDocument(File file, ExcelWriteRequest request, List<T> datas) throws IOException, InvalidFormatException {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            Workbook workbook = null;
            if (file.length() == 0L) {
                workbook = getWorkbook(file.getName());
            } else {
                fis = new FileInputStream(file);
                workbook = WorkbookFactory.create(fis);
            }

            buildDocument(workbook, request, datas);
            fos = new FileOutputStream(file);
            workbook.write(fos);
            fos.close();
        } catch (IOException e) {
            throw e;
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }

    /**
     * write list to excel rows from workbook
     */
    public <T> void buildDocument(Workbook workbook, ExcelWriteRequest request, List<T> datas) {
        if (workbook == null || request == null || datas == null || datas.size() == 0) {
            return;
        }

        // extract @ExcelField datas
        Class<?> clazz = datas.get(0).getClass();
        List<ExcelPersistentEntity> persistents = getExcelPersistents(clazz);
        if (persistents == null || persistents.size() == 0) {
            return;
        }

        // get or create sheet
        String sheetName = getValueWithDefault(request.getSheetName(), "sheet");
        Sheet sheet = null;
        if ((sheet = workbook.getSheet(sheetName)) == null) {
            sheet = workbook.createSheet(sheetName);
        }

        CellStyle headerCellStyle = workbook.createCellStyle();
        if (request.getHeaderCellStyleConsumer() == null) {
            request.setHeaderCellStyleConsumer(cellStyle -> {
                cellStyle.setAlignment(HorizontalAlignment.CENTER);
                cellStyle.setFillForegroundColor(HSSFColorPredefined.BLUE_GREY.getIndex());
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            });
        }
        request.getHeaderCellStyleConsumer().accept(headerCellStyle);

        // date format
        String excelFormatPattern = DateFormatConverter.convert(Locale.KOREA, getValueWithDefault(request.getDatePattern(), "yyyy-MM-dd"));
        CellStyle cellStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat(excelFormatPattern));

        // write header from ExcelField`s cell name
        int rowCnt = request.getStartRow() >= 0 ? request.getStartRow() : 0;
        int firstCellCnt = 0;
        // Row header = sheet.createRow(rowCnt++);
        Row header = getOrCreateRow(sheet, rowCnt++);
        for (ExcelPersistentEntity persistent : persistents) {
            if (persistent.getFieldType() == ExcelFieldType.Primitive) {
                Cell headerCell = header.createCell(firstCellCnt++);
                headerCell.setCellStyle(headerCellStyle);
                headerCell.setCellValue(persistent.getCellName());
            }
        }

        for (T inst : datas) {
            //Row row = sheet.createRow(rowCnt++);
            Row row = getOrCreateRow(sheet, rowCnt++);
            writeRow(0, persistents, inst, row, cellStyle);
        }

        // adjust cell size
        autoSizeColumns(workbook, request.getStartRow());
    }

    private Row getOrCreateRow(Sheet sheet, int rowIdx) {
        if (sheet != null && rowIdx >= 0) {
            Row row = sheet.getRow(rowIdx);

            if (row == null) {
                row = sheet.createRow(rowIdx);
            }

            return row;
        }

        return null;
    }


    /**
     * Get ExcelPersistentEntity list from class
     */
    private List<ExcelPersistentEntity> getExcelPersistents(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        List<ExcelPersistentEntity> persistents = null;
        boolean cache = true;

        if (cache) {
            persistents = persistentMap.get(clazz);
            if (persistents == null) {
                // locking for multiple thread
                synchronized (lock) {
                    if ((persistents = persistentMap.get(clazz)) == null) {
                        persistents = getPersistentsWithRecursive(clazz);
                        persistentMap.put(clazz, persistents);
                    }
                }
            }
        } else {
            persistents = getPersistentsWithRecursive(clazz);
        }

        return persistents;
    }

    /**
     *
     * @param clazz
     * @return
     */
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
            } else if (persistent.getFieldType() == ExcelFieldType.Primitive) {
                Method[] invokeMethods = getInvokeMethods(persistent.getMethodsName(), clazz);
                if (invokeMethods != null) {
                    persistent.setMethods(invokeMethods);
                }

                result.add(persistent);
            }
        }

        return result;
    }

    /**
     * Parse ExcelField -> ExcelPersistentEntity
     */
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

    /**
     * get invoke methods ["setter method", "getter method"]
     */
    private Method[] getInvokeMethods(String[] invokeMethods, Class<?> clazz) {
        if ((invokeMethods == null) || (invokeMethods.length != 2) || (isEmpty(invokeMethods[0]) && isEmpty(invokeMethods[1]))) {
            return null;
        }

        Method[] ret = new Method[2];
        // setter method
        if (isNotEmpty(invokeMethods[0])) {
            try {
                ret[0] = clazz.getMethod(invokeMethods[0], String.class);
            } catch (NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }
        }

        // getter method
        if (isNotEmpty(invokeMethods[1])) {
            try {
                ret[1] = clazz.getMethod(invokeMethods[1], VOID_PARAM_TYPES);
            } catch (NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }
        }

        return ret;
    }

    /**
     * Write row
     *
     * @param startIdx persistents`s read size
     * @return read index of persistents
     */
    private int writeRow(int startIdx, List<ExcelPersistentEntity> persistents, Object inst, Row row, CellStyle dateCellStyle) {
        int readIdx = startIdx;
        try {
            while (readIdx < persistents.size()) {
                ExcelPersistentEntity persistent = persistents.get(readIdx);
                Field field = persistent.getField();
                field.setAccessible(true);
                if (persistent.getFieldType() == ExcelFieldType.Primitive) {
                    // 1. 기저 사례1) 멤버필드(persistent 정보가 담긴)가 현재 inst의 멤버필드가 아닌 경우 재귀 종료
                    // & readIdx를 -1해줌으르써 이전 재귀에서 다시 사용
                    if (persistent.getInvoker() != inst.getClass()) {
                        return readIdx - 1;
                    }
                    writeCell(persistent, inst, row, dateCellStyle);
                } else if (persistent.getFieldType() == ExcelFieldType.Object) {
                    Object nextInst = null;
                    try {
                        nextInst = field.get(inst);
                    }
                    // 1. 기저사례2) 멤버필드(persistent 정보가 담긴)가 현재 inst의 멤버필드가 아닌경우
                    // & readIdx를 -1해줌으르써 이전 재귀에서 다시 사용
                    catch (IllegalArgumentException e) {
                        return readIdx - 1;
                    }

                    // 2. 멤버필드(persistent 정보가 담긴)가 현재 inst의 멤버필드 인 경우 -> 재귀
                    if (nextInst == null) {
                        // null일 경우, 기본 생성자를 이용하는 인스턴스를 통해 List<Persistent>의 index를 채움
                        nextInst = field.getType().newInstance();
                    }
                    readIdx = writeRow(readIdx + 1, persistents, nextInst, row, dateCellStyle);
                }
                readIdx++;
            }
        } catch (IllegalArgumentException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            return readIdx;
        }
        return readIdx;
    }

    /**
     * Write cell
     */
    private void writeCell(ExcelPersistentEntity persistent, Object inst, Row row, CellStyle dateCellStyle) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        short lastCellNum = row.getLastCellNum();
        if (lastCellNum < 0) {
            lastCellNum = 0;
        }
        Cell cell = row.createCell(lastCellNum);
        Method method = persistent.getterMethod();
        Object value = null;
        if (method != null) {
            value = method.invoke(inst, VOID_PARAMS);
        } else {
            Field field = persistent.getField();
            if (field != null) {
                value = field.get(inst);
            }
        }
        if (value == null) {
            cell.setCellValue("");
        } else {
            if (value instanceof Number) {
                if (value instanceof Double) {
                    cell.setCellValue((double) value);
                } else if (value instanceof Long) {
                    long longVal = (long) value;
                    cell.setCellValue(longVal);
                } else {
                    int intVal = (int) value;
                    cell.setCellValue(intVal);
                }
            }
            if (value instanceof Boolean) {
                cell.setCellValue((Boolean) value);
            } else if (value instanceof Date) {
                cell.setCellValue((Date) value);
                cell.setCellStyle(dateCellStyle);
            } else if (value instanceof String) {
                if (((String) value).toLowerCase().equals("null")) {
                    cell.setCellValue("");
                } else {
                    cell.setCellValue((String) value);
                }
            }
        }
    }

    /**
     * auto size columns from workbook
     */
    private void autoSizeColumns(Workbook workbook, int startRow) {
        startRow = startRow >= 0 ? startRow : 0;
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            if (sheet.getPhysicalNumberOfRows() > 0) {
                Row row = sheet.getRow(startRow);
                if (row != null) {
                    Iterator<Cell> cellIterator = row.cellIterator();
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        int columnIndex = cell.getColumnIndex();
                        sheet.autoSizeColumn(columnIndex);
                    }
                }
            }
        }
    }

    private Workbook getWorkbook(InputStream is, String extension) throws IOException {
        try {
            if ("xls".equalsIgnoreCase(extension)) {
                return new HSSFWorkbook(is);
            } else if ("xlsx".equalsIgnoreCase(extension)) {
                return new XSSFWorkbook(is);
            }
            return null;
        } catch (IOException e) {
            throw e;
        }
    }

    private Workbook getWorkbook(String fileName) {
        String extension = getExtension(fileName);
        if ("xls".equalsIgnoreCase(extension)) {
            return new HSSFWorkbook();
        } else if ("xlsx".equalsIgnoreCase(extension)) {
            return new XSSFWorkbook();
        } else {
            return null;
        }
    }

    private String getExtension(String fileName) {
        if (fileName == null || fileName.length() == 0) {
            return "";
        }

        int dotIdx = fileName.lastIndexOf('.');
        if (dotIdx < 0) {
            return "";
        }

        return fileName.substring(dotIdx + 1);
    }

    private boolean isEmpty(String value) {
        return (value == null) || (value.length() == 0);
    }

    private boolean isNotEmpty(String value) {
        return (value != null) && (value.length() > 0);
    }

    private String getValueWithDefault(String value, String defaultValue) {
        if (isEmpty(value)) {
            return defaultValue;
        }

        return value;
    }
}
