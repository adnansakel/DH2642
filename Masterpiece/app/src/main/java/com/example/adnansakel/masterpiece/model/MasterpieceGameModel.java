package com.example.adnansakel.masterpiece.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.adnansakel.masterpiece.R;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static android.content.Context.*;

/**
 * Created by Adnan Sakel on 3/28/2016.
 */
public class MasterpieceGameModel {
    private String gameNumber;
    private Set<Player> allPlayers = new HashSet<Player>();

    public MasterpieceGameModel(){
        // TEMPORARY: This is just for testing, will be replaced by firebase data model
        Bitmap test = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Painting pa1 = new Painting("Painting 1","http://res.cloudinary.com/masterpiece/image/upload/v1459241655/1.jpg", test, "A description",100000);
        Painting pa2 = new Painting("Painting 2","http://res.cloudinary.com/masterpiece/image/upload/v1459241655/1.jpg", test,"A description",300000);
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
        System.out.println("Player 4: " + p4);
    }

    public void setGameNumber(String gameNumber){
        this.gameNumber = gameNumber;
    }

    public String getGameNumber(){
        return gameNumber;
    }

    public Set<Player> getAllPlayers(){
        return allPlayers;
    }

    public Set<Painting> getPaintingsByPlayerID(){
        //TODO: Define proper function with by ID or object
        Bitmap test = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Painting pa1 = new Painting("Painting 1","http://res.cloudinary.com/masterpiece/image/upload/v1459241655/1.jpg", test, "A description",100000);
        Painting pa2 = new Painting("Painting 2","http://res.cloudinary.com/masterpiece/image/upload/v1459241655/1.jpg", test,"A description",300000);
        Set<Painting> setofpaintings = new HashSet<Painting>(Arrays.asList(pa1,pa2));
        return setofpaintings;
    }

    public Set<Painting> getAllPaintings(){
        //TODO: Define proper function with by ID or object
        Bitmap test = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Painting pa1 = new Painting("Painting 1","http://res.cloudinary.com/masterpiece/image/upload/v1459241655/1.jpg", test, "A description",100000);
        Painting pa2 = new Painting("Painting 2","http://res.cloudinary.com/masterpiece/image/upload/v1459241655/1.jpg", test,"A description",300000);
        Set<Painting> setofpaintings = new HashSet<Painting>(Arrays.asList(pa1,pa2));
        return setofpaintings;
    }
}
