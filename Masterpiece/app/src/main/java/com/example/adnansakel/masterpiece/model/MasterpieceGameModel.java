package com.example.adnansakel.masterpiece.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.adnansakel.masterpiece.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

import static android.content.Context.*;

/**
 * Created by Adnan Sakel on 3/28/2016.
 */
public class MasterpieceGameModel extends Observable{

    private String gameNumber;
    private List<Player> allPlayers;
    private List<Painting> allPaintings;
    private List<Integer> allPaintingValues;
    private Player turnTaker;
    private String turnAction;
    private Painting paintingBeingAuctioned;
    private Player currentBidder;
    private Player myPlayer;
    private Player nextPlayer;

    public MasterpieceGameModel(){

        allPlayers = new ArrayList<Player>();
        allPaintings = new ArrayList<Painting>();
        List<Integer> allPaintingValues = Arrays.asList(200000, 200000, 200000, 500000, 500000, 1000000); //prepopulated with fixed values
        /*
        // TEMPORARY: This is just for testing, will be replaced by firebase data model
        Bitmap test = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Painting pa1 = new Painting("Painting 1","http://res.cloudinary.com/masterpiece/image/upload/v1459241655/1.jpg", test, "A description",100000);
        Painting pa2 = new Painting("Painting 2","http://res.cloudinary.com/masterpiece/image/upload/v1459241655/1.jpg", test,"A description",300000);
        List<Painting> listOfPaintings = new ArrayList<Painting>();
        listOfPaintings.add(pa1);
        listOfPaintings.add(pa2);

        Player p1 = new Player("P1","1",200000,listOfPaintings);
        Player p2 = new Player("P2","2",200000,listOfPaintings);
        Player p3 = new Player("P3","3",200000,listOfPaintings);
        Player p4 = new Player("P4","4",200000,listOfPaintings);
        allPlayers.add(p1);
        allPlayers.add(p2);
        allPlayers.add(p3);
        allPlayers.add(p4);

        System.out.println("Player 1: " + p1);
        System.out.println("Player 1 Paintings: " + p1.ownedPaintings);
        System.out.println("Player 2: " + p2);
        System.out.println("Player 3: " + p3);
        System.out.println("Player 4: " + p4);
        */
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

    public void setTurnTaker(Player turnTaker){
        this.turnTaker = turnTaker;
    }

    public Player getTurnTaker(){
        return turnTaker;
    }

    public void setTurnAction(String turnAction){
        this.turnAction = turnAction;
    }

    public String getTurnAction() {
        return turnAction;
    }

    public void setPaintingBeingAuctioned(Painting paintingBeingAuctioned){
        this.paintingBeingAuctioned = paintingBeingAuctioned;
    }

    public Painting getPaintingBeingAuctioned(){
        return paintingBeingAuctioned;
    }

    public void setCurrentBidder(Player currentBidder){
        this.currentBidder = currentBidder;
    }

    public Player getCurrentBidder(){
        return currentBidder;
    }

    public void setMyPlayer(Player myPlayer){
        this.myPlayer = myPlayer;
    }
    //TODO: myPlayer is empty. need to set it when joining the game

    public Player getMyPlayer(){
        return myPlayer;
    }

    public void setNextPlayer(){
        Integer myPlayerIndex = allPlayers.indexOf(myPlayer);
        //if I'm player 4, next player will be player 1
        if (myPlayerIndex <= 2) {
            this.nextPlayer = allPlayers.get(myPlayerIndex + 1);
        } else {
            this.nextPlayer = allPlayers.get(0);
        }
    }
    //TODO: need to call this when joining the game

    public Player getNextPlayer(){
        return nextPlayer;
    }

    public Set<Painting> getPaintingsByPlayerID(){
        //TODO: Define proper function with by ID or object
        Bitmap test = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Painting pa1 = new Painting("Painting 1","http://res.cloudinary.com/masterpiece/image/upload/v1459241655/1.jpg", test, "A description",100000,"artist");
        Painting pa2 = new Painting("Painting 2","http://res.cloudinary.com/masterpiece/image/upload/v1459241655/1.jpg", test,"A description",300000,"artist");
        Set<Painting> setofpaintings = new HashSet<Painting>(Arrays.asList(pa1,pa2));
        return setofpaintings;
    }

    public Set<Painting> getAllPaintings(){
        //TODO: Define proper function with by ID or object
        Bitmap test = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Painting pa1 = new Painting("Painting 1","http://res.cloudinary.com/masterpiece/image/upload/v1459241655/1.jpg", test, "A description",100000,"artist");
        Painting pa2 = new Painting("Painting 2","http://res.cloudinary.com/masterpiece/image/upload/v1459241655/1.jpg", test,"A description",300000,"artist");
        Set<Painting> setofpaintings = new HashSet<Painting>(Arrays.asList(pa1,pa2));
        return setofpaintings;
    }

    public void addPlayer(Player player){
        allPlayers.add(player);
        setChanged();
        notifyObservers();
    }

    public void removeAllPlayer(){
        allPlayers.clear();
        setChanged();
        notifyObservers();
    }
}
