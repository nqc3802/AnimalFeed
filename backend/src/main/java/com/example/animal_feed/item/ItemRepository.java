package com.example.animal_feed.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Items, Integer> {
    Items findById(int id);
    Items findOneByCode(String code);
    Items deleteById(int id);
}
