package com.gloot.springbootcodetest.leaderboard;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.gloot.springbootcodetest.leaderboards.LeaderboardsEntryEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "leaderboard")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class LeaderboardEntryEntity implements WithNick {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID uuid;
	private String nick;
	private Integer score;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "board_id")
	private LeaderboardsEntryEntity leaderboard;

	public LeaderboardEntryEntity(String nick, Integer score, LeaderboardsEntryEntity leaderboard) {
		this.nick = nick;
		this.score = score;
		this.leaderboard = leaderboard;
	}

}
