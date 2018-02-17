package functional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.junit.Before;
import org.junit.Test;
import util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2018-02-17
 * @GitHub : https://github.com/zacscoding
 */
public class UnderScoreTest {

    List<Integer> numbers;
    List<UnderScoreTestPerson> persons;

    @Before
    public void setUp() {

        numbers = new ArrayList<>(201);
        IntStream.range(-100, 101).forEach(i -> {
            numbers.add(i);
        });

        persons = new ArrayList<UnderScoreTestPerson>(10);
        IntStream.range(1, 11).forEach(i -> {
            persons.add(new UnderScoreTestPerson("name" + i, i));
        });

    }

    @Test
    public void map() {
        List<String> names = UnderScore.map(persons, underScoreTestPerson -> underScoreTestPerson.getName());
        assertTrue(names.size() == 10);

        List<Integer> ages = UnderScore.map(persons, underScoreTestPerson -> underScoreTestPerson.getAge());
        assertTrue(ages.size() == 10);
    }


    @Test
    public void filter() {
        List<Integer> positiveEvens = UnderScore.filter(numbers, integer -> integer > 0 && integer % 2 == 0);
        assertTrue(positiveEvens.size() == 50);

        List<Integer> lessthan90 = UnderScore.filter(numbers, integer -> integer < 90);
        assertTrue(lessthan90.size() == 190);

        assertTrue(UnderScore.filter(null, null).size() == 0);
        assertTrue(UnderScore.filter(Collections.emptyList(), null).size() == 0);
        assertTrue(UnderScore.filter(numbers, null).size() == 0);

    }

    @Test
    public void findTest() {
        Integer find1 = UnderScore.find(numbers, integer -> integer.equals(100));
        assertThat(Integer.valueOf(100), is(find1));

        Integer findNotExist = UnderScore.find(numbers, integer -> integer.equals(101));
        assertNull(findNotExist);
    }

    @Test
    public void findIndexTest() {
        int idx1 = UnderScore.findIndex(numbers, integer -> integer.equals(100));
        assertTrue(idx1 == numbers.indexOf(Integer.valueOf(100)));

        int idx2 = UnderScore.findIndex(numbers, integer -> integer.equals(200));
        assertTrue(idx2 == -1);
    }
}

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
class UnderScoreTestPerson {

    private String name;
    private Integer age;
}