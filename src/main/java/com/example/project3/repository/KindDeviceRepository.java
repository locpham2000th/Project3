package com.example.project3.repository;

import com.example.project3.entity.KindDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KindDeviceRepository extends JpaRepository<KindDevice, Long> {
}
