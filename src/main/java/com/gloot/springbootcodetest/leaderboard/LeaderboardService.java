package com.gloot.springbootcodetest.leaderboard;

import static com.gloot.springbootcodetest.leaderboard.LeaderboardEntryMapper.mapToDto;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LeaderboardService {
	private static final String DEFAULT_BOARD = "DEFAULT";

	private final LeaderboardRepository repository;

	public List<LeaderboardEntryDto> getListOfDefaultLeaderboardEntriesAsDTO() {
		var position = new AtomicInteger(1);
		return repository.findByLeaderboard_Name(DEFAULT_BOARD).stream()
			.sorted((e1,e2) -> e2.getScore() - e1.getScore())
			.map(e -> mapToDto(position.getAndIncrement(), e))
			.toList()
			;
	}
}
