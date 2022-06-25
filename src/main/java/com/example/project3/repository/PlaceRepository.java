package com.example.project3.repository;

import com.example.project3.entity.Place;
import com.example.project3.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

    List<Place> findAllByUser(User user);

    Optional<Place> findByUser(User user);

    Optional<Place> findByUserAndName(User user, String name);


}
