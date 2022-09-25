package com.gloot.springbootcodetest.leaderboard;

import java.util.ArrayList;
import java.util.List;

import com.gloot.springbootcodetest.leaderboards.LeaderboardsEntryEntity;
import com.gloot.springbootcodetest.leaderboards.LeaderboardsRepository;

public final class DummyUtil {
	
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
