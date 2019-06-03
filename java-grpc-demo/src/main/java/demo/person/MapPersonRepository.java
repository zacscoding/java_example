package demo.person;

import demo.util.SimpleLogger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * @GitHub : https://github.com/zacscoding
 */
public class MapPersonRepository implements PersonRepository {

    private Map<Long, Person> persons = new HashMap<>();
    private AtomicLong idGenerator = new AtomicLong(0L);
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public Person save(Person person) {
        try {
            lock.writeLock().lock();
            //SimpleLogger.println("Save person : {}", person);
            person.setId(idGenerator.getAndIncrement());
            persons.put(person.getId(), clone(person));

            return person;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Optional<Person> findById(Long id) {
        try {
            lock.readLock().lock();
            SimpleLogger.println("Find person. id : {}", id);
            Person find = persons.get(id);
            return Optional.ofNullable(clone(find));
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<Person> findAllByName(final String name) {
        try {
            lock.readLock().lock();

            SimpleLogger.println("Find persons. name : {}", name);

            if (persons.isEmpty()) {
                return Collections.emptyList();
            }

            return persons.values().stream()
                .filter(p -> p.getName().equals(name))
                .map(p -> clone(p))
                .collect(Collectors.toCollection(ArrayList::new));
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Person deleteById(Long id) {
        try {
            lock.writeLock().lock();
            SimpleLogger.println("Delete person. id7 : {}", id);
            Person person = persons.remove(id);
            return person;
        } finally {
            lock.writeLock().unlock();
        }
    }

    private Person clone(Person person) {
        if (person == null) {
            return null;
        }

        return Person.builder()
            .id(person.getId())
            .name(person.getName())
            .age(person.getAge())
            .build();
    }
}