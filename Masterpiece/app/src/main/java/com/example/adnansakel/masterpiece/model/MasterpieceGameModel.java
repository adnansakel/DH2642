package com.example.adnansakel.masterpiece.model;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Set;

/**
 * Created by Adnan Sakel on 3/28/2016.
 */
public class MasterpieceGameModel extends Observable{
    private String gameNumber;
    private List<Player> allPlayers;
    private List<Painting> allPaintings;
    private List<Integer> allPaintingValues;

    public MasterpieceGameModel(){

        allPlayers = new ArrayList<Player>();
        allPaintings = new ArrayList<Painting>();
        allPaintingValues = Arrays.asList(200000,200000,200000,500000,500000,1000000); //prepopulated with fixed values

        /*
        // TEMPORARY: This is just for testing, will be replaced by firebase data model
        Bitmap test = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Painting pa1 = new Painting("Painting 1","http://res.cloudinary.com/masterpiece/image/upload/v1459241655/1.jpg", test, "A description",100000, artist);
        Painting pa2 = new Painting("Painting 2","http://res.cloudinary.com/masterpiece/image/upload/v1459241655/1.jpg", test,"A description",300000, artist);
        Set<Painting> setofpaintings = new HashSet<Painting>(Arrays.asList(pa1,pa2));

        Player p1 = new Player("P1","1",200000,setofpaintings);
        Player p2 = new Player("P2","2",200000,setofpaintings);
        Player p3 = new Player("P3","3",200000,setofpaintings);
        Player p4 = new Player("P4","4",200000,setofpaintings);
        allPlayers.add(p1);
        allPlayers.add(p2);
        allPlayers.add(p3);
        allPlayers.add(p4);

        System.out.println("Player 1: " + p1);
        System.out.println("Player 1 Paintings: " + p1.ownedPaintings);
        System.out.println("Player 2: " + p2);
        System.out.println("Player 3: " + p3);
        System.out.println("Player 4: " + p4);*/
    }

    public void setGameNumber(String gameNumber){
        this.gameNumber = gameNumber;
    }

    public String getGameNumber(){
        return gameNumber;
    }

    public List<Player> getAllPlayers(){
        return allPlayers;
    }

    public void addPlayer(Player player){
        allPlayers.add(player);
        setChanged();
        notifyObservers();
    }

    //the paintings and values are in a list in order to easily randomize them
    public void setAllPaintings(List<Painting> allPaintings){
        this.allPaintings = allPaintings;
    }

    public List<Painting> getAllPaintings1(){ //name has 1 because of duplicate getAllPaintings below
        return allPaintings;
    }

    public void setAllPaintingValues(List<Integer> allPaintingValues){
        this.allPaintingValues = allPaintingValues;
    }

    public List<Integer> getAllPaintingValues(){
        return allPaintingValues;
    }

    public Set<Painting> getPaintingsByPlayerID(){
        //TODO: Define proper function with by ID or object
        Bitmap test = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Painting pa1 = new Painting("Painting 1","http://res.cloudinary.com/masterpiece/image/upload/v1459241655/1.jpg", test, "A description",100000, "artist");
        Painting pa2 = new Painting("Painting 2","http://res.cloudinary.com/masterpiece/image/upload/v1459241655/1.jpg", test,"A description",300000, "artist");
        Set<Painting> setofpaintings = new HashSet<Painting>(Arrays.asList(pa1,pa2));
        return setofpaintings;
    }

    public Set<Painting> getAllPaintings(){
        //TODO: Define proper function with by ID or object
        Bitmap test = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Painting pa1 = new Painting("Painting 1","http://res.cloudinary.com/masterpiece/image/upload/v1459241655/1.jpg", test, "A description",100000, "artist");
        Painting pa2 = new Painting("Painting 2","http://res.cloudinary.com/masterpiece/image/upload/v1459241655/1.jpg", test,"A description",300000, "artist");
        Set<Painting> setofpaintings = new HashSet<Painting>(Arrays.asList(pa1,pa2));
        return setofpaintings;
    }

    public void removeAllPlayer(){
        allPlayers.clear();
        setChanged();
        notifyObservers();
    }
}
