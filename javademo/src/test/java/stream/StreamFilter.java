package stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import org.junit.Before;
import org.junit.Test;

/**
 * @author zacconding
 * @Date 2018-02-06
 * @GitHub : https://github.com/zacscoding
 */
public class StreamFilter {

    List<Student> students;

    @Before
    public void setUp() {
        students = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            students.add(new Student("name" + i, i));
        }
    }

    @Test
    public void mapToInt() {
        double average = students.stream().mapToInt(student -> {
            return student.getScore();
        }).average().getAsDouble();
        System.out.println(average);
    }

    @Test
    public void filter() {
        int sum = students.stream().filter(stu -> {
            return stu.getScore() % 2 == 0;
        }).mapToInt(Student::getScore).sum();
        System.out.println("Sum of even scores : " + sum);
    }

    @Test
    public void distict() {
        List<String> names = Arrays.asList("aaa", "bbb", "ccc", "aaa");
        names.stream().distinct().forEach(name -> System.out.println(name));
    }

    @Test
    public void flatMap() {
        List<String> values = Arrays.asList("token1 token2", "token3", "token4 token5");
        values.stream().flatMap(data -> {
            System.out.println("check : " + data);
            return Arrays.stream(data.split(" "));
        }).forEach(token -> System.out.println(token));
    }

    @Test
    public void asXXXStream() {
        int[] intArr = new int[]{1, 2, 3};
        Arrays.stream(intArr).asDoubleStream().forEach(doubleVal -> System.out.print(doubleVal + " "));
        System.out.println();
        Arrays.stream(intArr).boxed().forEach(integerInst -> System.out.print(integerInst.intValue() + " "));
    }
}
