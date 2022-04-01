package com.sillylab.lambda.master;

import com.sillylab.lambda.Common;

import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MasterMain extends Common {

    public static List<Book> library() {
        List<Book> books = new ArrayList<>();
        Book nails = new Book("Fundamentals of Chinese Fingernal Image",
                Arrays.asList("Li", "Fu", "Li"),
                new int[]{256},
                Year.of(2014),
                25.2,
                Topic.MEDICINE);
        books.add(nails);
        Book dragon = new Book("Compilers: Principles, Techniques and Tools",
                Arrays.asList("Aho", "Lam", "Sethi", "Ullman"),
                new int[]{1009},
                Year.of(2006),
                23.6,
                Topic.COMPUTING);
        books.add(dragon);
        Book voss = new Book("Voss",
                Arrays.asList("Patrick White"),
                new int[]{478},
                Year.of(1957),
                19.8,
                Topic.FICTION);
        books.add(voss);
        Book lotr = new Book("Lord of the Rings",
                Arrays.asList("Tolkien"),
                new int[]{531, 416, 624},
                Year.of(1955),
                23.0,
                Topic.FICTION);
        books.add(lotr);
        return books;
    }

    public static class Book {
        private String title ;
        private List<String> authors ;
        private int[] pageCounts;
        private Topic topic;
        private Year pubDate;
        private double height;

        public Book(String title, List<String> authors, int[] pageCounts, Year pubDate, double height, Topic topic ) {
            this.title = title;
            this.authors = authors;
            this.pageCounts = pageCounts;
            this.topic = topic;
            this.pubDate = pubDate;
            this.height = height;
        }

        public String getTitle() {
            return title;
        }

        public List<String> getAuthors() {
            return authors;
        }

        public int[] getPageCounts() {
            return pageCounts;
        }

        public Topic getTopic() {
            return topic;
        }

        public Year getPubDate() {
            return pubDate;
        }

        public double getHeight() {
            return height;
        }
    }
    public static enum Topic {
        MEDICINE, COMPUTING, FICTION, HISTORY, SCIENCE_FICTION, PROGRAMMING
    }
}
