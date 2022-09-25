package com.gloot.springbootcodetest.leaderboard;

import static com.gloot.springbootcodetest.leaderboard.LeaderboardEntryMapper.mapToDto;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.gloot.springbootcodetest.errors.EntityNotFoundException;
import com.gloot.springbootcodetest.leaderboards.LeaderboardsRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LeaderboardService {
	private static final String DEFAULT_BOARD = "DEFAULT";
	private static final Comparator<LeaderboardEntryEntity> HIGHER_SCORE_FIRST = (e1, e2) -> e2.getScore()- e1.getScore();

	private final LeaderboardRepository repository;
	private final LeaderboardsRepository leaderboardsRepo;

	public List<LeaderboardEntryDto> getListOfDefaultLeaderboardEntriesAsDTO() {
		return getListOfLeaderboardEntriesAsDTO(DEFAULT_BOARD);
	}

	public List<LeaderboardEntryDto> getListOfLeaderboardEntriesAsDTO(String name) {
		var position = new AtomicInteger(1);
		var leaderboardAsDtos = repository.findByLeaderboard_Name(name)
				.stream()
				.sorted(HIGHER_SCORE_FIRST)
				.map(e -> mapToDto(position.getAndIncrement(), e))
				.toList();
		if (leaderboardAsDtos.isEmpty()) { 
			leaderboardsRepo.findByName(name).orElseThrow(() -> new EntityNotFoundException("Missing leaderboard with the requested name"));
		}
		return leaderboardAsDtos;
	}

	public LeaderboardEntryDto getLeaderboardEntryAsDTO(String name, String nick) {
		System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAA: "+nick);
		return getListOfLeaderboardEntriesAsDTO(name).stream()
				.map(e -> {System.out.println("BBBBBB:"+e.getNick()+" "+StringUtils.equals(e.getNick(), nick)); return e;})
				.filter(e -> StringUtils.equals(e.getNick(), nick))
				.findAny()
				.orElseThrow(() -> new EntityNotFoundException("Missing user with the requested nickname"));
	}

}
