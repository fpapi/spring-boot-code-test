package com.gloot.springbootcodetest.leaderboard;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.gloot.springbootcodetest.SpringBootComponentTest;
import com.gloot.springbootcodetest.leaderboards.LeaderboardsRepository;

import static com.gloot.springbootcodetest.leaderboard.DummyUtil.A_LEADERBOARD_NAME;
import static com.gloot.springbootcodetest.leaderboard.DummyUtil.A_NICK;
import static com.gloot.springbootcodetest.leaderboard.DummyUtil.A_SCORE;
import static com.gloot.springbootcodetest.leaderboard.DummyUtil.objectAsJson;

public class LeaderboardControllerTest extends SpringBootComponentTest {

  private static final String API_V1_LEADERBOARD_NAME_USER_NICK = "/api/v1/leaderboard/{name}/user/{nick}";
  
  @Autowired private MockMvc mockMvc;
  @Autowired LeaderboardRepository leaderboardRepo;
  @Autowired LeaderboardsRepository leaderboardsRepo;

  /* GET LEADERBOARD */
  
  @Test
  void givenADefaultLeaderboardWith1UserWhenGetTheDefaultLeaderboardThenReturnAnOk() throws Exception {
    // Given
	LeaderboardEntryEntity entity = initLeaderboardWith1Users("DEFAULT").get(0);
	// When
    var defaults = mockMvc.perform(get("/api/v1/leaderboard"));
    // Then    
    defaults.andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$.[0].position", is(1)))
        .andExpect(jsonPath("$.[0].nick", is(entity.getNick())))
        .andExpect(jsonPath("$.[0].score", is(entity.getScore())));
  }
  
  @Test
  void givenALeaderboardWith1UserWhenGetTheLeaderboardThenReturnAnOk() throws Exception {
	// Given
	LeaderboardEntryEntity entity = initLeaderboardWith1Users(A_LEADERBOARD_NAME).get(0);
	// When
    var leaderboards = mockMvc.perform(get("/api/v1/leaderboard/{name}", A_LEADERBOARD_NAME));
    // Then
    leaderboards
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$.[0].position", is(1)))
        .andExpect(jsonPath("$.[0].nick", is(entity.getNick())))
        .andExpect(jsonPath("$.[0].score", is(entity.getScore())));
  }
  
  @Test
  void givenNoLeaderboardWhenGetALeaderboardThenReturnA404() throws Exception {
	// Given
	  
	// When
    var user = mockMvc.perform(get("/api/v1/leaderboard/NO_LEADERBOARD"));
	// Then
    user
        .andExpect(status().is(404))
        .andExpect(jsonPath("$.reason", is("Missing leaderboard with the requested name")));
  }
  
  @Test
  void givenALeaderboardWith1UserWhenGetTheUserThenReturnAnOk() throws Exception {
	// Given
	LeaderboardEntryEntity entity = initLeaderboardWith1Users(A_LEADERBOARD_NAME).get(0);
	// When
    var user = mockMvc.perform(get(API_V1_LEADERBOARD_NAME_USER_NICK,entity.getLeaderboard().getName(),entity.getNick()));
	// Then
    user
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.position", is(1)))
        .andExpect(jsonPath("$.nick", is(entity.getNick())))
        .andExpect(jsonPath("$.score", is(entity.getScore())));
  }
  
  @Test
  void givenAnEmptyLeaderboardWhenGetAnUserThenReturnA404() throws Exception {
	// Given
	DummyUtil.initLeaderboardWithNUsers(leaderboardRepo, leaderboardsRepo, A_LEADERBOARD_NAME, 0);
	// When
    var user = mockMvc.perform(get(API_V1_LEADERBOARD_NAME_USER_NICK,A_LEADERBOARD_NAME, A_NICK));
	// Then
    user
        .andExpect(status().is(404))
        .andExpect(jsonPath("$.reason", is("Missing user with the requested nickname")));
  }
  
  /* PUT LEADERBOARD */
  
  @Test
  void givenNoLeaderboardWhenPutAnUserThenCreateBothBoardAndUserAndReturnCreated() throws Exception {
	// Given
	
	// When
    var response = mockMvc.perform(put(API_V1_LEADERBOARD_NAME_USER_NICK,A_LEADERBOARD_NAME, A_NICK)
    							.contentType(MediaType.APPLICATION_JSON)
    							.content(objectAsJson(new LeaderboardNewEntryDto(A_SCORE))));
	// Then
    response.andExpect(status().is(201));
    var user = mockMvc.perform(get(API_V1_LEADERBOARD_NAME_USER_NICK,A_LEADERBOARD_NAME, A_NICK));
    user
	    .andExpect(status().isOk())
	    .andExpect(jsonPath("$.position", is(1)))
	    .andExpect(jsonPath("$.nick", is(A_NICK)))
	    .andExpect(jsonPath("$.score", is(A_SCORE)));
  }
  
  @Test
  void givenAnEmptyLeaderboardWhenPutAnUserThenCreateUserScoreAndReturnCreated() throws Exception {
	// Given
	DummyUtil.initLeaderboardWithNUsers(leaderboardRepo, leaderboardsRepo, A_LEADERBOARD_NAME, 0);

	// When
    var response = mockMvc.perform(put(API_V1_LEADERBOARD_NAME_USER_NICK,A_LEADERBOARD_NAME, A_NICK)
    							.contentType(MediaType.APPLICATION_JSON)
    							.content(objectAsJson(new LeaderboardNewEntryDto(A_SCORE))));
	// Then
    response.andExpect(status().is(201));
    var user = mockMvc.perform(get(API_V1_LEADERBOARD_NAME_USER_NICK,A_LEADERBOARD_NAME, A_NICK));
    user
	    .andExpect(status().isOk())
	    .andExpect(jsonPath("$.position", is(1)))
	    .andExpect(jsonPath("$.nick", is(A_NICK)))
	    .andExpect(jsonPath("$.score", is(A_SCORE)));
  }
  
  @Test
  void givenALeaderboardWhenPutANewUserThenUpdateTheUserScoreAndReturnCreated() throws Exception {
	// Given
	DummyUtil.initLeaderboardWithNUsers(leaderboardRepo, leaderboardsRepo, A_LEADERBOARD_NAME, 0);
	mockMvc.perform(put(API_V1_LEADERBOARD_NAME_USER_NICK,A_LEADERBOARD_NAME, A_NICK)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectAsJson(new LeaderboardNewEntryDto(0))));
	// When
    var response = mockMvc.perform(put(API_V1_LEADERBOARD_NAME_USER_NICK,A_LEADERBOARD_NAME, A_NICK)
    							.contentType(MediaType.APPLICATION_JSON)
    							.content(objectAsJson(new LeaderboardNewEntryDto(A_SCORE))));
	// Then
    response.andExpect(status().is(201));
    var user = mockMvc.perform(get(API_V1_LEADERBOARD_NAME_USER_NICK,A_LEADERBOARD_NAME, A_NICK));
    user
	    .andExpect(status().isOk())
	    .andExpect(jsonPath("$.position", is(1)))
	    .andExpect(jsonPath("$.nick", is(A_NICK)))
	    .andExpect(jsonPath("$.score", is(A_SCORE)));
  }
  
  
  private List<LeaderboardEntryEntity> initLeaderboardWith1Users(String name) {
	return DummyUtil.initLeaderboardWithNUsers(leaderboardRepo, leaderboardsRepo, name, 1);
  }
	
}
