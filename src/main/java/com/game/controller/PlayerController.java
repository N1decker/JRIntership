package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/rest")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/players")
    public ResponseEntity<List<Player>> getPlayersList(@RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,
                                                       @RequestParam(value = "pageSize", required = false, defaultValue = "3") int pageSize,
                                                       @RequestParam(value = "order", required = false, defaultValue = "id") String order,
                                                       @RequestParam Map<String, String> allParams){
        List<Player> players = playerService.getAllPlayers(pageNumber,pageSize,order.toLowerCase(), allParams);
        return new ResponseEntity<>(players, HttpStatus.OK);
    }

    @GetMapping("/players/count")
    public Long getPlayersCount(@RequestParam Map<String, String> allParams){
        return playerService.count(allParams);
    }

    @GetMapping("/players/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable String id){

        Long longId = playerService.checkId(id);
        return new ResponseEntity<>(playerService.getPlayerById(longId), HttpStatus.OK);
    }

    @DeleteMapping(value = "/players/{id}")
    public ResponseEntity<?> deletePlayer(@PathVariable String id) {
        Long longId = playerService.checkId(id);
        playerService.deletePlayer(longId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/players")
    public Player createPlayer(@RequestBody Player player) {
        return playerService.createPlayer(player);
    }

    @PostMapping("/players/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable String id, @RequestBody Map<String, String> allParams){
        Long longId = playerService.checkId(id);
        return new ResponseEntity<>(playerService.updatePlayer(longId, allParams), HttpStatus.OK);
    }

}
