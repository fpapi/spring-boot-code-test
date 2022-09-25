package com.gloot.springbootcodetest.leaderboard;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaderboardRepository extends JpaRepository<LeaderboardEntryEntity, UUID> {
	
	List<LeaderboardEntryEntity> findByLeaderboard_Name(String boardName);
	
}
