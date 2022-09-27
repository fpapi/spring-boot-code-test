package com.gloot.springbootcodetest.leaderboard;


import static com.gloot.springbootcodetest.Application.API_VERSION_1;

import java.util.List;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping(API_VERSION_1 + "/leaderboard")
@AllArgsConstructor
public class LeaderboardController {
  private final LeaderboardService service;

  @GetMapping
  @Operation(summary = "Get all the entries for the default leaderboard")
  public List<LeaderboardEntryDto> getLeaderboard() {
    return service.getListOfDefaultLeaderboardEntriesAsDTO();
  }
  
  @GetMapping(path = "/{name}")
  @Operation(summary = "Get all the entries for the leaderboard with the input name")
  public List<LeaderboardEntryDto> getLeaderboard(@PathVariable String name) {
    return service.getListOfLeaderboardEntriesAsDTO(name);
  }
  
  @GetMapping(path = "/{name}/user/{nick}")
  @Operation(summary = "Get the entry with the input nickname in the leaderboard with the input name")
  public LeaderboardEntryDto getLeaderboardForUser(@PathVariable String name, @PathVariable String nick) {
    return service.getLeaderboardEntryAsDTO(name, nick);
  }
  
  @Operation(summary = "Create or update the entry with the input nick in the leaderboard with the input name")
  @PutMapping(path = "/{name}/user/{nick}")
  @ResponseStatus(HttpStatus.CREATED)
  public void putLeaderboardForUser(@PathVariable String name, @PathVariable String nick, @RequestBody LeaderboardNewEntryDto entry) {
    service.addLeaderboardEntry(name, nick, entry);
  }
}
