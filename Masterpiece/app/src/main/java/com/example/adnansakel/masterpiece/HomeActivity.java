package com.example.adnansakel.masterpiece;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.adnansakel.masterpiece.model.AppConstants;
import com.example.adnansakel.masterpiece.view.HomeView;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Adnan Sakel on 3/28/2016.
 */
public class HomeActivity extends Activity implements View.OnClickListener {

    Button button_create_game;
    Button button_join_game;
   // Firebase masterpieceGameNumberRef;
    Firebase masterpieceRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Default call to load previous state
        super.onCreate(savedInstanceState);

        // Set the view for the main activity screen
        // it must come before any call to findViewById method
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_home);


        // Creating the view class instance
        HomeView homeView = new HomeView(findViewById(R.id.home_view));

        button_create_game = (Button)findViewById(R.id.buttonCreateGame);
        button_join_game = (Button)findViewById(R.id.buttonJoinGame);

        button_create_game.setOnClickListener(this);
        button_join_game.setOnClickListener(this);

        /*
        //masterpieceGameNumberRef = new Firebase(AppConstants.FireBaseUri+"/"+AppConstants.GameNumber);
        try{
            Firebase.setAndroidContext(this);
            masterpieceRef = new Firebase(AppConstants.FireBaseUri);
            Map<String,Object> newGameNumber = new HashMap<String,Object>();
            newGameNumber.put("test2", "123");
            //masterpieceRef.setValue(newGameNumber);
            masterpieceRef.child("GameNumber").setValue(newGameNumber);
            //masterpieceRef.push().setValue(newGameNumber);
        }
        catch (Exception ex){
            System.out.println(ex.getMessage().toString());
           // button_join_game.setText(ex.getMessage().toString());
        }*/


    }

    @Override
    public void onClick(View v) {
        if(v == button_create_game){
            //go to create game activity
            startActivity(new Intent(HomeActivity.this, CreateGameActivity.class));
            /*
            masterpieceRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String game_number = dataSnapshot.child(AppConstants.GameNumber).getValue().toString();

                    if(!dataSnapshot.child(game_number).exists()){

                        Map<String,Object> newGameNumber = new HashMap<String,Object>();
                        newGameNumber.put(AppConstants.GameNumber, String.valueOf(Integer.valueOf(game_number) + 1));
                        masterpieceRef.updateChildren(newGameNumber);
                        newGameNumber.put("Game_1114",null);

                        masterpieceRef.setValue(newGameNumber);

                    }

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });*/

        }
        else if(v == button_join_game){
            //go to join game activity
            startActivity(new Intent(HomeActivity.this, JoinGameActivity.class));
        }
    }
}
