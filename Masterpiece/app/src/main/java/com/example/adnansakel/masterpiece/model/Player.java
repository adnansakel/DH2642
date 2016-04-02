package com.example.adnansakel.masterpiece.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Daniel on 02/04/2016.
 */
public class Player {
    //TODO: Please set up according to Firebase (e.g. Name + ID)

    // Variable Definition
    String name;
    String firebaseid;
    Integer totalvalue;
    Integer cash;
    //TODO: Question: Should this be private?
    Set<Painting> ownedPaintings = new HashSet<Painting>();

    public Player(String name, String firebaseid, Integer cash, Set<Painting> ownedPaintings) {
        this.name = name;
        this.firebaseid = firebaseid;
        this.cash = cash;
        this.ownedPaintings = ownedPaintings;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getFirebaseid() {
        return firebaseid;
    }
    public void setFirebaseid(String firebaseid) {
        this.firebaseid = firebaseid;
    }
    public Integer getCash() {
        return cash;
    }
    public void setCash(Integer cash) {
        this.cash = cash;
    }

    public Set<Painting> getOwnedPaintings(){
        return ownedPaintings;
    }

    // TODO: Once we have an interface, add @Override
    public void addPainting(Painting painting) {
        ownedPaintings.add(painting);
        //setChanged();
        //notifyObservers(painting);
    }

    // TODO: Once we have an interface, add @Override
    public void removePainting(Painting painting) {
        ownedPaintings.remove(painting); //TODO: Does this work?
    }
}
