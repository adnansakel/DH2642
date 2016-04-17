package com.example.adnansakel.masterpiece;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.adnansakel.masterpiece.model.AppConstants;
import com.example.adnansakel.masterpiece.model.MasterpieceGameModel;
import com.example.adnansakel.masterpiece.model.Painting;
import com.example.adnansakel.masterpiece.model.Player;
import com.example.adnansakel.masterpiece.view.MainGameView;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

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
    Player selectedPlayer;
    MainGameView mainGameView;

    //Firebase masterpieceRef;
    Button button_status_bar;
    Button button_secondPlayer;
    Button button_thirdPlayer;
    Button button_fourthPlayer;
    Button button_start_turn;
    Button button_begin_bank_auction;

    View fullscreen_status_popup;
    LinearLayout layoutStatusPopup;
    RelativeLayout layoutPopupGameModelSelection;
    RelativeLayout layoutPopupPrivateAuctionInProgress;
    RelativeLayout layoutPopupBankAuctionInProgress;
    RelativeLayout layoutPopupPrivateAuctionSelectPainting;
    RelativeLayout layoutPopupBankAuctionBegin;

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
        MainGameView mainGameView = new MainGameView(findViewById(R.id.maingame_overview_view),model);

        // Load images
        ProgressDialog progress;
        progress = ProgressDialog.show(this, "", "Downloading paintings ...", true);
        ImageDownloader imageDownloader = new ImageDownloader(MainGameActivity.this,model);
        imageDownloader.downloadImages(progress);

        //find the buttons
        button_status_bar = (Button)findViewById(R.id.buttonStatusBar);
        button_start_turn = (Button)findViewById(R.id.btnRoll);
        button_begin_bank_auction = (Button)findViewById(R.id.btn_begin_bank_auction);

        //find popup & popup contents
        fullscreen_status_popup = findViewById(R.id.fullscreenStatusPopup);
        layoutStatusPopup = (LinearLayout)findViewById(R.id.fullscreenStatusPopup);
        layoutPopupGameModelSelection = (RelativeLayout)findViewById(R.id.game_mode_selection);
        layoutPopupPrivateAuctionInProgress = (RelativeLayout)findViewById(R.id.private_auction_in_progress);
        layoutPopupBankAuctionInProgress = (RelativeLayout)findViewById(R.id.bank_auction_in_progress);
        layoutPopupPrivateAuctionSelectPainting = (RelativeLayout)findViewById(R.id.private_auction_select_painting);
        layoutPopupBankAuctionBegin = (RelativeLayout)findViewById(R.id.start_bank_auction);

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
        mainGameView.populatePaintingsOtherPlayers(model.getPlayer(secondPlayerID));

        //load pictures for myPlayer
        mainGameView.populatePaintingsMyPlayer(myPlayer);

        //set listeners
        button_status_bar.setOnClickListener(this);
        //button_start_turn.setOnClickListener(this); //moved this into firebase listener
        button_secondPlayer.setOnClickListener(this);
        button_thirdPlayer.setOnClickListener(this);
        button_fourthPlayer.setOnClickListener(this);

        /*
        if(AppConstants.IamCreator){
            gameSetUp();
        }*/

        //start listening to changes in Firebase
        ListenForFirebaseGameEvents();

        //check turnTaker. this listener is called once then immediately removed
        Firebase turnTakerRef = new Firebase(AppConstants.GameRef+"/"+"TurnTaker");
        turnTakerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                System.out.println("first TurnTaker is: " + snapshot.getValue());

                //if the TurnTaker is me
                if (snapshot.getValue() == model.getMyPlayer().getPlayerpositionID()) {

                    //show popup
                    fullscreen_status_popup.setVisibility(View.VISIBLE);
                    statusPopupIsVisible = true;

                    //show the start turn popup layout (game model selection)
                    layoutStatusPopup.addView(layoutPopupGameModelSelection); //might have to use index of -1?

                    button_start_turn.setOnClickListener(MainGameActivity.this);
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

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

                    //show popup
                    fullscreen_status_popup.setVisibility(View.VISIBLE);
                    statusPopupIsVisible = true;

                    //show the start turn popup layout (game model selection)
                    layoutStatusPopup.addView(layoutPopupGameModelSelection); //might have to use index of -1?

                    button_start_turn.setOnClickListener(MainGameActivity.this);
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

                //if (snapshot.getValue() != null) { //probably don't need this

                    //if the turn is a private auction
                    if (snapshot.getValue() == "privateAuction") {

                        //my device will display the "private auction in progress" screen
                        layoutStatusPopup.addView(layoutPopupPrivateAuctionInProgress);
                    }

                    //if the turn is a bank auction
                    if (snapshot.getValue() == "bankAuction") {

                        //my device will display the "bank auction in progress" screen
                        layoutStatusPopup.addView(layoutPopupBankAuctionInProgress);
                    }
                //}
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
            selectedPlayer = model.getAllPlayers().get(secondPlayerID);
            button_secondPlayer.setBackgroundColor(Color.parseColor(AppConstants.MAINCOLOR));
            button_thirdPlayer.setBackgroundColor(Color.WHITE);
            button_fourthPlayer.setBackgroundColor(Color.WHITE);
            mainGameView.populatePaintingsOtherPlayers(selectedPlayer);
        } else if(v == button_thirdPlayer) {
            selectedPlayer = model.getAllPlayers().get(thirdPlayerID);
            button_secondPlayer.setBackgroundColor(Color.parseColor(AppConstants.MAINCOLOR));
            button_thirdPlayer.setBackgroundColor(Color.WHITE);
            button_fourthPlayer.setBackgroundColor(Color.WHITE);
            mainGameView.populatePaintingsOtherPlayers(selectedPlayer);
        } else if(v == button_fourthPlayer) {
            selectedPlayer = model.getAllPlayers().get(fourthPlayerID);
            button_secondPlayer.setBackgroundColor(Color.parseColor(AppConstants.MAINCOLOR));
            button_thirdPlayer.setBackgroundColor(Color.WHITE);
            button_fourthPlayer.setBackgroundColor(Color.WHITE);
            mainGameView.populatePaintingsOtherPlayers(selectedPlayer);
        } else if(v == button_begin_bank_auction) {

            //the below code could be moved to its own function

            //set turn action
            //model.setTurnAction("bankAuction");
            new Firebase(AppConstants.GameRef+"/"+"TurnAction").setValue("bankAuction");

            //choose painting being auctioned (first painting in Paintings[])
            Painting chosenPaintingToAuction = model.getAllPaintings1().get(0);

            //set painting being auctioned
            model.setPaintingBeingAuctioned(chosenPaintingToAuction);

            //set current bidder as the next person in allPlayers[]
            model.setCurrentBidder(model.getNextPlayer());
        }
    }

    /*public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_paintings_mainplayer, menu);
        return true;
    }*/

    /*
    //sets up the game, and is executed only by the game creator
    private void gameSetUp() {

        //access list of paintings and players
        List<Painting> thePaintings = model.getAllPaintings1();
        List<Player> thePlayers =  model.getAllPlayers();

        //randomize order of paintings
        Collections.shuffle(thePaintings);
        model.setAllPaintings(thePaintings);

        //randomize order of painting values
        List<Integer> thePaintingValues = model.getAllPaintingValues();
        Collections.shuffle(thePaintingValues);
        model.setAllPaintingValues(thePaintingValues);

        //for each player in the game
        for(int i=0; i<4; i++){

            //assign the first value in PaintingValues[] to the first painting in Paintings[]
            thePaintings.get(0).setValue(thePaintingValues.get(0));

            //add the first painting in Paintings[] to player’s painting list
            Player thisPlayer = thePlayers.get(i);
            thisPlayer.addPainting(thePaintings.get(0));

            //remove the first painting and value from their lists
            thePaintings.remove(0);
            thePaintingValues.remove(0);

        }

        //since 4 paintings and 4 values have been removed, we need to update the model
        model.setAllPaintings(thePaintings);
        model.setAllPaintingValues(thePaintingValues);
        //*** maybe we'll need a separate list for available paintings? (which would be different than all paintings)

        //randomly select a player and set them as TurnTaker
        Random rn = new Random();
        int index = rn.nextInt(3);
        Player selectedPlayer = thePlayers.get(index);
        model.setTurnTaker(selectedPlayer);

    }*/

    //executed whenever the turnTaker changes and is equal to my player
    public void startTurn() {

        //randomly select a turn type (roll the dice)
        Random rn = new Random();
        int roll = rn.nextInt(1); //there are only 2 types so far

        //hide the start turn layout
        layoutStatusPopup.removeView(layoutPopupGameModelSelection);

        if(roll == 0){

            //PRIVATE AUCTION

            //show the private auction select painting layout
            layoutStatusPopup.addView(layoutPopupPrivateAuctionSelectPainting);

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

            //show the bank auction select painting layout
            layoutStatusPopup.addView(layoutPopupBankAuctionBegin);

            //start listener on begin auction button
            button_begin_bank_auction.setOnClickListener(MainGameActivity.this);

        }

    }

    //executed when currentBidder = myPlayer
    public void bidOnPainting() {

        //temporarily hide the "private auction in progress" screen or "bank auction in progress" screen
        //TODO: hide the above screen

        //show the bidding screen
        //TODO: hide the above screen
    }

    public void Auction() {
        /*
        //full screen popup during auction
<<<<<<< Updated upstream
        private Button popup;
        private final PopupWindow popupWindow;
        private final LayoutInflater layoutInflater;
        private final RelativeLayout relativeLayout;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_auction);
            popup = (Button) findViewById(R.id.Button);
            relativeLayout = (relativeLayout) findViewById(R.id.relative);

            popup.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick (View view) {

                    layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                    ViewGroup container  = (ViewGroup) layoutInflater.inflate(R.layout.activity_bid,null);

                    popupWindow = new PopupWindow(container,400,400,true);
                    popupWindow.showAtLocation(relativeLayout, Gravity.NO_GRAVITY,500,500);

                    container.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent)
                        popupWindow.dismiss();
                        return true;
=======
       //private Button popup;
        //private final PopupWindow popupWindow;
        //private final LayoutInflater layoutInflater;
        //private final RelativeLayout relativeLayout;

        //@Override
        //protected void onCreate(Bundle savedInstanceState) {
            //super.onCreate(savedInstanceState);
            //setContentView(R.layout.activity_auction);
            //popup = (Button) findViewById(R.id.Button);
            //relativeLayout = (relativeLayout) findViewById(R.id.relative);

            //popup.setOnClickListener(new View.OnClickListener(){
                //@Override
                //public void onClick (View view) {

                    //layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                    //ViewGroup container  = (ViewGroup) layoutInflater.inflate(R.layout.activity_bid,null);

                    //popupWindow = new PopupWindow(container,400,400,true);
                    //popupWindow.showAtLocation(relativeLayout, Gravity.NO_GRAVITY,500,500);

                    //container.setOnTouchListener(new View.OnTouchListener() {
                    //@Override
                    //public boolean onTouch(View view, MotionEvent motionEvent)
                        //popupWindow.dismiss();
                        //return true;
>>>>>>> Stashed changes

                    });
            }
        }*/

    }

}
