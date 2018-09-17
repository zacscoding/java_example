package repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * @author zacconding
 * @Date 2018-09-17
 * @GitHub : https://github.com/zacscoding
 */
public class InmemoryCachePersonRepository implements PersonRepository {

    private ReentrantReadWriteLock lock;
    private List<PersonForRepository> savedPersons;
    // caches
    private Map<String, List<Integer>> nameCache;
    private Map<String, Integer> idCache;

    public InmemoryCachePersonRepository() {
        this.lock = new ReentrantReadWriteLock();
        this.savedPersons = new LinkedList<>();
        this.nameCache = new HashMap<>();
        this.idCache = new HashMap<>();
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

            if (savedPersons.add(person)) {
                pushPersonCache(person, Integer.valueOf(savedPersons.size() - 1));
                return 1;
            }
        } finally {
            lock.writeLock().unlock();
        }

        return 0;
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

                if (savedPersons.add(person)) {
                    pushPersonCache(person, Integer.valueOf(savedPersons.size() - 1));
                    success++;
                }
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
            Integer idx = idCache.get(id);
            return idx == null ? null : savedPersons.get(idx);
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
            List<Integer> indices = nameCache.get(name);
            if (indices == null || indices.size() == 0) {
                return Collections.emptyList();
            }

            List<PersonForRepository> persons = new ArrayList<>(indices.size());
            for (Integer idx : indices) {
                persons.add(savedPersons.get(idx));
            }

            return persons;
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

    private void pushPersonCache(PersonForRepository person, Integer idx) {
        // id
        idCache.put(person.getId(), idx);

        // name
        List<Integer> nameIndices = nameCache.get(person.getName());
        if (nameIndices == null) {
            nameIndices = new LinkedList<>();
            nameCache.put(person.getName(), nameIndices);
        }
        nameIndices.add(idx);
    }
}