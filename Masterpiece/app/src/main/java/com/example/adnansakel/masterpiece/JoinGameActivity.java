package com.example.adnansakel.masterpiece;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.adnansakel.masterpiece.model.AppConstants;
import com.example.adnansakel.masterpiece.view.HomeView;
import com.example.adnansakel.masterpiece.view.JoinGameView;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by Adnan Sakel on 3/28/2016.
 */
public class JoinGameActivity extends Activity implements View.OnClickListener {

    // Variable definition
    Button button_join_game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Default call to load previous state
        super.onCreate(savedInstanceState);

        // Set the view for the main activity screen
        // it must come before any call to findViewById method
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_joingame);

        // Creating the view class instance
        JoinGameView joinGameView = new JoinGameView(findViewById(R.id.joingame_view));

        button_join_game = (Button)findViewById(R.id.buttonJoinGame);
        button_join_game.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == button_join_game) {
            //go to lobby activity
            // TODO: add the conditions that it only moves to LobbyActivity if the Game ID is existing and the player is successfully added to Firebase
            startActivity(new Intent(JoinGameActivity.this, LobbyActivity.class));
        }
    }
}
