package com.example.adnansakel.masterpiece;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
public class MainGameActivity extends Activity implements View.OnClickListener, ShakeDetector.OnShakeListener {

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
    Button button_privateauction_bid;
    Button button_bankauction_bid;
    Button button_privateauction_not_bidding;
    Button button_bankauction_not_bidding;

    FirebaseCalls firebaseCalls;

    SensorManager mSensorManager;
    ShakeDetector mShakeDetector;
    View fullscreen_status_popup;

    boolean myTurn = false;
    boolean statusPopupIsVisible = false;
    Sensor mAccelerometer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        //adding the model
        model = ((MasterpieceApplication) this.getApplication()).getModel();

        // launch firebase calls
        firebaseCalls = new FirebaseCalls(this,model);
        firebaseCalls.distributeShuffledPaintingandValues();
        firebaseCalls.listentoFireBaseForGameLogic();

        myPlayer = model.getMyPlayer();
        mainGameView = new MainGameView(findViewById(R.id.maingame_overview_view),model);

        //find views of buttons
        button_status_bar = (Button)findViewById(R.id.buttonStatusBar);
        button_start_turn = (Button)findViewById(R.id.btnRoll); // Changed from OLDBUTTONROLL
        button_begin_bank_auction = (Button)findViewById(R.id.btn_begin_bank_auction);
        button_privateauction_bid = (Button)findViewById(R.id.btn_privateauction_bid);
        button_bankauction_bid = (Button)findViewById(R.id.btn_bankauction_bid);
        button_privateauction_not_bidding = (Button)findViewById(R.id.btn_privateauction_not_bidding);
        button_bankauction_not_bidding = (Button)findViewById(R.id.btn_bankauction_not_bidding);

        //setting


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
        mainGameView.populatePaintingsOtherPlayers(secondPlayerID);//should not call like this

        //load pictures for myPlayer
        mainGameView.populatePaintingsMyPlayer(myPlayerID, (LinearLayout) findViewById(R.id.llPaintingsOfMyPlayer));

        //set click listeners
        button_status_bar.setOnClickListener(this);
        button_start_turn.setOnClickListener(this);
        button_secondPlayer.setOnClickListener(this);
        button_thirdPlayer.setOnClickListener(this);
        button_fourthPlayer.setOnClickListener(this);
        button_privateauction_bid.setOnClickListener(this);
        button_bankauction_bid.setOnClickListener(this);
        button_privateauction_not_bidding.setOnClickListener(this);
        button_bankauction_not_bidding.setOnClickListener(this);
        button_begin_bank_auction.setOnClickListener(MainGameActivity.this);


