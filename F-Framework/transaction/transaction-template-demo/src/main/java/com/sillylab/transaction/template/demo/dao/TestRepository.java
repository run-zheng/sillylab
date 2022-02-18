package com.sillylab.transaction.template.demo.dao;

import com.sillylab.transaction.template.TransactionExecutorTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

@Repository
public class TestRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TransactionExecutorTemplate  transactionExecutorTemplate;

    public void test() {
        transactionExecutorTemplate.executeRequired(() -> {
            String addTest = "insert into test(title, author, create_time, update_time) value(?, ?, now(), now())";

            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement ps = con.prepareStatement(addTest, new String[]{});
                    ps.setString(1,""+System.currentTimeMillis()+1);
                    ps.setString(2,""+System.currentTimeMillis()+2);
                    return ps;
                }
            });

            int i = 1 / new Random().nextInt(10) % 4;

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
