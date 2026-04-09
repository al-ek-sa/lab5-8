package edu.itmo.piikt.client.algorithms;

import edu.itmo.piikt.common.logger.AppLogger;
import java.util.*;
import lombok.Getter;

/**
 * The class builds a directed graph of script calls and detects
 * whether adding a new edge would create a cycle. This prevents infinite
 * recursion when executing scripts.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@Getter
public class Graph {
	/** Graph of script dependencies. Key - script name, value - list of scripts it calls*/
	private final Map<String, List<String>> graph = new HashMap<>();
	/**Current execution stack (path of scripts being executed)*/
	private final List<String> list = new ArrayList<>();
	private static final AppLogger log = new AppLogger(Graph.class);

	/**
	 * Adds a directed edge from script to scriptTwo
	 * @param script caller script
	 * @param scriptTwo called script
	 */
	public void addScript(String script, String scriptTwo) {
		if (!script.equals(scriptTwo)) {
			graph.computeIfAbsent(script, two -> new ArrayList<>()).add(scriptTwo);
			log.debug("Added script dependency: {} -> {}", script, scriptTwo);
		}
	}

	/**
	 * Removes script from execution stack when it finishes
	 * @param script script name
	 */
	public void endScript(String script) {
		if (!list.isEmpty() && list.getLast().equals(script)) {
			list.removeLast();
			log.debug("Ended script: {}, current stack: {}", script, list);
		}
	}

	/**
	 *  Adds script to execution stack when it starts
	 * @param script script name
	 */
	public void start(String script) {
		list.add(script);
		log.debug("Started script: {}, current stack: {}", script, list);
	}

	/**
	 * Algorithm to check if there is a path from startScript to any script in currentPath
	 * @param startScript starting vertex
	 * @param graph graph to search in
	 * @param currentPath current execution path
	 * @param visited visited vertices set
	 * @return true if path exists (cycle), false otherwise
	 */
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

	/**
	 * This method temporarily adds an edge from the last script in the
	 * execution stack to the new script, then checks if a path exists from the new
	 * script back to any script in the current stack. If such a path exists, the
	 * call would create a cycle
	 * @param script script to check
	 * @return true if cycle would be created, false if safe
	 */
	public boolean copy(String script) {
		log.info("Copy: checking if adding '{}' creates a cycle", script);
		log.debug("Copy: current graph = {}", graph);
		log.debug("Copy: current stack = {}", list);
		Map<String, List<String>> listMap = new HashMap<>(graph);
		List<String> currentPath = new ArrayList<>(list);
		//Add edges from execution stack
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
