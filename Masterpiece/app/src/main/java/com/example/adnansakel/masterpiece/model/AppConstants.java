package com.example.adnansakel.masterpiece.model;

import android.graphics.Color;

/**
 * Created by Adnan Sakel on 3/28/2016.
 */
public class AppConstants {

    public static String FireBaseUri = "https://master-piece.firebaseio.com/";
    public static String GameID = "";//later on will be saved in app preference
    public static String GameRef = "";//later on will be saved in app preference
    public static String PlayerRef = "";//later on will be saved in app preference//Ref for player who is playing on this device
    public static boolean IamCreator = false;


    public static int TotalNumberofPainting = 10;
    public static int TotalNumberofPlayers = 4;
    // All values which just represent a string are in capital letters
    public static String GAMENUMBER = "GameNumber";

    public static String CURRENTBID = "CurrentBid";
    public static String COUNTNONBIDDERS = "CountNonBidders";
    public static String GAMENR = "GameNr";
    public static String GAMESTATE = "GameState";
    public static String NUMBEROFPLAYERS = "NumberofPlayers";
    public static String PAINTINGBEINGAUCTIONED = "PaintingBeingAuctioned";
    public static String PLAYERS = "Players";
    public static String BIDAMOUNT = "BidAmount";
    public static String CASH = "Cash";
    public static String NAME = "Name";
    public static String PAINTINGS = "Paintings";
    public static String TURNACTION = "TurnAction";
    public static String TURNTAKER = "TurnTaker";
    public static String BIDDING = "Bidding";
    public static String GAMES = "Games";
    public static String GAMESTARTED = "GameStarted";
    public static String BIDDINGINCREMENT = "50000";
    public static String STARTINGCASH = "250000";

    public static String ARTIST = "Artist";
    public static String IMAGE = "Image";
    public static String DESCRIPTION = "Description";

    public static String SHUFFLEDPAINTINGVALUES = "ShuffledPaintingValues";
    public static String SHUFFLEDPAINTINGS = "ShuffledPaintings";

    public static String CURRENTBIDDER = "CurrentBidder";
    public static String TURN_TAKER_CHANGED = "turnTakerChanged";
    public static String CURRENT_BIDDER_CHANGED = "CurrentBidderChanged";
    public static String TURN_ACTION_CHANGED = "turnActionChanged";
    public static String PRIVATE = "private";
    public static String BANK = "bank";
    public static String WINNERFOUND = "WinnerFound";
    public static String NOTIFY_FOR_UPDATED_PAINTING_AND_CASH = "notifyforupdatedpaintingandcash";
    public static int TotalNumberofRounds = 0;
    public static String TOTAL_ROUNDS = "TotalRounds";

}
