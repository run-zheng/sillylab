package com.sillylab.transaction.template.dao;

import com.sillylab.transaction.template.TransactionExecutorTemplate;
import com.sillylab.transaction.template.entity.TestInfo;
import com.sillylab.transaction.template.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

@Repository
public class TestRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TransactionExecutorTemplate transactionExecutorTemplate;

    public int insertTest(TestInfo test) {
        String addTest = "insert into test(title, author, create_time, update_time) values(?, ?, now(), now())";
        return jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(addTest, new String[]{});
                ps.setString(1,test.getTitle());
                ps.setString(2,test.getAuthor());
                return ps;
            }
        });
    }

    public int insertUser(User user) {
        String addUser = "insert into user(name, age, create_time, update_time) values(?, ?, now(), now())";
        return jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(addUser, new String[]{});
                ps.setString(1,user.getName());
                ps.setInt(2,user.getAge());
                return ps;
            }
        });
    }

    public List<TestInfo> getTestInfos() {
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

    public List<User> getUsers() {
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

    public void test(Consumer consumer) {
        transactionExecutorTemplate.executeRequired(() -> {
            consumer.accept(null);

            String addUser = "insert into user(name, age, create_time, update_time) values(?, ?, now(), now())";
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement ps = con.prepareStatement(addUser, new String[]{});
                    ps.setString(1,""+System.currentTimeMillis()+1);
                    ps.setInt(2, new Random().nextInt(100));
                    return ps;
                }
            });
        });

    }
}
