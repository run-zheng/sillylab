package com.sillylab.transaction.template;

import com.sillylab.transaction.template.dao.TestRepository;
import com.sillylab.transaction.template.entity.TestInfo;
import com.sillylab.transaction.template.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TransactionTemplateApplicationTests {
    @Autowired
    private TestRepository testRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TransactionExecutorTemplate executorTemplate;

    @Test
    public void testRollbackWhenException(){
        Exception exception = assertThrows(
                RuntimeException.class,
                () -> testRepository.test(a -> {
                    throw new RuntimeException("test");
                }));
        assertEquals("test", exception.getMessage());
        List<User> users = testRepository.getUsers();
        List<TestInfo> tests = testRepository.getTestInfos();
        assertTrue(users.isEmpty());
        assertTrue(tests.isEmpty());
    }

    @Test
    public void testNormalInserAll() {
        executorTemplate.executeRequired(() -> {
            testRepository.insertTest(createTest());
            testRepository.insertUser(createUser());
        });
        List<User> users = testRepository.getUsers();
        List<TestInfo> tests = testRepository.getTestInfos();
        assertTrue(users.size() == 1 );
        assertTrue(tests.size()  == 1 );
        clearData();
    }

    private void clearData() {
        executorTemplate.executeRequired(() -> {
            jdbcTemplate.execute("delete from test");
            jdbcTemplate.execute("delete from user");
        });
    }

    @Test
    public void testDeleteByRequired(){
        executorTemplate.executeRequired(() -> {
            testRepository.insertTest(createTest());
            testRepository.insertUser(createUser());
        });
        List<User> users = testRepository.getUsers();
        List<TestInfo> tests = testRepository.getTestInfos();
        assertTrue(users.size() == 1 );
        assertTrue(tests.size()  == 1 );
        clearData();
        users = testRepository.getUsers();
        tests = testRepository.getTestInfos();
        assertTrue(users.isEmpty());
        assertTrue(tests.isEmpty());
    }

    @Test
    public void testRequiredNew() {
        executorTemplate.executeRequired(() -> {
            testRepository.insertTest(createTest());
            executorTemplate.executeRequiresNew(() -> {
                testRepository.insertUser(createUser());
            });
        });
        List<User> users = testRepository.getUsers();
        List<TestInfo> tests = testRepository.getTestInfos();
        assertTrue(users.size() == 1 );
        assertTrue(tests.size()  == 1);
        clearData();
    }

    @Test
    public void testRequiredNewInnerRollback() {
        executorTemplate.executeRequired(() -> {
            testRepository.insertTest(createTest());
            try {
                executorTemplate.executeRequiresNew(() -> {
                    testRepository.insertUser(createUser());
                    throw new RuntimeException();
                });
            }catch (Exception e){
                //ignore
            }
        });
        List<User> users = testRepository.getUsers();
        List<TestInfo> tests = testRepository.getTestInfos();
        assertTrue(users.size() == 0 );
        assertTrue(tests.size()  == 1);
        clearData();
    }

    @Test
    public void testRequiredNewRollback() {
        try {
            executorTemplate.executeRequired(() -> {
                testRepository.insertTest(createTest());
                    executorTemplate.executeRequiresNew(() -> {
                        testRepository.insertUser(createUser());
                    });
                throw new RuntimeException();
            });
        }catch (Exception e){
            //ignore
        }
        List<User> users = testRepository.getUsers();
        List<TestInfo> tests = testRepository.getTestInfos();
        assertTrue(users.size() == 1 );
        assertTrue(tests.size()  == 0);
        clearData();
    }

    @Test
    public void testRequiredWithReturn(){
        Integer result = executorTemplate.executeRequired(() -> {
            return testRepository.insertTest(createTest());
        });
        assertEquals(1, result.intValue());
        List<TestInfo> tests = testRepository.getTestInfos();
        assertTrue(tests.size()  == 1 );
        clearData();
        tests = testRepository.getTestInfos();
        assertTrue(tests.isEmpty());
    }

    @Test
    public void testRequireChain() {
        ITransactionExecutor executor = TransactionExecutorTemplate.of(() -> {
            testRepository.insertTest(createTest());
        });
        //do something
        executor.andThen(() -> {
            testRepository.insertUser(createUser());
        });
        //do something else
        executorTemplate.executeRequired(executor);
        List<User> users = testRepository.getUsers();
        List<TestInfo> tests = testRepository.getTestInfos();
        assertTrue(users.size() == 1 );
        assertTrue(tests.size()  == 1 );
        clearData();
        users = testRepository.getUsers();
        tests = testRepository.getTestInfos();
        assertTrue(users.isEmpty());
        assertTrue(tests.isEmpty());
    }

    private User createUser() {
        User user = new User();
        user.setName("3"+System.currentTimeMillis());
        user.setAge(new Random().nextInt(100));
        return user;
    }

    private TestInfo createTest() {
        TestInfo testInfo = new TestInfo();
        testInfo.setTitle("1"+System.currentTimeMillis());
        testInfo.setAuthor("2"+System.currentTimeMillis());
        return testInfo;
    }


}
