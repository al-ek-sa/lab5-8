package edu.itmo.piikt.client.algorithms;

import edu.itmo.piikt.common.logger.AppLogger;
import java.util.*;
import lombok.Getter;

@Getter
public class Graph {
	private final Map<String, List<String>> graph = new HashMap<>();
	private final List<String> list = new ArrayList<>();
	private static final AppLogger log = new AppLogger(Graph.class);

	public void addScript(String script, String scriptTwo) {
		if (!script.equals(scriptTwo)) {
			graph.computeIfAbsent(script, two -> new ArrayList<>()).add(scriptTwo);
			log.debug("Added script dependency: {} -> {}", script, scriptTwo);
		}
	}

	public void endScript(String script) {
		if (!list.isEmpty() && list.getLast().equals(script)) {
			list.removeLast();
			log.debug("Ended script: {}, current stack: {}", script, list);
		}
	}

	public void start(String script) {
		list.add(script);
		log.debug("Started script: {}, current stack: {}", script, list);
	}

	public boolean hasPath(String startScript, Map<String, List<String>> graph, List<String> currentPath,
			Set<String> visited) {
		log.debug("hasPath: checking '{}' in path {}", startScript, currentPath);
		if (currentPath.contains(startScript)) {
			log.warn("hasPath: CYCLE detected! '{}' already in path {}", startScript, currentPath);
			return true;
		}

		if (visited.contains(startScript)) {
			log.debug("hasPath: '{}' already visited", startScript);
			return false;
		}

		visited.add(startScript);

		if (graph.containsKey(startScript)) {
			for (String next : graph.get(startScript)) {
				log.debug("hasPath: recursing from '{}' to '{}'", startScript, next);
				if (hasPath(next, graph, currentPath, visited)) {
					return true;
				}
			}
		}
		log.debug("hasPath: no path found from '{}'", startScript);
		return false;
	}

	public boolean copy(String script) {
		log.info("Copy: checking if adding '{}' creates a cycle", script);
		log.debug("Copy: current graph = {}", graph);
		log.debug("Copy: current stack = {}", list);
		Map<String, List<String>> listMap = new HashMap<>(graph);
		List<String> currentPath = new ArrayList<>(list);

		for (int i = 0; i < list.size() - 1; i++) {
			String from = list.get(i);
			String to = list.get(i + 1);
			listMap.computeIfAbsent(from, a -> new ArrayList<>()).add(to);
			log.debug("Copy: added edge from stack: {} -> {}", from, to);
		}

		if (!list.isEmpty()) {
			String lastScript = list.getLast();
			listMap.computeIfAbsent(lastScript, a -> new ArrayList<>()).add(script);
			log.debug("Copy: added new edge: {} -> {}", lastScript, script);
		}

		Set<String> visited = new HashSet<>();
		boolean result = hasPath(script, listMap, currentPath, visited);
		if (result) {
			log.warn("Copy: CYCLE DETECTED! Script '{}' would create a cycle", script);
		} else {
			log.info("Copy: safe to add script '{}', no cycle detected", script);
		}
		return result;
	}
}
