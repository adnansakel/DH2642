package com.example.adnansakel.masterpiece;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by Daniel on 02/04/2016.
 */
public class MainGameActivity extends Activity implements View.OnClickListener {

    MasterpieceGameModel model;
    //Firebase masterpieceRef;
    Button button_status_bar;
    View fullscreen_status_popup;
    boolean turnIsHappening = false;
    boolean statusPopupIsVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        // Adding the model
        model = ((MasterpieceApplication) this.getApplication()).getModel();
        MainGameView mainGameView = new MainGameView(findViewById(R.id.maingame_overview_view),model);

        //find the status bar button, hide popup button, and fullscreen status popup
        button_status_bar = (Button)findViewById(R.id.buttonStatusBar);
        fullscreen_status_popup = findViewById(R.id.fullscreenStatusPopup);

        button_status_bar.setOnClickListener(this);

        /* DM TODO: OLD WAY OF CREATING IMAGES - Remove later
        LinearLayout test = (LinearLayout)findViewById(R.id.llPersonalImages);


        for (int i = 0; i < 4; i++) { // TODO: DM - Replace 4 with "getActivePlayer()" and the data of the player (e.g. image, title,...)
            LinearLayout tv = (LinearLayout)findViewById(R.id.llSinglePainting); // LinearLayout(getApplicationContext());
            //tv.setText(yourData.get(i));
            test.addView(tv);
        }
        */

        /*
        if(AppConstants.IamCreator){
            gameSetUp();
        }*/

        //listen for changes in TurnTaker in Firebase
        Firebase turnTakerRef = new Firebase(AppConstants.GameRef+"/"+"TurnTaker");
        turnTakerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                System.out.println("TurnTaker changed to: " + snapshot.getValue());

                //if the TurnTaker is me
                if (snapshot.getValue() == "playerPositionID") {
                    //TODO: use the right variable to compare to in the above (my id)
                    //startTurn();
                    //show the start turn screen
                    //TODO: show the above screen
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
                if (snapshot.getValue() == "") {
                    //TODO: use the right variable to compare to in the above
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

                //if a new turn action just started
                if (snapshot.getValue() != null && turnIsHappening == false) {

                    turnIsHappening = true;

                    //if the turn is a private auction
                    if (snapshot.getValue() == "privateAuction") {

                        //my device will display the "private auction in progress" screen
                        //TODO: display the above screen
                    }

                    //if the turn is a bank auction
                    if (snapshot.getValue() == "bankAuction") {

                        //my device will display the "bank auction in progress" screen
                        //TODO: display the above screen
                    }
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

        //get reference to Firebase
        //masterpieceRef = new Firebase(AppConstants.FireBaseUri);

        if(roll == 0){

            //PRIVATE AUCTION

            //set turn action
            //model.setTurnAction("privateAuction");
            new Firebase(AppConstants.GameRef+"/"+"TurnAction").setValue("privateAuction");

            //choose painting being auctioned (for now I'm just choosing my player's first painting)
            //Painting chosenPaintingToAuction = model.getMyPlayer().getOwnedPaintings().get(0);
            //TODO: need to access my owned paintings

            //TODO: allow my player to select a painting by swiping up

            //set painting being auctioned
            //model.setPaintingBeingAuctioned(chosenPaintingToAuction);
            new Firebase(AppConstants.GameRef+"/"+"PaintingBeingAuctioned").setValue("");
            //TODO: set the value of my painting in the above

            //TODO: set my player's bidding property to false in Firebase (eg. It's my auction so I'm done bidding)

            //set current bidder as the next player
            //model.setCurrentBidder(model.getNextPlayer());
            //TODO: get the next player
            new Firebase(AppConstants.GameRef+"/"+"CurrentBidder").setValue("");

            //bidding on private auction


        } else if(roll == 1){

            //BANK AUCTION

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

                    });
            }
        }*/

    }

}
