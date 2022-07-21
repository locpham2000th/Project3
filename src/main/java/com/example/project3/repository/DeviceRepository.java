package com.example.project3.repository;

import com.example.project3.entity.Device;
import com.example.project3.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    List<Device> findAllByPlace(Place places);

    Optional<Device> findByPlaceAndName(Place place, String name);

}
