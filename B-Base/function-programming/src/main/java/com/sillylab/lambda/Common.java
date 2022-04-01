package com.sillylab.lambda;

import java.util.Arrays;
import java.util.Currency;
import java.util.List;

public class Common {
    public static void printLine(){
        System.out.println("-------------------------------------------");
    }
    public static void printLine(String msg){
        System.out.println("----------------------"+msg+"---------------------");
    }

    public static class Dish {
        private final String name;
        private final boolean vegetarian;
        private final int calories;
        private final Type type;

        public Dish(String name, boolean vegetarian , int calories, Type type) {
            this.name = name;
            this.vegetarian = vegetarian;
            this.calories = calories;
            this.type = type;
        }
        public enum Type {
            FISH, OTHER, MEAT
        }

        public String getName() {
            return name;
        }

        public boolean isVegetarian() {
            return vegetarian;
        }

        public int getCalories() {
            return calories;
        }

        public Type getType() {
            return type;
        }
    }
    public static List<Dish> menu= Arrays.asList(
            new Dish ("pork", false, 800, Dish.Type.MEAT),
            new Dish (" beef ", false, 700, Dish.Type.MEAT),
            new Dish ("chicken " , false, 400, Dish.Type.MEAT),
            new Dish (" french fries ", true, 530, Dish.Type.OTHER),
            new Dish ("rice ", true, 350, Dish.Type.OTHER),
            new Dish ("season fruit ", true, 120, Dish.Type.OTHER),
            new Dish ("pizza ", true, 550 , Dish. Type. OTHER) ,
            new Dish ("pr aw ", false, 300, Dish.Type. FISH),
            new Dish ("salmon ", false, 450, Dish.Type.FISH) ) ;

    public static class Trader{
        private final String name;
        private final String city;
        public Trader(String n, String c) {
            this.name =n;
            this.city =c;
        }

        public String getName() {
            return name;
        }

        public String getCity() {
            return city;
        }

        @Override
        public String toString() {
            return "Trader{" +
                    "name='" + name + '\'' +
                    ", city='" + city + '\'' +
                    '}';
        }
    }
    public static class Transaction {
        private final Trader trader;
        private final int year;
        private final int value;
        private final Currency currency;
        public Transaction(Trader trader, int year, int value, Currency currency) {
            this.trader = trader;
            this.year = year;
            this.value = value;
            this.currency = currency;
        }

        public Currency getCurrency() {
            return currency;
        }

        public Trader getTrader() {
            return trader;
        }

        public int getYear() {
            return year;
        }

        public int getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "Transaction{" +
                    "trader=" + trader +
                    ", year=" + year +
                    ", value=" + value +
                    '}';
        }
    }
    public static  List<Transaction> transactions;
    static {
        Trader raoul = new Trader ("Raoul","Cambridge ");
        Trader mario =new Trader("Mari 。", " Milan ");
        Trader alan = new Trader( "Alan ", "Cambridge ");
        Trader brian = new Trader ("Brian ","Cambridge ");
        transactions = Arrays.asList(
            new Transaction(brian, 2011, 300, Currency.getInstance("USD")),
            new Transaction(raoul, 2012, 1000, Currency.getInstance("CNY")),
            new Transaction(raoul, 2011, 400, Currency.getInstance("USD")),
            new Transaction(mario, 2012, 710, Currency.getInstance("CNY")),
            new Transaction(mario, 2012, 700, Currency.getInstance("CNY")),
            new Transaction(alan, 2012, 950, Currency.getInstance("USD")));
    }

}
