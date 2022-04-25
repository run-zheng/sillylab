package com.sillylab.common.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author run-zheng
 * @date 2022/4/25 16:34
 */
public class ITypeEnumTest {

    @Getter
    @AllArgsConstructor
    public enum TestEnum implements ITypeEnum<Integer>{
        First(1, "First"), Second(2, "Second");

        private Integer type;
        private String text;
    }

    @Test
    public void test(){
        assertTrue(TestEnum.First.isEqualsTo(1));
        assertTrue(TestEnum.First.isNotEqualsTo(2));
        assertTrue(TestEnum.First.isEqualsToByStr("1"));
        assertTrue(TestEnum.First.isNotEqualsToByStr("2"));
    }
}
