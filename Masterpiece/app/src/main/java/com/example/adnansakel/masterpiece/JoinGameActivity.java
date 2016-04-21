package com.example.adnansakel.masterpiece;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.adnansakel.masterpiece.model.AppConstants;
import com.example.adnansakel.masterpiece.model.MasterpieceGameModel;
import com.example.adnansakel.masterpiece.view.HomeView;
import com.example.adnansakel.masterpiece.view.JoinGameView;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Adnan Sakel on 3/28/2016.
 */
public class JoinGameActivity extends Activity implements View.OnClickListener {
    // Variables for checking the internet connection status
    Boolean isConnected = false;
    ConnectionCheck checkConnection;

    // Variable definition
    Button button_join_game;
    ProgressDialog progress;
    EditText editTextUserName;
    EditText editTextGameNumber;
    FirebaseCalls firebaseCalls;
    MasterpieceGameModel masterpieceGameModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Default call to load previous state
        super.onCreate(savedInstanceState);

        // Set the view for the main activity screen
        // it must come before any call to findViewById method
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_joingame);

        // Create Internet Check Instance
        checkConnection = new ConnectionCheck(JoinGameActivity.this);

        masterpieceGameModel = masterpieceGameModel = ((MasterpieceApplication)this.getApplication()).getModel();
        // Creating the view class instance
        JoinGameView joinGameView = new JoinGameView(findViewById(R.id.joingame_view),masterpieceGameModel);

        //mDateEntryField.addTextChangedListener(mDateEntryWatcher);

        Firebase.setAndroidContext(this);

        initializeComponent();

    }

    private void initializeComponent(){
        button_join_game = (Button)findViewById(R.id.buttonJoinGame);
        button_join_game.setOnClickListener(this);

        editTextUserName = (EditText)findViewById(R.id.edittext_userName);
        editTextGameNumber = (EditText)findViewById(R.id.edittext_GameNumber);
        firebaseCalls = new FirebaseCalls(JoinGameActivity.this,masterpieceGameModel);
        //editTextGameNumber.addTextChangedListener();
    }

    /*private TextWatcher mDateEntryWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String working = s.toString();
            boolean isValid = true;
            if (working.length()==4) {
                if (Integer.parseInt(working) < 1 || Integer.parseInt(working)>12) {
                    isValid = false;
                } else {
                    working+="/";
                    mDateEntryWatcher.setText(working);
                    editTextGameNumber.setSelection(working.length());
                }
            }

            if (!isValid) {
                mDateEntryField.setError("Enter a valid date: MM/YYYY");
            } else {
                mDateEntryField.setError(null);
            }

        }
        }

    };*/

    @Override
    public void onClick(View v) {
        if (v == button_join_game) {
            if (checkConnection.isConnected()) {
                if(editTextGameNumber.getText().toString().length()>0 && editTextUserName.getText().toString().length() > 0){
                    masterpieceGameModel.setGameNumber(editTextGameNumber.getText().toString());
                    masterpieceGameModel.setUserName(editTextUserName.getText().toString());
                    firebaseCalls.joinGame();
                }
                else{

                    Toast.makeText(this,"You need to insert both game id and a username to join a game",Toast.LENGTH_LONG);
                }

            }
        }
    }
}
