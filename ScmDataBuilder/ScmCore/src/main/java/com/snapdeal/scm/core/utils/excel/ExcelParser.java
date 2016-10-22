package com.snapdeal.scm.core.utils.excel;

import com.google.common.base.Strings;
import com.google.common.primitives.Primitives;
import com.snapdeal.scm.core.utils.StringUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author chitransh
 */
public class ExcelParser {

    private InputStream inputStream;
    private String sheetName;

    public ExcelParser(InputStream inputStream, String sheetName) {
        this.inputStream = inputStream;
        this.sheetName = sheetName;
    }

    public ExcelParser(InputStream inputStream) {
        this(inputStream, null);
    }

    public ExcelParser(File file) throws FileNotFoundException {
        this(file, null);
    }

    public ExcelParser(File file, String sheetName) throws FileNotFoundException {
        this(new FileInputStream(file), sheetName);
    }

    public ExcelParser(String filePath) throws FileNotFoundException {
        this(new File(filePath), null);
    }

    public ExcelParser(String filePath, String sheetName) throws FileNotFoundException {
        this(new File(filePath), sheetName);
    }

    public <T> ExcelParserResponse<T> parse(Class<T> dtoClass) {

        ExcelParserResponse<T> response = new ExcelParserResponse<>();
        try {
            Workbook workbook = WorkbookFactory.create(this.inputStream);
            Sheet sheet = Strings.isNullOrEmpty(this.sheetName) ? workbook.getSheetAt(0) : workbook.getSheet(this.sheetName);

            Iterator<Row> rowIterator = sheet.iterator();
            if (!rowIterator.hasNext()) {
                response.setError("No rows found in the excel sheet including the header rows.");
                return response;
            }

            List<Field> indexedFields;
            try {
                indexedFields = getFieldsIndexedPerHeaders(rowIterator.next(), dtoClass);
            } catch (IllegalArgumentException e) {
                response.setError(e.getMessage());
                return response;
            }

            while (rowIterator.hasNext()) {
                T dtoObject;
                try {
                    Constructor<T> constructor = dtoClass.getConstructor();
                    constructor.setAccessible(true);
                    dtoObject = constructor.newInstance();
                } catch (NoSuchMethodException e) {
                    response.setError("No-arg constructor not found for the DTO class: " + dtoClass.getName());
                    return response;
                } catch (IllegalAccessException e) {
                    response.setError("Unable to access the no-arg constructor for the DTO class: " + dtoClass.getName());
                    return response;
                } catch (InstantiationException e) {
                    response.setError("Unable to instantiate the no-arg constructor for the DTO class: " + dtoClass.getName());
                    return response;
                } catch (InvocationTargetException e) {
                    response.setError("Unable to invoke the no-arg constructor for the DTO class: " + dtoClass.getName());
                    return response;
                }

                Row row = rowIterator.next();
                int columnIndex = 0;

                while (columnIndex < indexedFields.size()) {
                    Cell cell = row.getCell(columnIndex);
                    if (cell != null) {
                        Field field = indexedFields.get(columnIndex);
                        Object typedFieldValue;

                        try {
                            typedFieldValue = getFieldValueAsPerType(cell, field);
                        } catch (IllegalArgumentException e) {
                            response.setError(e.getMessage());
                            return response;
                        }
                        if (typedFieldValue != null) {
                            try {
                                field.setAccessible(true);
                                field.set(dtoObject, typedFieldValue);
                            } catch (IllegalAccessException e) {
                                response.setError("Unable to set the value for field: " + field.getName());
                                return response;
                            }
                        }
                    }
                    columnIndex++;
                }
                response.addRecord(dtoObject);
            }
        } catch (IOException e) {
            response.setError("Unable to open the input stream for reading.");
            return response;
        } catch (InvalidFormatException e) {
            response.setError("The input stream is not in valid xls or xlsx format.");
            return response;
        } catch (EncryptedDocumentException e) {
            response.setError("The input stream is encrypted; unable  to read file.");
            return response;
        } finally {
            try {
                this.inputStream.close();
            } catch (IOException ignored) {
            }
        }
        response.setSuccessful(true);
        return response;
    }

