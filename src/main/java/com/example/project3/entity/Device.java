package com.example.project3.entity;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "device")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_place")
    private Place place;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_kind_device")
    private KindDevice kindDevice;

    private boolean active;

    @Column(name = "created_date")
    private Instant createdData;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public KindDevice getKindDevice() {
        return kindDevice;
    }

    public void setKindDevice(KindDevice kindDevice) {
        this.kindDevice = kindDevice;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Instant getCreatedData() {
        return createdData;
    }

    public void setCreatedData(Instant createdData) {
        this.createdData = createdData;
    }
}