package com.gloot.springbootcodetest.leaderboard;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.gloot.springbootcodetest.SpringBootComponentTest;
import com.gloot.springbootcodetest.leaderboards.LeaderboardsEntryEntity;
import com.gloot.springbootcodetest.leaderboards.LeaderboardsRepository;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class LeaderboardRepositoryTest extends SpringBootComponentTest {
  @Autowired LeaderboardRepository repository;
  @Autowired LeaderboardsRepository leaderboardsRepository;

  @Test
  void saveAndRetrieve() {
	LeaderboardsEntryEntity board = leaderboardsRepository.findByName("DEFAULT");
    LeaderboardEntryEntity entity = new LeaderboardEntryEntity("g-looter", 100, board);
    repository.saveAll(List.of(entity));
    LeaderboardEntryEntity fromRepository = repository.findById(entity.getUuid()).get();
    assertThat(fromRepository, is(entity));
  }
}
