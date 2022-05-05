package com.game.repository;

import com.game.entity.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PlayerRepository extends PagingAndSortingRepository<Player, Long>, JpaSpecificationExecutor<Player> {

    Page<Player> findAll(Pageable pageable);

    @Override
    void deleteById(Long aLong);
}
