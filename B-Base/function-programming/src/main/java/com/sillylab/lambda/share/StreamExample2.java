package com.sillylab.lambda.share;

import com.sillylab.lambda.Common;

import java.util.*;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

public class StreamExample2 extends Common {


    public static void main(String[] args) {
        List<Dish> lowCaloricDishs = new ArrayList<>();
        for(Dish d: menu){
            if(d.getCalories() < 400){
                lowCaloricDishs.add(d);
            }
        }
        Collections.sort(lowCaloricDishs, new Comparator<Dish>() {
            @Override
            public int compare(Dish o1, Dish o2) {
                return Integer.compare(o1.getCalories(), o2.getCalories());
            }
        });
        List<String> lowCalorisDishesName = new ArrayList<>();
        for(Dish d: lowCaloricDishs){
            lowCalorisDishesName.add(d.getName());
        }
        //java 8
        List<String> lowCaloricDishesName = menu.stream()
                .filter(d -> d.getCalories() < 400)
                .sorted(comparing(Dish::getCalories))
                .map(Dish::getName)
                .collect(toList());

        List<String> lowCaloricDishesNameByParallel = menu.parallelStream()
                .filter(d -> d.getCalories() < 400)
                .sorted(comparing(Dish::getCalories))
                .map(Dish::getName)
                .collect(toList());
    }
}