        mSensorManager = (SensorManager) getSystemService(getApplicationContext().SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(this);


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
                fullscreen_status_popup.setVisibility(View.INVISIBLE);

                //toggle the button to show popup next time it's pressed
                statusPopupIsVisible = false;
            }
        }
        else if(v == button_start_turn) {

            //randomly select a turn type (roll the dice)
            Random rn = new Random();
            int roll = rn.nextInt(1); //there are only 2 types so far

            if(roll == 0) {
                //PRIVATE AUCTION
                startPrivateAuction();
            }
            else if(roll == 1) {
                //BANK AUCTION
                model.setPopupContent("bankAuctionBegin");
            }

        } else if(v == button_secondPlayer) {
            model.setCurrentPlayerToDisplay(secondPlayerID);
            //TODO move code to change view to GameView and do not use deprecated method like getColor
            button_secondPlayer.setBackgroundColor(Color.parseColor(AppConstants.MAINCOLOR));
            button_thirdPlayer.setBackgroundColor(getResources().getColor(R.color.colorButtonInactive));
            button_fourthPlayer.setBackgroundColor(getResources().getColor(R.color.colorButtonInactive));
        } else if(v == button_thirdPlayer) {
            model.setCurrentPlayerToDisplay(thirdPlayerID);
            //TODO Change setting color from view
            button_secondPlayer.setBackgroundColor(getResources().getColor(R.color.colorButtonInactive));
            button_thirdPlayer.setBackgroundColor(Color.parseColor(AppConstants.MAINCOLOR));
            button_fourthPlayer.setBackgroundColor(getResources().getColor(R.color.colorButtonInactive));
        } else if(v == button_fourthPlayer) {
            model.setCurrentPlayerToDisplay(fourthPlayerID);
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
            model.setPaintingBeingAuctioned(String.valueOf(chosenPaintingToAuction));

            //set current bidder as the next person in allPlayers[]
            model.setCurrentBidder(model.getCurrentBidder());

        } else if(v == button_privateauction_bid) {

            //increase current bid in Firebase
            int currentBid = Integer.parseInt(model.getCurrentBid());
            currentBid += 50000;
            new Firebase(AppConstants.GameRef+"/"+"CurrentBid").setValue(Integer.toString(currentBid));

            //set current bidder as the next player
            new Firebase(AppConstants.GameRef+"/"+"CurrentBidder").setValue((myPlayerID + 1)%4);

            //change to private auction in progress screen
            model.setPopupContent("privateAuctionInProgress");

        } else if(v == button_privateauction_not_bidding) {

            //set current bidder as the next player
            new Firebase(AppConstants.GameRef+"/"+"CurrentBidder").setValue((myPlayerID + 1) % 4);

            //change to private auction in progress screen
            model.setPopupContent("privateAuctionInProgress");

        } else if(v == button_bankauction_bid) {

            //increase current bid in Firebase
            int currentBid = Integer.parseInt(model.getCurrentBid());
            currentBid += 50000;
            new Firebase(AppConstants.GameRef+"/"+"CurrentBid").setValue(Integer.toString(currentBid));

            //set current bidder as the next player
            new Firebase(AppConstants.GameRef+"/"+"CurrentBidder").setValue((myPlayerID + 1)%4);

            //change to bank auction in progress screen
            model.setPopupContent("bankAuctionInProgress");

        } else if(v == button_bankauction_not_bidding) {

            //set current bidder as the next player
            new Firebase(AppConstants.GameRef+"/"+"CurrentBidder").setValue((myPlayerID + 1)%4);

            //change to bank auction in progress screen
            model.setPopupContent("bankAuctionInProgress");

        }
    }

    //executed whenever the turnTaker changes and is equal to my player
    public void startPrivateAuction () {

            //PRIVATE AUCTION

            //show the private auction select painting layout
            model.setPopupContent("privateAuctionSelectPainting");

            // load clickable pictures for myPlayer
            mainGameView.populatePaintingsMyPlayerPrivateAuction(myPlayerID,(LinearLayout)findViewById(R.id.ll_PrivateAuction_PaintingsToSelect));

            // player can
            //TODO: allow my player to select a painting

            //set painting being auctioned
            //model.setPaintingBeingAuctioned(chosenPaintingToAuction);

           /* TODO DM SET BACK
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


       // } else if(roll == 1){

            //BANK AUCTION

            model.setPopupContent("bankAuctionBegin");

            //start listener on begin auction button
            button_begin_bank_auction.setOnClickListener(MainGameActivity.this);

        //}

        */
    }

    public void PaintingSelected(View v) {
        System.out.println("ID of Selected Painting: " + v.getId());

        //TODO: move to firebase class

        //TODO: set the value of my painting in PaintingBeingAuctioned
        new Firebase(AppConstants.GameRef+"/"+"PaintingBeingAuctioned").setValue(v.getId());

        //set my player's bidding property to false in Firebase (eg. It's my auction so I'm done bidding)
        new Firebase(AppConstants.GameRef+"/"+AppConstants.PLAYERS+"/"+model.getPlayer(myPlayerID).getFirebaseid()+"/"+AppConstants.BIDDING).setValue("false");

        //set TurnAction to privateAuction so that everybody receives the update of screens
        new Firebase(AppConstants.GameRef+"/"+"TurnAction").setValue("privateAuction");

        //set the current bidder to the playerID of the next player
        new Firebase(AppConstants.GameRef+"/"+"CurrentBidder").setValue((myPlayerID + 1)%4);

        //bidding on private auction

    }

    @Override
    public void onShake(int count) {
        Toast.makeText(this,"Shake detected"+count,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }
}
