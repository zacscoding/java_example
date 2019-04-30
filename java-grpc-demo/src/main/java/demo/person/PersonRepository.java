package demo.person;

import java.util.List;
import java.util.Optional;

/**
 * @GitHub : https://github.com/zacscoding
 */
public interface PersonRepository {

    Person save(Person person);

    Optional<Person> findById(Long id);

    List<Person> findAllByName(String name);

    Person deleteById(Long id);
}
