package com.example.adnansakel.masterpiece;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.adnansakel.masterpiece.model.AppConstants;
import com.example.adnansakel.masterpiece.model.MasterpieceGameModel;
import com.example.adnansakel.masterpiece.model.Painting;
import com.example.adnansakel.masterpiece.view.HomeView;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import android.os.Handler;

/**
 * Created by Adnan Sakel on 3/28/2016.
 */
public class HomeActivity extends Activity implements View.OnClickListener {

    // Variables for checking the internet connection status
    Boolean isConnected = false;
    ConnectionCheck checkConnection;

    Button button_create_game;
    Button button_join_game;
    Firebase masterpieceGameNumberRef;
    MasterpieceGameModel masterpieceGameModel;
    FirebaseCalls firebaseCalls;
    Handler handler;
    boolean mStopHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Default call to load previous state
        super.onCreate(savedInstanceState);

        // Set the view for the main activity screen
        // it must come before any call to findViewById method
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_home);

        // Create Internet Check Instance
        checkConnection = new ConnectionCheck(HomeActivity.this);

        masterpieceGameModel = ((MasterpieceApplication)this.getApplication()).getModel();

        // Creating the view class instance
        HomeView homeView = new HomeView(findViewById(R.id.home_view),masterpieceGameModel);

        button_create_game = (Button)findViewById(R.id.buttonCreateGame);
        button_join_game = (Button)findViewById(R.id.buttonJoinGame);

        button_create_game.setOnClickListener(this);
        button_join_game.setOnClickListener(this);

        Firebase.setAndroidContext(this);

        Firebase ref = new Firebase(AppConstants.FireBaseUri+"/"+AppConstants.PAINTINGS+"/"+"0"+"Image");
        Map<String,Object> mp = new HashMap<String,Object>();

        handler = new Handler();
        mStopHandler = false;
        firebaseCalls = new FirebaseCalls(this, masterpieceGameModel);
        checkConnection.isConnected();
        masterpieceGameModel.removeAllPlayer();
        masterpieceGameModel.getAllPaintings().clear();
        downloadImages();




    }

   // ProgressDialog progress;

    private void downloadImages(){

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // do your stuff - don't create a new runnable here!
                if(checkConnection.isConnectedWithoutToastMessage()){
                    if(masterpieceGameModel.getAllPaintings().size()==0)firebaseCalls.downloadMasterpiecePaintings();
                    handler.removeCallbacks(this);
                    mStopHandler = true;

                }
                if (!mStopHandler) {
                    //System.out.println("Hi...");
                    handler.postDelayed(this, 500);
                }
            }
        };

// start it with:
        handler.post(runnable);



    }

    @Override
    public void onClick(View v) {
        if(v == button_create_game){
            // check if connected to the internet = true
            if (checkConnection.isConnected()) {
                // if it is connected, then go to create game activity
                masterpieceGameModel.removeAllPlayer();
                masterpieceGameModel.getAllPaintings();
                masterpieceGameModel.shufflePaintingIDsandValues();
                startActivity(new Intent(HomeActivity.this, CreateGameActivity.class));
            }
        }
        else if(v == button_join_game){
            // check if connected to the internet = true
            if (checkConnection.isConnected()) {
                // if it is connected, then go to join game activity
                masterpieceGameModel.getAllPlayers().clear();
                startActivity(new Intent(HomeActivity.this, JoinGameActivity.class));
            }
        }
    }
}
