package repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * @author zacconding
 * @Date 2018-09-17
 * @GitHub : https://github.com/zacscoding
 */
public class InmemoryPersonRepository implements PersonRepository {

    private ReentrantReadWriteLock lock;
    private List<PersonForRepository> savedPersons;

    public InmemoryPersonRepository() {
        this.lock = new ReentrantReadWriteLock();
        this.savedPersons = new LinkedList<>();
    }


    @Override
    public int save(PersonForRepository person) {
        if (person == null) {
            return 0;
        }

        try {
            lock.writeLock().lock();
            PersonForRepository find = findOneById(person.getId());
            if (find != null) {
                return 0;
            }

            savedPersons.add(person);
            return 1;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public int saves(List<PersonForRepository> persons) {
        if (persons == null || persons.size() == 0) {
            return 0;
        }

        try {
            lock.writeLock().lock();
            int success = 0;
            for (PersonForRepository person : persons) {
                if (findOneById(person.getId()) != null) {
                    continue;
                }
                savedPersons.add(person);
                success++;
            }

            return success;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public PersonForRepository findOneById(String id) {
        if (id == null || id.length() == 0) {
            return null;
        }

        try {
            lock.readLock().lock();

            for (PersonForRepository person : savedPersons) {
                if (person.getId().equals(id)) {
                    return person;
                }
            }

            return null;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<PersonForRepository> findByName(String name) {
        if (name == null || name.length() == 0) {
            return Collections.emptyList();
        }

        try {
            lock.readLock().lock();
            List<PersonForRepository> ret = new ArrayList<>();
            for (PersonForRepository person : savedPersons) {
                if (person.getName().equals(name)) {
                    ret.add(person);
                }
            }

            return ret;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<PersonForRepository> findAll() {
        try {
            lock.readLock().lock();
            return savedPersons.stream().collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }
}