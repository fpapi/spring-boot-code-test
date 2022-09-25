package com.gloot.springbootcodetest.leaderboard;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import com.gloot.springbootcodetest.SpringBootComponentTest;
import com.gloot.springbootcodetest.leaderboards.LeaderboardsRepository;

public class LeaderboardControllerTest extends SpringBootComponentTest {

  @Autowired private MockMvc mockMvc;
  @Autowired LeaderboardRepository leaderboardRepo;
  @Autowired LeaderboardsRepository leaderboardsRepo;

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
	LeaderboardEntryEntity entity = initLeaderboardWith1Users("ALL_USER").get(0);
	// When
    var leaderboards = mockMvc.perform(get("/api/v1/leaderboard/ALL_USER"));
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
	LeaderboardEntryEntity entity = initLeaderboardWith1Users("SINGLE_USER").get(0);
	// When
    var user = mockMvc.perform(get("/api/v1/leaderboard/"+entity.getLeaderboard().getName()+"/user/"+entity.getNick()));
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
	DummyUtil.initLeaderboardWithNUsers(leaderboardRepo, leaderboardsRepo, "NO_USER", 0);
	// When
    var user = mockMvc.perform(get("/api/v1/leaderboard/NO_USER/user/a-user"));
	// Then
    user
        .andExpect(status().is(404))
        .andExpect(jsonPath("$.reason", is("Missing user with the requested nickname")));
  }
  
	private List<LeaderboardEntryEntity> initLeaderboardWith1Users(String name) {
		return DummyUtil.initLeaderboardWithNUsers(leaderboardRepo, leaderboardsRepo, name, 1);
	}
	
}
