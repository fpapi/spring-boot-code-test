package com.gloot.springbootcodetest.leaderboard;


import static com.gloot.springbootcodetest.Application.API_VERSION_1;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(API_VERSION_1 + "/leaderboard")
@AllArgsConstructor
public class LeaderboardController {
  private final LeaderboardService service;

  @GetMapping
  public List<LeaderboardEntryDto> getLeaderboard() {
    return service.getListOfDefaultLeaderboardEntriesAsDTO();
  }
  
  @GetMapping(path = "/{name}")
  public List<LeaderboardEntryDto> getLeaderboard(@PathVariable String name) {
    return service.getListOfLeaderboardEntriesAsDTO(name);
  }
  
  @GetMapping(path = "/{name}/user/{nick}")
  public LeaderboardEntryDto getLeaderboardForUser(@PathVariable String name, @PathVariable String nick) {
    return service.getLeaderboardEntryAsDTO(name, nick);
  }
  
}
