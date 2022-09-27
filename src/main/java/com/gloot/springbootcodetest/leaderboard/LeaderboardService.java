package com.gloot.springbootcodetest.leaderboard;

import static com.gloot.springbootcodetest.leaderboard.LeaderboardEntryMapper.mapToDto;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gloot.springbootcodetest.errors.EntityNotFoundException;
import com.gloot.springbootcodetest.leaderboards.LeaderboardsEntryEntity;
import com.gloot.springbootcodetest.leaderboards.LeaderboardsRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LeaderboardService {
	private static final String DEFAULT_BOARD = "DEFAULT";
	private static final Comparator<LeaderboardEntryEntity> HIGHER_SCORE_FIRST = (e1, e2) -> e2.getScore()- e1.getScore();
	
	private final LeaderboardRepository repository;
	private final LeaderboardsRepository leaderboardsRepo;

	/**Retrieve all the entries of the default leaderboard (having the name {@value #DEFAULT_BOARD})
	 * @return a list of entries
	 */
	public List<LeaderboardEntryDto> getListOfDefaultLeaderboardEntriesAsDTO() {
		return getListOfLeaderboardEntriesAsDTO(DEFAULT_BOARD);
	}

	/**Retrieve all the entries of the leaderboard having the input name. Will throw an {@link EntityNotFoundException} if the leaderboard do not exist.
	 * @param name the leaderboard name
	 * @return a list of entries
	 */
	public List<LeaderboardEntryDto> getListOfLeaderboardEntriesAsDTO(String name) {
		var position = new AtomicInteger(1);
		var leaderboardAsDtos = repository.findByLeaderboard_Name(name)
				.stream()
				.sorted(HIGHER_SCORE_FIRST)
				.map(e -> mapToDto(position.getAndIncrement(), e))
				.toList();
		if (leaderboardAsDtos.isEmpty()) { 
			leaderboardsRepo.findByName(name).orElseThrow(() -> new EntityNotFoundException("Missing leaderboard with the requested name"));
		}
		return leaderboardAsDtos;
	}

	/**Retrieve the leaderboard entry having the input nick in the input leaderboard name. Will throw an {@link EntityNotFoundException} if the leaderboard do not exist or do not contains an entry for the input nickname.
	 * @param name a leaderboard name
	 * @param nick a nickname
	 * @return an entry
	 */
	public LeaderboardEntryDto getLeaderboardEntryAsDTO(String name, String nick) {
		return getListOfLeaderboardEntriesAsDTO(name).stream()
				.filter(getHaveTheSameNickPredicate(nick))
				.findAny()
				.orElseThrow(() -> new EntityNotFoundException("Missing user with the requested nickname"));
	}
	
	/**Add or Update in the leaderboard (having the input name) the entry having the input nickname with the input data. If the leaderboard do not exists, it will be created.
	 * @param name a leaderboard name
	 * @param nick a nickname
	 * @param entryToAdd the data to add/update
	 */
	@Transactional
	public void addLeaderboardEntry(String name, String nick, LeaderboardNewEntryDto entryToAdd) {
		leaderboardsRepo.findByName(name)
			.or(() -> Optional.of(leaderboardsRepo.save(new LeaderboardsEntryEntity(name))))
			.flatMap(board -> 
				repository.findByLeaderboard_Name(name).stream()
					.filter(getHaveTheSameNickPredicate(nick))
					.findAny()
					.map(user -> user.toBuilder().score(entryToAdd.getScore()).build())
				.or(() -> Optional.of(new LeaderboardEntryEntity(nick, entryToAdd.getScore(), board)))
				.map(repository::save)
			)
			.orElseThrow(() -> new IllegalStateException("Something go wrong! We could not put together the failure cause, please contact our backoffice."))
			;
	}

	private static Predicate<WithNick> getHaveTheSameNickPredicate(String nick) {
		return (e) ->  StringUtils.equals(e.getNick(), nick);
	}
	
	
}
