package com.gloot.springbootcodetest.leaderboards;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaderboardsRepository extends JpaRepository<LeaderboardsEntryEntity, UUID> {
	LeaderboardsEntryEntity findByName(String name);
}
