package edu.itmo.piikt.saveManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Reader {
    private String name;
    public Reader() {
        this.name = System.getenv("WORKER_FILE");
        if (this.name == null || this.name.isEmpty()) {
            this.name = "workers.csv";
        }
    }

    public void reader() {
        try {
            try (FileReader fileReader = new FileReader("workers.csv")) {
                var bufferrider = new BufferedReader(fileReader);
                System.out.println(bufferrider.readLine());
                /**
                 * while(input.hasNextLine()){ String line = input.nextLine(); String[] part =
                 * line.split(";"); List<String> part2 = new ArrayList<>();
                 * Arrays.stream(part).forEach(part1 -> part1.substring(1, part1.length()-1)); }
                 */
                /**
                 * var line = input.readLine(); String[] part = line.split(";"); List<String>
                 * queue = new ArrayList<>();
                 */
                /** List<T> list = new ArrayList<>(); */
                /**
                 * for (String part1 : part) { if (part1.length() > 2) {
                 */
                /** part1.substring(1, part1.length() - 1); */
                /**
                 * queue.add(part1); } } queue.stream().map(element -> element + "
                 * ").forEach(System.out::print);
                 */
                /**
                 * Worker worker = new Worker(list.get(1), list.get(2), list.get(3),
                 * list.get(4), list.get(5), list.get(6),list)
                 */
                /**
                 * String[] part = line.split(";"); List<String> part2 = new ArrayList<>();
                 * Arrays.stream(part).forEach(part1 -> part1.substring(1, part1.length-1));
                 */
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
