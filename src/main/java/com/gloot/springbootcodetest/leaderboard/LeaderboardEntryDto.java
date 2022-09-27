package com.gloot.springbootcodetest.leaderboard;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class LeaderboardEntryDto implements WithNick {
  int position;
  String nick;
  int score;
}
