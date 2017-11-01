package com.hybhub.atp.impl;

import com.hybhub.atp.Player;
import com.hybhub.atp.enumeration.Backhand;
import com.hybhub.atp.enumeration.Handedness;

import java.time.LocalDate;
import java.time.Period;

public class DefaultPlayer implements Player {

    private String id;
    private String name;
    private String country;
    private LocalDate dob;
    private Handedness handedness;
    private Backhand backhand;
    private int weight;
    private int height;

    public DefaultPlayer() {
    }

    public DefaultPlayer(String id, String name, String country, LocalDate dob, Handedness handedness, Backhand backhand, int weight, int height) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.dob = dob;
        this.handedness = handedness;
        this.backhand = backhand;
        this.weight = weight;
        this.height = height;
    }

    public DefaultPlayer(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("%s from %s, age %d, id %s", getName(), getCountry(), getAge(), getId());
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof DefaultPlayer && ((DefaultPlayer) o).getId().equals(this.getId())){
            return true;
        }
        return false;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getCountry() {
        return country;
    }

    @Override
    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public int getAge() {
        return Period.between(getDob(), LocalDate.now()).getYears();
    }

    @Override
    public LocalDate getDob() {
        return dob;
    }

    @Override
    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    @Override
    public Handedness getHandedness() {
        return handedness;
    }

    @Override
    public void setHandedness(Handedness handedness) {
        this.handedness = handedness;
    }

    @Override
    public Backhand getBackhand() {
        return backhand;
    }

    @Override
    public void setBackhand(Backhand backhand) {
        this.backhand = backhand;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }
}
