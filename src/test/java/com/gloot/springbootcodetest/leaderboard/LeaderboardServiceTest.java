package com.gloot.springbootcodetest.leaderboard;

import static com.gloot.springbootcodetest.leaderboard.DummyUtil.A_LEADERBOARD_NAME;
import static com.gloot.springbootcodetest.leaderboard.DummyUtil.A_NICK;
import static com.gloot.springbootcodetest.leaderboard.DummyUtil.A_SCORE;
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
	
	/* GET LEADERBOARD */
	
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
		List<LeaderboardEntryEntity> entities = initLeaderboardWith2Users(A_LEADERBOARD_NAME);
		// When
		List<LeaderboardEntryDto> leaderboard = service.getListOfLeaderboardEntriesAsDTO(A_LEADERBOARD_NAME);
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
		Exception notFound = assertThrows(EntityNotFoundException.class, () -> service.getListOfLeaderboardEntriesAsDTO(A_LEADERBOARD_NAME));
		// Then
		assertEquals("Missing leaderboard with the requested name", notFound.getMessage());
	}

	@Test
	void givenALeaderboardWith2UserWhenRetrieveAnExistingUserScoreThenReturnTheLeaderboardEntry() {
		// Given
		List<LeaderboardEntryEntity> entities = initLeaderboardWith2Users(A_LEADERBOARD_NAME);
		// When
		LeaderboardEntryDto user = service.getLeaderboardEntryAsDTO(A_LEADERBOARD_NAME,"g-looter-1");
		// Then
		assertEqual(2, entities.get(0), user);
	}
	
	@Test
	void givenALeaderboardWith2UserWhenRetrieveAnUserScoreThenThrowAnEntityNotFound() {
		// Given
		initLeaderboardWith2Users(A_LEADERBOARD_NAME);
		// When
		Exception notFound = assertThrows(EntityNotFoundException.class, () -> service.getLeaderboardEntryAsDTO(A_LEADERBOARD_NAME, A_NICK));
		// Then
		assertEquals("Missing user with the requested nickname", notFound.getMessage());
	}
	
	@Test
	void givenNoLeaderboardWhenRetrieveAnUserThenThrowAnEntityNotFound() {
		// Given
		
		// When
		Exception notFound = assertThrows(EntityNotFoundException.class, () -> service.getLeaderboardEntryAsDTO(A_LEADERBOARD_NAME, A_NICK));
		// Then
		assertEquals("Missing leaderboard with the requested name", notFound.getMessage());
	}
	
	/* ADD USER SCORE */
	
	@Test
	void givenNoLeaderboardWhenAddANewUserThenCreateTheLeaderboardAndTheUser() {
		// Given
		
		// When
		service.addLeaderboardEntry(A_LEADERBOARD_NAME, A_NICK, new LeaderboardNewEntryDto(A_SCORE) );
		// Then
		LeaderboardEntryDto user = service.getLeaderboardEntryAsDTO(A_LEADERBOARD_NAME, A_NICK);
		assertEquals(A_NICK, user.getNick());
		assertEquals(A_SCORE, user.getScore());
	}

	@Test
	void givenALeaderboardWhenAddANewUserThenCreateTheTheUser() {
		// Given
		initLeaderboardWith2Users(A_LEADERBOARD_NAME);
		// When
		service.addLeaderboardEntry(A_LEADERBOARD_NAME, A_NICK, new LeaderboardNewEntryDto(A_SCORE) );
		// Then
		LeaderboardEntryDto user = service.getLeaderboardEntryAsDTO(A_LEADERBOARD_NAME, A_NICK);
		assertEquals(A_NICK, user.getNick());
		assertEquals(A_SCORE, user.getScore());
	}
	
	@Test
	void givenALeaderboardWhenAddAnExistingUserThenUpdateTheTheUser() {
		String lastUser = "last-user";
		// Given
		initLeaderboardWith2Users(A_LEADERBOARD_NAME);
		service.addLeaderboardEntry(A_LEADERBOARD_NAME, lastUser, new LeaderboardNewEntryDto(0) );
		// When
		service.addLeaderboardEntry(A_LEADERBOARD_NAME, lastUser, new LeaderboardNewEntryDto(1000) );
		// Then
		LeaderboardEntryDto user = service.getLeaderboardEntryAsDTO(A_LEADERBOARD_NAME, lastUser);
		assertEquals(lastUser, user.getNick());
		assertEquals(A_SCORE, user.getScore());
		assertEquals(1, user.getPosition());
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
