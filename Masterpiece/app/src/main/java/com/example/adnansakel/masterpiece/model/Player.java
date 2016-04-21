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
    //TODO: Should this be an observable

    // Variable Definition
    String name;
    String firebaseid;
    Integer cash;
    private int playerpositionID;
    //TODO: Question: Should this be private?

    //Set<Painting> ownedPaintings = new HashSet<Painting>();//why using hash set?
    List<Integer> ownedPaintingValues;
    List<Integer> ownedPaintingIDs;
    private boolean bidding;

    public Player(String name, String firebaseid, Integer cash) {
        this.name = name;
        this.firebaseid = firebaseid;
        this.cash = cash;
        ownedPaintingIDs = new ArrayList<Integer>();
        ownedPaintingValues = new ArrayList<Integer>();

    }

    public Player(){
        ownedPaintingIDs = new ArrayList<Integer>();
        ownedPaintingValues = new ArrayList<Integer>();
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

    public List<Integer> getOwnedPaintingValues(){
        return ownedPaintingValues;
    }

    public List<Integer> getOwnedPaintingIDs(){ return ownedPaintingIDs; }

    public void addOwnedPaintingID(int paintigID){
        ownedPaintingIDs.add(paintigID);
    }



    public void addOenedPaintingValue(int paintingValue){
        ownedPaintingValues.add(paintingValue);
    }

    // TODO: Once we have an interface, add @Override
    public void removePaintingID(int paintingID) {
        ownedPaintingIDs.remove(paintingID); //TODO: Does this work?
    }

    public void removePaintingValues(int paintingValue) {
        ownedPaintingIDs.remove(paintingValue); //TODO: Does this work?
    }

    public int getPlayerpositionID() {
        return playerpositionID;
    }

    public void setPlayerpositionID(int playerpositionID) {
        this.playerpositionID = playerpositionID;
    }

    public boolean isBidding() {
        return bidding;
    }

    public void setBidding(boolean bidding) {
        this.bidding = bidding;
    }
}
