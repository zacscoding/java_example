package org.excelparser.test;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.excelparser.ExcelParser;
import org.excelparser.domain.Address;
import org.excelparser.domain.Company;
import org.excelparser.domain.ExcelWriteRequest;
import org.excelparser.domain.Person;
import org.junit.Test;

/**
 * @author zacconding
 * @Date 2018-03-13
 * @GitHub : https://github.com/zacscoding
 */
public class ExcelWriteTest {

    @Test
    public void read() throws Exception {
        File file = new File("D:\\excel", "test.xls");
        InputStream is = new FileInputStream(file);
        Workbook wb = WorkbookFactory.create(is);
        System.out.println(wb);

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


    @Test
    public void write() throws Exception {
        // create persons
        int count = 20;
        List<Person> persons = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            persons.add(createPerson());
        }

        // write person to excel
        int startRow = 0;
        for (int i = 0; i < 3; i++) {
            File file = new File("D:\\excel", "test.xls");
            ExcelWriteRequest request = new ExcelWriteRequest();
            request.setSheetName("TestSheet");
            request.setStartRow(startRow);
            System.out.println("request : " + request);
            ExcelParser.INSTANCE.buildDocument(file, request, persons);
            startRow += persons.size() + 4;
        }
    }

    private Person createPerson() {
        Person p = new Person();

        p.setAge(random(1, 50));
        p.setName("name" + random(1, 50));
        p.setAddress(createAddress());
        p.setHobbies(createHobbies());
        p.setCompany(createCompany());

        return p;
    }

    private Company createCompany() {
        Company c = new Company();

        c.setName("Company" + random(1, 10));
        c.setSalary(random(2000, 5000));

        return c;
    }

    private Address createAddress() {
        Address a = new Address();
        a.setCity("City" + random(1, 10));
        String code = "";
        for (int i = 0; i < 6; i++) {
            code += random(0, 10);
        }
        a.setZipCode(code);

        return a;
    }

    private List<String> createHobbies() {
        int repeat = random(0, 5);
        List<String> hobbies = new ArrayList<>();

        for (int i = 0; i < repeat; i++) {
            hobbies.add("hobby" + random(1, 100));
        }

        return hobbies;
    }


    private int random(int start, int range) {
        return (int) (Math.random() * range) + start;
    }

}
