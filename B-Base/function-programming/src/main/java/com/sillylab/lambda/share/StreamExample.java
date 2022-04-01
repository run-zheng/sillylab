package com.sillylab.lambda.share;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StreamExample {
    public final static Set<String> NON_WORDS = new HashSet<String>(){{
        add("the"); add("and"); add("of"); add("to");
        add("a"); add("i"); add("it"); add("in");
        add("or"); add("is"); add("d"); add("s");
        add("as"); add("so"); add("but"); add("be");
    }};
    public static void main(String[] args) {
        String words = "Simplified Chinese Edition jointly published by OReilly Media Inc and Posts Telecom " +
                "Press  2015  Authorized translation of the English edition 2014 OReilly Media,Inc the " +
                "owner of all rights to publish and sell the same";
        System.out.println(wordFreq(words));
        System.out.println(wordFreqByFunction(words));
    }

    public static Map<String, Long> wordFreq(String words){
        TreeMap<String, Long> wordMap = new TreeMap<>();
        String[] strs = Pattern.compile("\\s").split(words);
        for (String s: strs) {
            String word = s.toLowerCase();
            if(!word.trim().isEmpty() && !NON_WORDS.contains(word)){
                if(wordMap.get(word) == null){
                    wordMap.put(word, 1L);
                }else {
                    wordMap.put(word, wordMap.get(word)+1);
                }
            }
        }
        return wordMap;
    }

    public static Map<String, Long> wordFreqByFunction(String words){
        TreeMap<String, Long> collect = Pattern.compile("\\s")
                .splitAsStream(words)
                .map(w -> w.toLowerCase())
                .filter(w -> !w.trim().isEmpty() && !NON_WORDS.contains(w))
                .collect(Collectors.groupingBy(String::valueOf, TreeMap::new, Collectors.counting()));
        return collect;
    }
}
