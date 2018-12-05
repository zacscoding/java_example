package unitils;

import static org.junit.Assert.assertEquals;
import static org.unitils.reflectionassert.ReflectionAssert.assertPropertyLenientEquals;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.unitils.reflectionassert.ReflectionComparatorMode;

/**
 * @author zacconding
 * @Date 2018-12-03
 * @GitHub : https://github.com/zacscoding
 */
public class ReflectionEqualsTest {

    @Test
    public void test_assertEquals() {
        Book book1 = new Book("book", 1000);
        Book book2 = new Book("book", 1000);

        // fail
        assertEquals(book1, book2);
    }

    @Test
    public void test_assertReflectionEquals() {
        Book book1 = new Book("book", 1000);
        Book book2 = new Book("book", 1000);

        assertReflectionEquals("Book compare", book1, book2);
    }

    @Test
    public void test_comparatorMode() throws Exception {
        // test ReflectionComparatorMode.LENIENT_ORDER
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(3, 2, 1);
        assertReflectionEquals("Compare list", list1, list2, ReflectionComparatorMode.LENIENT_ORDER);

        // test ReflectionComparatorMode.IGNORE_DEFAULTS (expected
        Book book1 = new Book(null, 1000);
        Book book2 = new Book("Book1", 1000);

        assertReflectionEquals("Compare book", book1, book2, ReflectionComparatorMode.IGNORE_DEFAULTS);

        // test ReflectionComparatorMode.LENIENT_DATES
        Order order1 = new Order(100, "order1");
        TimeUnit.MILLISECONDS.sleep(1L);
        Order order2 = new Order(100, "order1");

        assertReflectionEquals("Compare Order", order1, order2, ReflectionComparatorMode.LENIENT_DATES);
    }

    @Test
    public void test_assertPropertyLenientEquals() {
        Player player = new Player("hivava", 31, 15);

        assertPropertyLenientEquals("age", 31, player);
        assertPropertyLenientEquals("experienceYear", 15, player);
    }

    public static class Player {
        private String name;
        private int age;
        private int experienceYear;

        public Player(String name, int age, int experienceYear) {
            this.name = name;
            this.age = age;
            this.experienceYear = experienceYear;
        }

        public String getName() {
            return this.name;
        }

        public int getAbilityPoint() {
            return (30 - this.age) + experienceYear;
        }
    }


    public static class Order {

        private int price;
        private String name;
        private Date date;

        public Order(int price, String name) {
            this.price = price;
            this.name = name;
            this.date = new Date();
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }
    }

    public static class Book {

        private String name;
        private int price;

        public Book(String name, int price) {
            this.name = name;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }
    }

}
