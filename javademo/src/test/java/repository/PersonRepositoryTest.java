package repository;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2018-09-17
 * @GitHub : https://github.com/zacscoding
 */
public class PersonRepositoryTest {

    private int saveCount = 100;
    private int saveNameRequest = 10000;
    private int notSavedNameRequest = 10000;
    List<PersonForRepository> persons;
    List<String> names;

    @Before
    public void setUp() throws Exception {
        names = new LinkedList<>();
        File file = new ClassPathResource("repository/name.txt").getFile();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String readLine = null;
        while ((readLine = br.readLine()) != null) {
            String name = new StringTokenizer(readLine, " ").nextToken();
            names.add(name);
        }

        persons = new ArrayList<>(saveCount);

        for (int i = 0; i < saveCount; i++) {
            PersonForRepository person = new PersonForRepository();
            person.setId(UUID.randomUUID().toString());
            person.setName(names.get(i));
            persons.add(person);
        }
    }

    @Test
    public void doTask() throws Exception{
        /*
        Saved record : 100 | Saved request : 10000 | Not saved request : 10000
        Default : 2223 [ms]
        Cache : 1643 [ms]
         */
        SimpleLogger.println("Saved record : {} | Saved request : {} | Not saved request : {}", saveCount, saveNameRequest, notSavedNameRequest);
        PersonRepository defaultRepo = new InmemoryPersonRepository();
        PersonRepository cacheRepo = new InmemoryCachePersonRepository();
        assertTrue(defaultRepo.saves(persons) == persons.size());
        assertTrue(cacheRepo.saves(persons) == persons.size());

        SimpleLogger.println("Default : {} [ms]", findTask(defaultRepo));
        SimpleLogger.println("Cache : {} [ms]", findTask(cacheRepo));
    }

    public long findTask(PersonRepository personRepository) throws Exception {
        Random random = new Random();
        Thread[] savedReq = new Thread[saveNameRequest];
        Thread[] notSavedReq = new Thread[notSavedNameRequest];
        for (int i = 0; i < saveNameRequest; i++) {
            savedReq[i] = new Thread(() -> personRepository.findByName(names.get(random.nextInt(saveCount))));
            savedReq[i].setDaemon(true);
        }

        for (int i = 0; i < notSavedNameRequest; i++) {
            notSavedReq[i] = new Thread(() -> personRepository.findByName(names.get(random.nextInt(names.size() - saveCount) + saveCount)));
            notSavedReq[i].setDaemon(true);
        }

        long start = System.currentTimeMillis();
        for (int i = 0; i < saveNameRequest; i++) {
            savedReq[i].start();
        }
        for (int i = 0; i < notSavedNameRequest; i++) {
            notSavedReq[i].start();
        }

        for (int i = 0; i < saveNameRequest; i++) {
            savedReq[i].join();
        }
        for (int i = 0; i < notSavedNameRequest; i++) {
            notSavedReq[i].join();
        }
        long end = System.currentTimeMillis();

        return end - start;
    }

}
