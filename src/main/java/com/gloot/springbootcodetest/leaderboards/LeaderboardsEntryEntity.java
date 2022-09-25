package com.gloot.springbootcodetest.leaderboards;

import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.gloot.springbootcodetest.leaderboard.LeaderboardEntryEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "leaderboards")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class LeaderboardsEntryEntity {
	  @Id
	  @GeneratedValue(strategy = GenerationType.AUTO)
	  private UUID uuid;
	  private String name;
	  @OneToMany(mappedBy = "leaderboard", fetch = FetchType.LAZY)
	  @EqualsAndHashCode.Exclude private List<LeaderboardEntryEntity> ranks;
	
	  public LeaderboardsEntryEntity(String name) {
		this.name = name;
	}
  
}
