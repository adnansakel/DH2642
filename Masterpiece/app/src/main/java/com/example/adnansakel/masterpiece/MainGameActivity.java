package com.example.adnansakel.masterpiece;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.adnansakel.masterpiece.model.AppConstants;
import com.example.adnansakel.masterpiece.model.MasterpieceGameModel;
import com.example.adnansakel.masterpiece.model.Painting;
import com.example.adnansakel.masterpiece.model.Player;
import com.example.adnansakel.masterpiece.view.MainGameView;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.List;
import java.util.Random;

/**
 * Created by Daniel on 02/04/2016.
 */
public class MainGameActivity extends Activity implements View.OnClickListener {

    MasterpieceGameModel model;
    Player myPlayer;
    Integer myPlayerID;
    Integer secondPlayerID;
    Integer thirdPlayerID;
    Integer fourthPlayerID;
    Player selectedPlayer = new Player();
    MainGameView mainGameView;

    Button button_status_bar;
    Button button_secondPlayer;
    Button button_thirdPlayer;
    Button button_fourthPlayer;
    Button button_start_turn;
    Button button_begin_bank_auction;

    View fullscreen_status_popup;

    boolean myTurn = false;
    boolean statusPopupIsVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        // Adding the model
        model = ((MasterpieceApplication) this.getApplication()).getModel();
        List<Player> players = model.getAllPlayers();
        myPlayer = model.getMyPlayer();
        mainGameView = new MainGameView(findViewById(R.id.maingame_overview_view),model);

        //find views of buttons
        button_status_bar = (Button)findViewById(R.id.buttonStatusBar);
        button_start_turn = (Button)findViewById(R.id.btnRoll); // Changed from OLDBUTTONROLL
        button_begin_bank_auction = (Button)findViewById(R.id.btn_begin_bank_auction);

        //find popup
        fullscreen_status_popup = findViewById(R.id.fullscreenStatusPopup);

        //find buttons for top section of the overview
        button_secondPlayer = (Button)findViewById(R.id.btnSecondPlayer);
        button_thirdPlayer = (Button)findViewById(R.id.btnThirdPlayer);
        button_fourthPlayer = (Button)findViewById(R.id.btnFourthPlayer);

        myPlayerID = Integer.valueOf(model.getMyPlayer().getPlayerpositionID());
        secondPlayerID = (myPlayerID + 1)%4;
        thirdPlayerID = (myPlayerID + 2)%4;
        fourthPlayerID = (myPlayerID + 3)%4;

        button_secondPlayer.setText(model.getAllPlayers().get(secondPlayerID).getName());
        button_thirdPlayer.setText(model.getAllPlayers().get(thirdPlayerID).getName());
        button_fourthPlayer.setText(model.getAllPlayers().get(fourthPlayerID).getName());

        button_secondPlayer.setBackgroundColor(Color.parseColor(AppConstants.MAINCOLOR));

        //load pictures for the active player (2)
        mainGameView.populatePaintingsOtherPlayers(secondPlayerID);

        //load pictures for myPlayer
        mainGameView.populatePaintingsMyPlayer(myPlayerID);

        //set click listeners
        button_status_bar.setOnClickListener(this);
        button_start_turn.setOnClickListener(this);
        button_secondPlayer.setOnClickListener(this);
        button_thirdPlayer.setOnClickListener(this);
        button_fourthPlayer.setOnClickListener(this);

