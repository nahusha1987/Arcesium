package com.arcesium.interview;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Solution {

	private static final String REJECT = "REJECT";
	private static final String SELECT = "SELECT";
	private static final String STRIKER = "STRIKER";
	private static final String DEFENDER = "DEFENDER";
	private static final String NA = "NA";
	
	static class PlayerStatus {
		String name;
		String selectionStatus;
		String position;
	}

	public static void main(String[] args) throws IOException {
		FileReader fileReader = new FileReader("input.txt");
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String[] sizes = bufferedReader.readLine().split(" ");
		int i = Integer.parseInt(sizes[0]);
		List<List<String>> applications = new ArrayList<>();
		for (int x = 0; x < i; x++) {
			List<String> tmp = Arrays.asList(bufferedReader.readLine().split(" "));
			applications.add(tmp);
		}
		applications = Solution.selectTeam(applications);
		for (List<String> players : applications) {
			System.out.println(players.get(0) + " " + players.get(1) + " " + players.get(2));
		}
		fileReader.close();
		bufferedReader.close();
	}

	public static List<List<String>> selectTeam(List<List<String>> players) {
		Map<String, Integer> strikerMap = new HashMap<>();
		Map<String, Integer> defenderMap = new LinkedHashMap<>();
		List<PlayerStatus> status = new ArrayList<>();
		for (List<String> player : players) {
			PlayerStatus ps = new PlayerStatus();
			ps.name = player.get(0);
			ps.selectionStatus = REJECT;
			ps.position = NA;
			if (fitCheckPassed(player)) {
				determinePosition(player, ps, strikerMap, defenderMap);
			} 
			status.add(ps);
		}

		// This is to calculate the number of players to be selected in each category 
		int selectionCount = Math.min(strikerMap.size(), defenderMap.size());

		strikerMap = sortByValue(strikerMap, true);
		defenderMap = sortByValue(defenderMap, false);

		List<String> strikerList = new ArrayList<>();
		List<String> defenderList = new ArrayList<>();
		Iterator<String> it1 = strikerMap.keySet().iterator();
		Iterator<String> it2 = defenderMap.keySet().iterator();

		// This is to get the list of players who are to be selected based on selectionCount
		for (int i = 0; i < selectionCount; i++) {
			if(it1.hasNext())
				strikerList.add(it1.next());
			if(it2.hasNext())
				defenderList.add(it2.next());
		}

		// Update the striker's status and position
		for (String str : strikerList) {
			for(PlayerStatus ps : status) {
				if (str.equalsIgnoreCase(ps.name)) {
					ps.selectionStatus = SELECT;
					ps.position = STRIKER;
				}
			}
		}

		// Update the defender's status and position
		for(String str : defenderList) {
			for (PlayerStatus ps : status) {
				if (str.equals(ps.name)) {
					ps.selectionStatus = SELECT;
					ps.position = DEFENDER;
				}
			}
		}

		// Prepare the list of players to be returned
		List<List<String>> returnList = new ArrayList<>();
		for (PlayerStatus ps : status) {
			List<String> list = new ArrayList<>();
			list.add(ps.name);
			list.add(ps.selectionStatus);
			list.add(ps.position);
			returnList.add(list);
		}
		return returnList;
	}

	// check and populate if the player belongs to a particular category based on his statistics
	private static void determinePosition(List<String> player, PlayerStatus ps, Map<String, Integer> strikerMap, Map<String, Integer> defenderMap) {
		int goalsScored = Integer.parseInt(player.get(3));
		int goalsDefended = Integer.parseInt(player.get(4));
		if (goalsScored >= 50) {
			strikerMap.put(ps.name, goalsScored);
		}
		else if (goalsDefended <= 30) {
			defenderMap.put(ps.name, goalsDefended);
		}
		else {
			ps.selectionStatus = REJECT;
			ps.position = NA;
		}
	}

	// Check if the player passes the height and bmi criteria
	private static boolean fitCheckPassed(List<String> player) {
		float height = Float.parseFloat(player.get(1));
		float bmi = Float.parseFloat(player.get(2));
		if (height < 5.8)
			return false;
		if (bmi > 23)
			return false;
		return true;
	}
	
	// Sort the maps based on value
	private static Map<String, Integer> sortByValue(Map<String, Integer> map, boolean descending) {
		Map<String, Integer> sortedMap = null;
		if (descending)
			     sortedMap = map.entrySet().stream()
			    .sorted(Entry.<String, Integer>comparingByValue().reversed())
			    .collect(Collectors.toMap(Entry::getKey, Entry::getValue,
			                              (e1, e2) -> e1, LinkedHashMap::new));
		else
			sortedMap = map.entrySet().stream()
		    .sorted(Entry.comparingByValue())
		    .collect(Collectors.toMap(Entry::getKey, Entry::getValue,
		                              (e1, e2) -> e1, LinkedHashMap::new));
		
		return sortedMap;
	}
}
