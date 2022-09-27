package com.gloot.springbootcodetest.leaderboard;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gloot.springbootcodetest.leaderboards.LeaderboardsEntryEntity;
import com.gloot.springbootcodetest.leaderboards.LeaderboardsRepository;

public final class DummyUtil {
	
	public static final String A_LEADERBOARD_NAME = "WORLD";
	public static final String A_NICK = "BOB";
	public static final int A_SCORE = 1000;
	
	public static <T> String objectAsJson(T input) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(input);
	}
	
	public static List<LeaderboardEntryEntity> initLeaderboardWithNUsers(LeaderboardRepository leaderboardRepo, LeaderboardsRepository leaderboardsRepo, String name, int size) {
		LeaderboardsEntryEntity board = leaderboardsRepo.findByName(name)
				.orElseGet(() -> leaderboardsRepo.save(LeaderboardsEntryEntity.builder().name(name).build()));
		List<LeaderboardEntryEntity> entities = new ArrayList<>(size); 
		for(int i=0; i < size; i++) {
			entities.add(LeaderboardEntryEntity.builder()
					.leaderboard(board)
					.nick(String.format("g-looter-%s", i+1))
					.score((i+1)*100)
					.build());
		}
		leaderboardRepo.saveAll(entities);
		return entities;
	}

}
