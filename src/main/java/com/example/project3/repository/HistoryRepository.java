package com.example.project3.repository;

import com.example.project3.entity.Device;
import com.example.project3.entity.History;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {

    @Query(value = "select h from History h where h.device = ?1 order by h.id desc")
    List<History> findHistoryNewByDevice(Device device);

//    @Query(value = "select h from History h where h.device = ?1 order by h.id desc ")
//    History findByDevice(Device device);

    Optional<History> findFirstByDeviceOrderByIdDesc(Device device);

//    History findFirstByDeviceOrderByIdDesc(Device);

    @Query(value = "select h from History h where h.device.id = ?1 And h.time < ?2 ")
    List<History> findHistoryByDeviceAndTimeBefore(long id, Instant time, Pageable pageable);

}
