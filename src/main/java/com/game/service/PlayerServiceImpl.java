package com.game.service;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Service
public class PlayerServiceImpl implements PlayerService{

    final
    PlayerRepository playerRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }


    @Override
    public Long count(@RequestParam Map<String, String> allParams) {
        //Фильтрация по имени
        PlayerSpecification name = null;
        if (allParams.get("name")!=null) {
            name = new PlayerSpecification("name", "%"+allParams.get("name")+"%");
        }

        //Фильтрация по прозвищу(title)
        PlayerSpecification title = null;
        if (allParams.get("title")!=null) {
            name = new PlayerSpecification("title", "%"+allParams.get("title")+"%");
        }

        //Фильтрация по race
        Specification race = null;
        if (allParams.get("race")!=null){
            race = new Specification() {
                @Override
                public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
                    return criteriaBuilder.equal(root.get("race"),Race.valueOf(allParams.get("race")));
                }
            };
        }

        //Фильтрация по profession
        Specification profession = null;
        if (allParams.get("profession")!=null) {
            profession = new Specification() {
                @Override
                public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
                    return criteriaBuilder.equal(root.get("profession"), Profession.valueOf(allParams.get("profession")));
                }
            };
        }

        //Фильтрация по minExperience
        Specification minExperience = null;
        if (allParams.get("minExperience")!=null){
            minExperience = new Specification() {
                @Override
                public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
                    return criteriaBuilder.greaterThan(root.get("experience").as(Integer.class), Integer.parseInt(allParams.get("minExperience")));
                }
            };
        }

        //Фильтрация по maxExperience
        Specification maxExperience = null;
        if (allParams.get("maxExperience")!=null){
            maxExperience = new Specification() {
                @Override
                public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
                    return criteriaBuilder.lessThan(root.get("experience").as(Integer.class), Integer.parseInt(allParams.get("maxExperience")));
                }
            };
        }

        //Фильтрация по minLevel
        Specification minLevel = null;
        if (allParams.get("minLevel")!=null){
            minLevel = new Specification() {
                @Override
                public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
                    return criteriaBuilder.greaterThanOrEqualTo(root.get("level").as(Integer.class),Integer.parseInt(allParams.get("minLevel")));
                }
            };
        }

        //Фильтрация по maxLevel
        Specification maxLevel = null;
        if (allParams.get("maxLevel")!=null){
            maxLevel = new Specification() {
                @Override
                public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
                    return criteriaBuilder.lessThanOrEqualTo(root.get("level").as(Integer.class),Integer.parseInt(allParams.get("maxLevel")));
                }
            };
        }

        //Фильтрация по minBirthday
        Specification minBirthday = null;
        if (allParams.get("after")!=null){
            minBirthday = new Specification() {
                @Override
                public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
                    return criteriaBuilder.greaterThanOrEqualTo(root.get("birthday"), new Date(Long.parseLong(allParams.get("after"))));
                }
            };
        }

        //Фильтрация по maxBirthday
        Specification maxBirthday = null;
        if (allParams.get("before")!=null){
            maxBirthday = new Specification() {
                @Override
                public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
                    return criteriaBuilder.lessThanOrEqualTo(root.get("birthday"), new Date(Long.parseLong(allParams.get("before"))));
                }
            };
        }

        //Фильтрация по banned
        Specification banned = null;
        if (allParams.get("banned")!= null) {
            banned = new Specification() {
                @Override
                public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
                    return criteriaBuilder.equal(root.get("banned"), Boolean.parseBoolean(allParams.get("banned")));
                }
            };
        }

        return playerRepository.count(Specification.where(name).and(title).and(race).and(profession).and(minExperience).
                and(maxExperience).and(minLevel).and(maxLevel).and(minBirthday).and(maxBirthday).and(banned));
    }

    @Override
    public List<Player> getAllPlayers(int pageNumber, int pageSize, String order,
                                      @RequestParam Map<String,String> allParams) {
//        List<Player> players = new ArrayList<>();


        //Фильтрация по имени
        PlayerSpecification name = null;
        if (allParams.get("name")!=null) {
            name = new PlayerSpecification("name", "%"+allParams.get("name")+"%");
        }

        //Фильтрация по прозвищу(title)
        PlayerSpecification title = null;
        if (allParams.get("title")!=null) {
            name = new PlayerSpecification("title", "%"+allParams.get("title")+"%");
        }

        //Фильтрация по race
        Specification race = null;
        if (allParams.get("race")!=null){
            race = new Specification() {
                @Override
                public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
                    return criteriaBuilder.equal(root.get("race"),Race.valueOf(allParams.get("race")));
                }
            };
        }


        //Фильтрация по profession
        Specification profession = null;
        if (allParams.get("profession")!=null) {
            profession = new Specification() {
                @Override
                public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
                    return criteriaBuilder.equal(root.get("profession"), Profession.valueOf(allParams.get("profession")));
                }
            };
        }

        //Фильтрация по minExperience
        Specification minExperience = null;
        if (allParams.get("minExperience")!=null){
            minExperience = new Specification() {
                @Override
                public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
                    return criteriaBuilder.greaterThan(root.get("experience").as(Integer.class), Integer.parseInt(allParams.get("minExperience")));
                }
            };
        }

        //Фильтрация по maxExperience
        Specification maxExperience = null;
        if (allParams.get("maxExperience")!=null){
            maxExperience = new Specification() {
                @Override
                public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
                    return criteriaBuilder.lessThan(root.get("experience").as(Integer.class), Integer.parseInt(allParams.get("maxExperience")));
                }
            };
        }

        //Фильтрация по minLevel
        Specification minLevel = null;
        if (allParams.get("minLevel")!=null){
            minLevel = new Specification() {
                @Override
                public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
                    return criteriaBuilder.greaterThanOrEqualTo(root.get("level").as(Integer.class),Integer.parseInt(allParams.get("minLevel")));
                }
            };
        }

        //Фильтрация по maxLevel
        Specification maxLevel = null;
        if (allParams.get("maxLevel")!=null){
            maxLevel = new Specification() {
                @Override
                public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
                    return criteriaBuilder.lessThanOrEqualTo(root.get("level").as(Integer.class),Integer.parseInt(allParams.get("maxLevel")));
                }
            };
        }

        //Фильтрация по minBirthday
        Specification minBirthday = null;
        if (allParams.get("after")!=null){
            minBirthday = new Specification() {
                @Override
                public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
                    return criteriaBuilder.greaterThanOrEqualTo(root.get("birthday"), new Date(Long.parseLong(allParams.get("after"))));
                }
            };
        }

        //Фильтрация по maxBirthday
        Specification maxBirthday = null;
        if (allParams.get("before")!=null){
            maxBirthday = new Specification() {
                @Override
                public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
                    return criteriaBuilder.lessThanOrEqualTo(root.get("birthday"), new Date(Long.parseLong(allParams.get("before"))));
                }
            };
        }

        //Фильтрация по banned
        Specification banned = null;
        if (allParams.get("banned")!= null) {
            banned = new Specification() {
                @Override
                public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
                    return criteriaBuilder.equal(root.get("banned"), Boolean.parseBoolean(allParams.get("banned")));
                }
            };
        }


        Pageable paging = PageRequest.of(pageNumber,pageSize,Sort.by(order));
        Page<Player> pagedResult =
                playerRepository.findAll(Specification.where(name).and(title).and(race).and(profession).and(minExperience).
                        and(maxExperience).and(minLevel).and(maxLevel).and(title).and(minBirthday).and(maxBirthday).and(banned),paging);
        return pagedResult.getContent();
    }

    @Override
    public Player getPlayerById(long id) {
        if (!playerRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return playerRepository.findById(id).get();
    }

    @Override
    public void deletePlayer(Long id) {
        if (!playerRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        playerRepository.deleteById(id);
    }   

    @Override
    public Player createPlayer(Player player) {
        Player newPlayer = new Player();

        if (player.getName()==null || player.getTitle() == null || player.getRace() == null ||
                player.getProfession() == null || player.getBirthday() == null || player.isBanned() == null ||
                player.getExperience() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        newPlayer.setBanned(player.isBanned());

        newPlayer.setName(checkName(player.getName()));

        newPlayer.setTitle(checkTitle(player.getTitle()));

        newPlayer.setBirthday(checkBirthday(player.getBirthday()));

        newPlayer.setProfession(player.getProfession());

        newPlayer.setRace(player.getRace());

        newPlayer.setExperience(checkExperience(player.getExperience()));

        newPlayer.setLevel(calculateLevel(newPlayer.getExperience()));

        newPlayer.setUntilNextLevel(calculateUntilNextLevel(newPlayer.getLevel(), newPlayer.getExperience()));
        return playerRepository.save(newPlayer);
    }

    @Override
    public Player updatePlayer(Long id, Map<String, String> allParams) {

        if (!playerRepository.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        Player updatedPlayer = playerRepository.findById(id).get();

        if (allParams.get("name")!=null) updatedPlayer.setName(checkName(allParams.get("name")));

        if (allParams.get("title")!=null) updatedPlayer.setTitle(checkTitle(allParams.get("title")));

        if (allParams.get("race")!=null) updatedPlayer.setRace(Race.valueOf(allParams.get("race")));

        if (allParams.get("profession")!=null) updatedPlayer.setProfession(Profession.valueOf(allParams.get("profession")));

        if (allParams.get("experience")!=null) updatedPlayer.setExperience(checkExperience(Integer.parseInt(allParams.get("experience"))));

        updatedPlayer.setLevel(calculateLevel(updatedPlayer.getExperience()));

        updatedPlayer.setUntilNextLevel(calculateUntilNextLevel(updatedPlayer.getLevel(), updatedPlayer.getExperience()));

        if (allParams.get("birthday")!=null) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(Long.parseLong(allParams.get("birthday")));
            updatedPlayer.setBirthday(checkBirthday(cal.getTime()));
        }

        if (allParams.get("banned")!=null) updatedPlayer.setBanned(Boolean.parseBoolean(allParams.get("banned")));

        return updatedPlayer;
    }

////////////////////////////////////////////////////////////////////

    public Integer calculateLevel(int experience){


        int level = (int) ((Math.sqrt(2500 + 200 * experience) - 50) / 100);
        return level;
    }

    public Integer calculateUntilNextLevel(int level, int experience){

        int untilNextLevel = 50 * (level+1) * (level+2) - experience;
        return untilNextLevel;
    }

    public String checkName(String name){
        if (name==null || name==""){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (name.length()>12){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return name;
    }

    public String checkTitle(String title){
        if (title==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (title.length()>30){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return title;
    }

    public Integer checkExperience(int experience){

        if (experience < 0 || experience > 10_000_000){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return experience;
    }

    public Date checkBirthday(Date birthday){
        if (birthday==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (birthday.getTime()<=0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return birthday;
    }

    public Long checkId(String id) {
        Long longId = null;

        if (id == null || id.equals("0")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        try {
            longId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (longId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return longId;
    }

}

class PlayerSpecification implements Specification<Player> {
    private String filterIn;
    private String criteria;

    public PlayerSpecification(String filterIn, String criteria) {
        this.filterIn = filterIn;
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Player> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.like(root.get(filterIn), criteria);
    }
}
