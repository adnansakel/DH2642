package com.example.adnansakel.masterpiece;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.adnansakel.masterpiece.model.AppConstants;
import com.example.adnansakel.masterpiece.model.MasterpieceGameModel;
import com.example.adnansakel.masterpiece.model.Painting;
import com.example.adnansakel.masterpiece.model.Player;
import com.example.adnansakel.masterpiece.view.CreateGameView;
import com.example.adnansakel.masterpiece.view.HomeView;
import com.example.adnansakel.masterpiece.view.LobbyView;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 02/04/2016.
 */

// TODO: We have to distinguish the LobbyActivity for participants and the game creator, as only the game creator should be able to start/create the game in the lobby?
// TODO: If it is complex, everybody can start it as long as it is 4 people.
/*
*I think lobby activity should finish itself as soon as there are four players are available and should proceed to MainGameActivity
* immediately ---- Commented by Sakel
 *  */



public class LobbyActivity extends Activity implements View.OnClickListener {
    // Variables for checking the internet connection status
    Boolean isConnected = false;
    ConnectionCheck checkConnection;
    ProgressDialog progress;
    // Variable definition
    Button button_create_game;
    MasterpieceGameModel masterpiecegamemodel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Default call to load previous state
        super.onCreate(savedInstanceState);

        // Set the view for the main activity screen
        // it must come before any call to findViewById method
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_lobby);

        // Create Internet Check Instance
        checkConnection = new ConnectionCheck(LobbyActivity.this);

        masterpiecegamemodel = ((MasterpieceApplication)this.getApplication()).getModel();
        // Creating the view class instance
        LobbyView lobbyView = new LobbyView(findViewById(R.id.lobby_view),masterpiecegamemodel);

        button_create_game = (Button)findViewById(R.id.buttonStartGame);
        button_create_game.setOnClickListener(this);

        Firebase.setAndroidContext(this);

        if (checkConnection.isConnected()) {
            listentoFirebaseforPlayers();

        }
    }




    private void distributeShuffledPaintingandValues(){
        progress = ProgressDialog.show(this, "", "distributing paintings ...", true);
        //downloading the shuffled painting ids and values then assign a painting and value to my player and rest to bamk
        new Firebase(AppConstants.GameRef).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        GenericTypeIndicator<List<Integer>> t = new GenericTypeIndicator<List<Integer>>() {};
                        List<Integer> shuffledpaintinglist = new ArrayList<Integer>();
                        List<Integer> shuffledpaintinvalueglist = new ArrayList<Integer>();
                        shuffledpaintinglist = dataSnapshot.child(AppConstants.SHUFFLEDPAINTINGS).getValue(t);
                        masterpiecegamemodel.setShuffledPaintingIDs(shuffledpaintinglist);
                        shuffledpaintinvalueglist = dataSnapshot.child(AppConstants.SHUFFLEDPAINTINGVALUES).getValue(t);
                        masterpiecegamemodel.setShuffledPaintingValues(shuffledpaintinvalueglist);

                        for(int i = 0; i < 4; i++){//masterpiecegamemodel.getAllPlayers().size() should be used instead of 4
                            masterpiecegamemodel.getAllPlayers().get(i).addOwnedPaintingID(shuffledpaintinglist.get(i));
                            masterpiecegamemodel.getAllPlayers().get(i).addOenedPaintingValue(shuffledpaintinvalueglist.get(i));
                        }
                        progress.dismiss();
                        startActivity(new Intent(LobbyActivity.this, MainActivity.class));
                        LobbyActivity.this.finish();

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        progress.dismiss();
                        Toast.makeText(LobbyActivity.this, firebaseError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    private void listentoFirebaseforPlayers(){

        new Firebase(AppConstants.GameRef+"/"+"Players").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                masterpiecegamemodel.removeAllPlayer();
                int i = 0;
                for(DataSnapshot dsplayer : dataSnapshot.getChildren()){
                    //DataSnapshot dpl = (DataSnapshot)dsplayer.getValue();
                    //System.out.println(dsplayer.child("Name"));
                    Player player = new Player();
                    player.setName(dsplayer.child("Name").getValue().toString());
                    player.setPlayerpositionID(String.valueOf(i));
                    player.setFirebaseid(dsplayer.getKey().toString());
                    masterpiecegamemodel.addPlayer(player);
                    if(player.getName().equals(masterpiecegamemodel.getUserName())){
                        masterpiecegamemodel.setMyPlayer(player);
                    }
                   // masterpiecegamemodel.notifyObservers();
                    i++;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(LobbyActivity.this,firebaseError.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == button_create_game) {
            //go to lobby activity
            // TODO: add the conditions that it only moves to MainGameActivity if the Game is successfully setup in Firebase
            if (checkConnection.isConnected()) {
                distributeShuffledPaintingandValues();
                //downloadImage();
            }

        }
    }
}

