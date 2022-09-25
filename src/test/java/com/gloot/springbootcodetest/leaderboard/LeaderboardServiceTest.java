package com.gloot.springbootcodetest.leaderboard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gloot.springbootcodetest.SpringBootComponentTest;
import com.gloot.springbootcodetest.errors.EntityNotFoundException;
import com.gloot.springbootcodetest.leaderboards.LeaderboardsRepository;

public class LeaderboardServiceTest extends SpringBootComponentTest {

	@Autowired LeaderboardRepository leaderboardRepo;
	@Autowired LeaderboardService service;
	@Autowired LeaderboardsRepository leaderboardsRepo;

	@Test
	void givenADefaultLeaderboardWith2UserWhenRetrieveTheDefaultLeadeboardThenReturnTheLeaderboardEntries() {
		// Given
		List<LeaderboardEntryEntity> entities = initLeaderboardWith2Users("DEFAULT");
		// When
		List<LeaderboardEntryDto> leaderboard = service.getListOfDefaultLeaderboardEntriesAsDTO();
		// Then
		assertEquals(entities.size(), leaderboard.size());
		// Verify ordering by score
		assertEqual(1, entities.get(1), leaderboard.get(0));
		assertEqual(2, entities.get(0), leaderboard.get(1));
	}

	@Test
	void givenALeaderboardWith2UserWhenRetrieveTheLeadeboardThenReturnTheLeaderboardEntries() {
		// Given
		List<LeaderboardEntryEntity> entities = initLeaderboardWith2Users("ALL_USERS");
		// When
		List<LeaderboardEntryDto> leaderboard = service.getListOfLeaderboardEntriesAsDTO("ALL_USERS");
		// Then
		assertEquals(entities.size(), leaderboard.size());
		// Verify ordering by score
		assertEqual(1, entities.get(1), leaderboard.get(0));
		assertEqual(2, entities.get(0), leaderboard.get(1));
	}
	
	@Test
	void givenNoLeaderboardWhenRetrieveALeadeboardThenThrowAnEntityNotFound() {
		// Given
		
		// When
		Exception notFound = assertThrows(EntityNotFoundException.class, () -> service.getListOfLeaderboardEntriesAsDTO("MISSING_LEADERBOARD"));
		// Then
		assertEquals("Missing leaderboard with the requested name", notFound.getMessage());
	}

	@Test
	void givenALeaderboardWith2UserWhenRetrieveAnExistingUserScoreThenReturnTheLeaderboardEntry() {
		// Given
		List<LeaderboardEntryEntity> entities = initLeaderboardWith2Users("SINGLE_USERS");
		// When
		LeaderboardEntryDto user = service.getLeaderboardEntryAsDTO("SINGLE_USERS","g-looter-2");
		// Then
		assertEqual(2, entities.get(0), user);
	}
	
	@Test
	void givenALeaderboardWith2UserWhenRetrieveAnUserScoreThenThrowAnEntityNotFound() {
		// Given
		initLeaderboardWith2Users("MISSING_USERS");
		// When
		Exception notFound = assertThrows(EntityNotFoundException.class, () -> service.getLeaderboardEntryAsDTO("MISSING_USERS","a-user"));
		// Then
		assertEquals("Missing user with the requested nickname", notFound.getMessage());
	}
	
	@Test
	void givenNoLeaderboardWhenRetrieveAnUserThenThrowAnEntityNotFound() {
		// Given
		
		// When
		Exception notFound = assertThrows(EntityNotFoundException.class, () -> service.getLeaderboardEntryAsDTO("MISSING_LEADERBOARD","a-user"));
		// Then
		assertEquals("Missing leaderboard with the requested name", notFound.getMessage());
	}

	private List<LeaderboardEntryEntity> initLeaderboardWith2Users(String name) {
		return DummyUtil.initLeaderboardWithNUsers(leaderboardRepo, leaderboardsRepo, name, 2);
	}

	private void assertEqual(int pos, LeaderboardEntryEntity entity, LeaderboardEntryDto dto) {
		assertEquals(pos, dto.getPosition());
		assertEquals(entity.getNick(), dto.getNick());
		assertEquals(entity.getScore(), dto.getScore());
	}
}
