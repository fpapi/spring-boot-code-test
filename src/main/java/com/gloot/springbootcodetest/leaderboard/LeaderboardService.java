package com.gloot.springbootcodetest.leaderboard;

import static com.gloot.springbootcodetest.leaderboard.LeaderboardEntryMapper.mapToDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LeaderboardService {
	private static final String DEFAULT_BOARD = "DEFAULT";

	private final LeaderboardRepository repository;

	public List<LeaderboardEntryDto> getListOfAllLeaderboardEntriesAsDTO() {
		List<LeaderboardEntryEntity> allEntries = repository.findAll().stream().filter(e -> DEFAULT_BOARD.equals(e.getLeaderboard().getName())).collect(Collectors.toList());
		Collections.sort(allEntries, new Comparator<LeaderboardEntryEntity>() {
			public int compare(LeaderboardEntryEntity e1, LeaderboardEntryEntity e2) {
				return e2.getScore() - e1.getScore();
			}
		});

		LeaderboardEntryEntity[] allEntriesAsEntities = allEntries.toArray(new LeaderboardEntryEntity[] {});
		LeaderboardEntryDto[] dtoObjects = new LeaderboardEntryDto[allEntriesAsEntities.length];

		for (int i = allEntriesAsEntities.length - 1; i >= 0; i--) {
			dtoObjects[i] = mapToDto(i + 1, allEntriesAsEntities[i]);
		}

		List<LeaderboardEntryDto> leaderboardEntryDtos = new ArrayList<>();
		for (int j = dtoObjects.length - 1; j >= 0; j--) {
			leaderboardEntryDtos.add(dtoObjects[j]);
		}

		Collections.sort(leaderboardEntryDtos, new Comparator<LeaderboardEntryDto>() {
			public int compare(LeaderboardEntryDto e1, LeaderboardEntryDto e2) {
				return e1.getPosition() - e2.getPosition();
			}
		});

		return leaderboardEntryDtos;
	}
}
