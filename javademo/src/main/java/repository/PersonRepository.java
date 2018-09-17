package repository;

import java.util.List;

/**
 * @author zacconding
 * @Date 2018-09-17
 * @GitHub : https://github.com/zacscoding
 */
public interface PersonRepository {

    int save(PersonForRepository person);

    int saves(List<PersonForRepository> persons);

    PersonForRepository findOneById(String id);

    List<PersonForRepository> findByName(String name);

    List<PersonForRepository> findAll();
}