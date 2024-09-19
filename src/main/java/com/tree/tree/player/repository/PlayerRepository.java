package com.tree.tree.player.repository;

import com.tree.tree.player.Player;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface PlayerRepository extends ReactiveCrudRepository<Player, Long> {

}
