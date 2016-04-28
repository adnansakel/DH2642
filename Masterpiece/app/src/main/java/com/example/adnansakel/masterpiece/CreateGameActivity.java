package com.example.adnansakel.masterpiece;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adnansakel.masterpiece.model.AppConstants;
import com.example.adnansakel.masterpiece.model.MasterpieceGameModel;
import com.example.adnansakel.masterpiece.model.Painting;
import com.example.adnansakel.masterpiece.view.CreateGameView;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * Created by Adnan Sakel on 3/28/2016.
 */

public class CreateGameActivity extends Activity implements View.OnClickListener{

    // Variables for checking the internet connection status
    ConnectionCheck checkConnection;

    Firebase masterpieceRef;
    Button buttonJoinGame;
    ProgressDialog progress;
    TextView textViewGameNumber;
    EditText editTextUserName;

    MasterpieceGameModel masterpiecegamemodel;
    FirebaseCalls firebaseCalls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Default call to load previous state
        super.onCreate(savedInstanceState);

        // Set the view for the main activity screen
        // it must come before any call to findViewById method
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_creategame);

        // Create Internet Check Instance
        checkConnection = new ConnectionCheck(CreateGameActivity.this);

        masterpiecegamemodel = ((MasterpieceApplication)this.getApplication()).getModel();

        // Creating the view class instance
        CreateGameView createGameViewView = new CreateGameView(findViewById(R.id.creategame_view),masterpiecegamemodel);
        initializeComponent();

        // retrieve Internet status

        createGame();

    }

    private void initializeComponent(){
        buttonJoinGame = (Button)findViewById(R.id.buttonJoinGame);
        textViewGameNumber = (TextView)findViewById(R.id.textviewGameNumber);
        editTextUserName = (EditText)findViewById(R.id.edittext_userName);
        buttonJoinGame.setOnClickListener(this);
        firebaseCalls = new FirebaseCalls(CreateGameActivity.this, masterpiecegamemodel);
    }

    private void createGame(){
        if(checkConnection.isConnected()){
            firebaseCalls.createGame();
        }

    }

    @Override
    public void onClick(View view){

        if (view == buttonJoinGame) {
            if (checkConnection.isConnected()) {
                masterpiecegamemodel.setUserName(editTextUserName.getText().toString());
                AppConstants.TotalNumberofPlayers = Integer.valueOf(((TextView)findViewById(R.id.edittext_NumberofPlayers))
                        .getText().toString());
                if(masterpiecegamemodel.getUserName().length()>0){
                    masterpiecegamemodel.roundCounter = 0;
                    AppConstants.TotalNumberofRounds = Integer.valueOf(((EditText)findViewById(R.id.edittext_NumberofRounds)).getText().toString());
                    firebaseCalls.joinGamebyCreator();
                }
                else{
                    Toast.makeText(this,"Please insert an username to join the game.",Toast.LENGTH_LONG);
                }

            }
        }
    }
}
