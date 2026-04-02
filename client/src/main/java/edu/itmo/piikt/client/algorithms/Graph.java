package edu.itmo.piikt.client.algorithms;

import lombok.Getter;

import java.util.*;
@Getter
public class Graph {
    private final Map<String, List<String>> graph = new HashMap<>();
    private final List<String> list = new ArrayList<>();

    public void addScript(String script, String scriptTwo) {
        if (!script.equals(scriptTwo)) {
            graph.computeIfAbsent(script, two -> new ArrayList<>()).add(scriptTwo);
        }
    }

    public void endScript(String script) {
        if(!list.isEmpty()) list.removeLast();
    }

    public void start(String script) {
        list.add(script);
    }

    public boolean hasPath(String startScript, Map<String, List<String>> graph,
                           Set<String> visited) {
        if (list.contains(startScript)) {
            return true;
        }

        if (visited.contains(startScript)) {
            return false;
        }

        visited.add(startScript);

        if (graph.containsKey(startScript)) {
            for (String next : graph.get(startScript)) {
                if (hasPath(next, graph, visited)) return true;
            }
        }
        return false;
    }

    public boolean copy(String script) {
        Map<String, List<String>> listMap = new HashMap<>(graph);
        for (int i = 0; i < list.size() - 1; i++) {
            String from = list.get(i);
            String to = list.get(i + 1);
            listMap.computeIfAbsent(from, a -> new ArrayList<>()).add(to);
        }
        if (!list.isEmpty()) {String lastScript = list.getLast();
            listMap.computeIfAbsent(lastScript, a -> new ArrayList<>()).add(script);}
            Set<String> visited = new HashSet<>();
            return hasPath(script, listMap, visited);
    }

    private boolean cycle(String script, Map<String, Integer> color) {
        color.put(script, 1);
        if (graph.containsKey(script)) {
            for (String string: graph.get(script)) {
                int stringColor = color.getOrDefault(string, 0);
                if (stringColor == 1) return true;

                if (stringColor == 0) {
                    if (cycle(string, color)) return true;
                }
            }
        }
        color.put(script, 2);
        return false;
    }

    public boolean hasCycle() {
        Map<String, Integer> color = new HashMap<>();
        for (String script : graph.keySet()) {
            if (color.getOrDefault(script, 0) == 0) {
                if (cycle(script, color)) return  true;
            }
        } return false;
    }
}
