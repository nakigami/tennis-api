package com.bforbank.tennis_api.infrastructure.adapter.repository;

import com.bforbank.tennis_api.domain.model.Match;
import com.bforbank.tennis_api.domain.model.MatchStatus;
import com.bforbank.tennis_api.domain.model.Player;
import com.bforbank.tennis_api.domain.port.output.MatchRepository;
import com.bforbank.tennis_api.infrastructure.adapter.repository.entity.MatchEntity;
import com.bforbank.tennis_api.infrastructure.adapter.repository.springdata.SpringDataMatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class MatchRepositoryImpl implements MatchRepository {

    private final SpringDataMatchRepository springDataMatchRepository;

    @Override
    public Match save(Match match) {
        MatchEntity entity = toEntity(match);
        MatchEntity saved = springDataMatchRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Match> findById(String matchId) {
        return springDataMatchRepository.findById(matchId)
                .map(this::toDomain);
    }

    @Override
    public List<Match> findAll() {
        return springDataMatchRepository.findAll()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String matchId) {
        springDataMatchRepository.deleteById(matchId);
    }

    private MatchEntity toEntity(Match match) {
        MatchEntity entity = new MatchEntity();
        entity.setId(match.getId());

        entity.setPlayerAName(match.getPlayerA().getName());
        entity.setPlayerAPoints(match.getPlayerA().getPoints());
        entity.setPlayerAAdvantage(match.getPlayerA().isHasAdvantage());

        entity.setPlayerBName(match.getPlayerB().getName());
        entity.setPlayerBPoints(match.getPlayerB().getPoints());
        entity.setPlayerBAdvantage(match.getPlayerB().isHasAdvantage());

        entity.setWinner(match.getWinner());
        entity.setStatus(match.getStatus().name());

        entity.setDate(match.getDate());

        return entity;
    }

    private Match toDomain(MatchEntity entity) {
        Player playerA = new Player(entity.getPlayerAName());
        playerA.setPoints(entity.getPlayerAPoints());
        playerA.setHasAdvantage(entity.isPlayerAAdvantage());

        Player playerB = new Player(entity.getPlayerBName());
        playerB.setPoints(entity.getPlayerBPoints());
        playerB.setHasAdvantage(entity.isPlayerBAdvantage());

        Match match = new Match(playerA, playerB);
        match.setWinner(entity.getWinner());
        match.setStatus(MatchStatus.valueOf(entity.getStatus()));
        match.setDate(entity.getDate());

        return match;
    }
}
