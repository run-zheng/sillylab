package com.sillylab.transaction.template;

import com.sillylab.transaction.template.dao.TestRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

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
    public void testService() {
        Exception exception = assertThrows(
                RuntimeException.class,
                () -> testRepository.test(a -> {
                        throw new RuntimeException("test");
                    }));
        assertEquals("test", exception.getMessage());
        List<User> users = getUsers();
        List<TestInfo> tests = getTestInfos();
        assertTrue(users.isEmpty());
        assertTrue(tests.isEmpty());

        testRepository.test(a -> { });
        users = getUsers();
        tests = getTestInfos();
        assertTrue(users.size() == 1 );
        assertTrue(tests.size()  == 1 );


        executorTemplate.executeRequired(() ->{
            jdbcTemplate.execute("delete from test");
            jdbcTemplate.execute("delete from user");
        });
    }

    private List<TestInfo> getTestInfos() {
        List<TestInfo> tests = jdbcTemplate.query("select * from test", new RowMapper<TestInfo>() {
            @Override
            public TestInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new TestInfo(rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getDate("create_time"),
                        rs.getDate("update_time"));
            }
        });
        return tests;
    }

    private List<User> getUsers() {
        List<User> users = jdbcTemplate.query("select * from user", new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new User(rs.getLong("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getDate("create_time"),
                        rs.getDate("update_time"));
            }
        });
        return users;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestInfo{
        private Long id;
        private String title;
        private String author;
        private Date createTime;
        private Date updateTime;
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class User {
        private Long id ;
        private String name;
        private Integer age ;
        private Date createTime;
        private Date updateTime;
    }

}
