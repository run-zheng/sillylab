package com.sillylab.common.utils;

/**
 * @author run-zheng
 * @description: 枚举辅助接口
 * @date 2022/4/7 12:38
 */
public interface ITypeEnum<T> {
    /**
     * 获取类型
     * @return
     */
    T getType();

    /**
     * 判断是否是该类型的枚举值
     * @param type
     * @return
     */
    default boolean isEqualsTo(T type){
        return this.getType().equals(type);
    }

    /**
     * 判断是否不是该类型的枚举值
     * @param type
     * @return
     */
    default boolean isNotEqualsTo(T type){
        return !this.isEqualsTo(type);
    }

    /**
     * 判断是否是该类型， 字符串形式比较
     * @param typeString
     * @return
     */
    default boolean isEqualsToByStr(String typeString){
        String thisTypeStr = String.valueOf(getType());
        if(thisTypeStr == typeString){
            return true;
        }
        if(typeString == null){
            return false;
        }
        if(thisTypeStr.length() != typeString.length()){
            return false;
        }
        final int length = thisTypeStr.length();
        for (int i = 0; i < length; i++){
            if(thisTypeStr.charAt(i) != typeString.charAt(i)){
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否不是是该类型， 字符串形式比较
     * @param typeString
     * @return
     */
    default boolean isNotEqualsToByStr(String typeString){
        return !isEqualsToByStr(typeString);
    }
}
