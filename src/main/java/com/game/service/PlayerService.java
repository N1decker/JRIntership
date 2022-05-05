package com.game.service;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public interface PlayerService {

    List<Player> getAllPlayers(int pageNumber, int pageSize, String order,
                               @RequestParam Map<String,String> allParams);

    Long count(@RequestParam Map<String, String> allParams);

    Player getPlayerById(long id);

    Player createPlayer(Player player);

    Player updatePlayer(Long id, Map<String, String> allParams);

    void deletePlayer(Long id);

    Long checkId(String id);

}