        //start listening to changes in Firebase
        ListenForFirebaseGameEvents();

    }

    public void ListenForFirebaseGameEvents() {

        //listen for changes in TurnTaker in Firebase
        Firebase turnTakerRef = new Firebase(AppConstants.GameRef+"/"+"TurnTaker");
        turnTakerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                System.out.println("TurnTaker changed to: " + snapshot.getValue());

                //if the TurnTaker is me
                if (snapshot.getValue() == model.getMyPlayer().getPlayerpositionID()) {

                    //tell model that popupContent needs to change
                    model.setPopupContent("startTurn");

                    //button_start_turn.setOnClickListener(MainGameActivity.this);
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        //listen for changes in CurrentBidder in Firebase
        Firebase currentBidderRef = new Firebase(AppConstants.GameRef+"/"+"CurrentBidder");
        turnTakerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                System.out.println("CurrentBidder changed to: " + snapshot.getValue());

                //if the CurrentBidder is me
                if (snapshot.getValue() == model.getMyPlayer().getPlayerpositionID()) {

                    bidOnPainting();
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        //listen for changes in TurnAction in Firebase
        Firebase turnActionRef = new Firebase(AppConstants.GameRef+"/"+"TurnAction");
        turnTakerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                System.out.println("TurnAction changed to: " + snapshot.getValue());

                //if the turn is a private auction
                if (snapshot.getValue() == "privateAuction") {

                    model.setPopupContent("privateAuctionInProgress");

                    model.setTurnAction("privateAuction");
                }

                //if the turn is a bank auction
                if (snapshot.getValue() == "bankAuction") {

                    model.setPopupContent("bankAuctionInProgress");

                    model.setTurnAction("bankAuction");
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }


    @Override
    public void onClick(View v) {
        if(v == button_status_bar) {

            if(statusPopupIsVisible == false) {

                //show the fullscreen popup
                fullscreen_status_popup.setVisibility(View.VISIBLE);

                //toggle the button to hide popup next time it's pressed
                statusPopupIsVisible = true;
            }
            else {

                //hide the fullscreen popup
                fullscreen_status_popup.setVisibility(View.GONE);

                //toggle the button to show popup next time it's pressed
                statusPopupIsVisible = false;
            }
        }
        else if(v == button_start_turn) {

            startTurn();

        } else if(v == button_secondPlayer) {
            model.setCurrentPlayerToDisplayID(secondPlayerID);
            button_secondPlayer.setBackgroundColor(Color.parseColor(AppConstants.MAINCOLOR));
            button_thirdPlayer.setBackgroundColor(getResources().getColor(R.color.colorButtonInactive));
            button_fourthPlayer.setBackgroundColor(getResources().getColor(R.color.colorButtonInactive));
        } else if(v == button_thirdPlayer) {
            model.setCurrentPlayerToDisplayID(secondPlayerID);
            //TODO Change setting color from view
            button_secondPlayer.setBackgroundColor(getResources().getColor(R.color.colorButtonInactive));
            button_thirdPlayer.setBackgroundColor(Color.parseColor(AppConstants.MAINCOLOR));
            button_fourthPlayer.setBackgroundColor(getResources().getColor(R.color.colorButtonInactive));
        } else if(v == button_fourthPlayer) {
            model.setCurrentPlayerToDisplayID(secondPlayerID);
            button_secondPlayer.setBackgroundColor(getResources().getColor(R.color.colorButtonInactive));
            button_thirdPlayer.setBackgroundColor(getResources().getColor(R.color.colorButtonInactive));
            button_fourthPlayer.setBackgroundColor(Color.parseColor(AppConstants.MAINCOLOR));
        } else if(v == button_begin_bank_auction) {

            //TODO: the below code could be moved to its own function

            //set turn action
            //model.setTurnAction("bankAuction");
            new Firebase(AppConstants.GameRef+"/"+"TurnAction").setValue("bankAuction");

            //choose painting being auctioned (first painting in Paintings[])
            int chosenPaintingToAuction = model.getMyPlayer().getOwnedPaintingIDs().get(0);

            //set painting being auctioned
            model.setPaintingBeingAuctioned(chosenPaintingToAuction);

            //set current bidder as the next person in allPlayers[]
            model.setCurrentBidder(model.getCurrentBidder());
        }

        //TODO: if bid or don't bid buttons for private or public auction
    }


    //executed whenever the turnTaker changes and is equal to my player
    public void startTurn() {

        //randomly select a turn type (roll the dice)
        Random rn = new Random();
        int roll = rn.nextInt(1); //there are only 2 types so far

        //hide the start turn layout
        //layoutStatusPopup.removeView(layoutPopupGameModelSelection);

        if(roll == 0){

            //PRIVATE AUCTION

            //show the private auction select painting layout
            model.setPopupContent("privateAuctionSelectPainting");

            //TODO: need to access my owned paintings

            //TODO: display my paintings in the select painting layout

            //TODO: allow my player to select a painting

            //set painting being auctioned
            //model.setPaintingBeingAuctioned(chosenPaintingToAuction);
            new Firebase(AppConstants.GameRef+"/"+"PaintingBeingAuctioned").setValue("");
            //TODO: set the value of my painting in the above

            //TODO: set my player's bidding property to false in Firebase (eg. It's my auction so I'm done bidding)

            //set turn action
            //model.setTurnAction("privateAuction");
            new Firebase(AppConstants.GameRef+"/"+"TurnAction").setValue("privateAuction");

            //set current bidder as the next player
            //model.setCurrentBidder(model.getNextPlayer());
            //TODO: get the next player
            new Firebase(AppConstants.GameRef+"/"+"CurrentBidder").setValue("");

            //bidding on private auction


        } else if(roll == 1){

            //BANK AUCTION

            model.setPopupContent("bankAuctionBegin");

            //start listener on begin auction button
            button_begin_bank_auction.setOnClickListener(MainGameActivity.this);

        }

    }

    //executed when currentBidder = myPlayer
    public void bidOnPainting() {

        //if this is a private auction
        if(model.getTurnAction() == "privateAuction") {

            model.setPopupContent("privateAuctionBid");

            //TODO: start listener for bid and don't bid buttons
        }
        //if this is a bank auction
        else if (model.getTurnAction() == "bankAuction") {

            model.setPopupContent("bankAuctionBid");

            //TODO: start listener for bid and don't bid buttons
        }
    }

}