    private static Object getFieldValueAsPerType(Cell cell, Field field) throws IllegalArgumentException {

        String fieldName = field.getName();
        Class<?> type = field.getType();
        if (type.isPrimitive()) {
            type = Primitives.wrap(type);
        }

        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_BOOLEAN:
                if (type.isAssignableFrom(Boolean.class)) {
                    return cell.getBooleanCellValue();
                } else {
                    throw new IllegalArgumentException("Cell type is boolean, but didn't find boolean value for field: " + fieldName);
                }

            case Cell.CELL_TYPE_NUMERIC:
                Double numericValue = cell.getNumericCellValue();
                if (type.isAssignableFrom(Byte.class)) {
                    return numericValue.byteValue();
                } else if (type.isAssignableFrom(Short.class)) {
                    return numericValue.shortValue();
                } else if (type.isAssignableFrom(Integer.class)) {
                    return numericValue.intValue();
                } else if (type.isAssignableFrom(Long.class)) {
                    return numericValue.longValue();
                } else if (type.isAssignableFrom(Float.class)) {
                    return numericValue.floatValue();
                } else if (type.isAssignableFrom(Double.class)) {
                    return numericValue;
                } else if (type.isAssignableFrom(Date.class) && HSSFDateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else {
                    throw new IllegalArgumentException("Cell type is numeric, but didn't find numeric value for field: " + fieldName + " with type: " + type.getName());
                }
            case Cell.CELL_TYPE_STRING:
                String stringValue = cell.getStringCellValue();
                if (type.isAssignableFrom(String.class)) {
                    return stringValue;
                } else if (type.isAssignableFrom(Character.class)) {
                    if (stringValue.length() == 1) {
                        return stringValue.charAt(0);
                    } else {
                        throw new IllegalArgumentException("Field type is character, but instead found string of length greater than 1, for field: " + fieldName + " with value: " + stringValue);
                    }
                } else if (type.isAssignableFrom(TimeUnit.class)) {
                    try {
                        return TimeUnit.valueOf(stringValue);
                    } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException("Field type is character, but instead found invalid, for field: " + fieldName + " with value: " + stringValue);
                    }
                } else {
                    throw new IllegalArgumentException("Cell type is string, but didn't find matching field type: " + fieldName);
                }
            case Cell.CELL_TYPE_BLANK:
                return null;
            default:
                throw new IllegalArgumentException("Unknown type found: " + type.getName() + " for field: " + fieldName);
        }
    }

    private static <T> List<Field> getFieldsIndexedPerHeaders(Row headerRow, Class<T> dtoClass) throws IllegalArgumentException {

        List<Field> indexedFields = new LinkedList<>();

        for (Cell column : headerRow) {
            String columnName = column.getStringCellValue();
            Field field = getFieldWithName(columnName, dtoClass);
            if (field == null) {
                throw new IllegalArgumentException("Unknown field: " + columnName + " found.");
            }
            indexedFields.add(field);
        }

        if (indexedFields.isEmpty()) {
            throw new IllegalArgumentException("No headers found for processing the DTO.");
        }
        return indexedFields;
    }

    private static <T> Field getFieldWithName(String fieldName, Class<T> dtoClass) {

        Field[] fields = dtoClass.getDeclaredFields();
        for (Field field : fields) {
            String absoluteFieldName = getAbsoluteString(field.getName());
            if (absoluteFieldName.equals(getAbsoluteString(fieldName))) {
                return field;
            }
        }
        return null;
    }

    private static String getAbsoluteString(String str) {
        return Strings.isNullOrEmpty(str) ? str : StringUtils.removeNonWordChars(str).toLowerCase();
    }
}
