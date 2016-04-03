package com.example.adnansakel.masterpiece.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
    //Set<Painting> ownedPaintings = new HashSet<Painting>();//why using hash set ?
    List<Painting> ownedPaintings;
    List<Integer> ownedPaintingIDs;

    public Player(String name, String firebaseid, Integer cash, ArrayList<Painting> ownedPaintings) {
        this.name = name;
        this.firebaseid = firebaseid;
        this.cash = cash;
        this.ownedPaintings = new ArrayList<Painting>();
        this.ownedPaintings = ownedPaintings;
    }

    public Player(){
        ownedPaintingIDs = new ArrayList<Integer>();
        ownedPaintings = new ArrayList<Painting>();
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

    public List<Painting> getOwnedPaintings(){
        return ownedPaintings;
    }

    public List<Integer> getOwnedPaintingIDs(){ return ownedPaintingIDs; }

    public void addOwnedPaintingIDs(int paintigID){
        ownedPaintingIDs.add(paintigID);
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
