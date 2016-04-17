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


        downloadImages();

        /*Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 10, bytes);
        String encodeImage = Base64.encodeToString(bytes.toByteArray(), Base64.DEFAULT);*/
        /*
        Bitmap bmp =  BitmapFactory. decodeResource(this.getApplication().getResources(), R.drawable.a);//your image
        ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, bYtE);
        bmp.recycle();
        byte[] byteArray = bYtE.toByteArray();
        String imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);
        ref.setValue(imageFile);

        ref = new Firebase(AppConstants.FireBaseUri+"/"+AppConstants.PAINTINGS+"/"+"1"+"Image");
        bmp =  BitmapFactory.decodeResource(getResources(), R.drawable.b);//your image
        bYtE = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, bYtE);
        bmp.recycle();
        byteArray = bYtE.toByteArray();
        imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);
        ref.setValue(imageFile);

        ref = new Firebase(AppConstants.FireBaseUri+"/"+AppConstants.PAINTINGS+"/"+"2"+"Image");
        bmp =  BitmapFactory.decodeResource(getResources(), R.drawable.c);//your image
        bYtE = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, bYtE);
        bmp.recycle();
        byteArray = bYtE.toByteArray();
        imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);
        ref.setValue(imageFile);

        ref = new Firebase(AppConstants.FireBaseUri+"/"+AppConstants.PAINTINGS+"/"+"3"+"Image");
        bmp =  BitmapFactory.decodeResource(getResources(), R.drawable.d);//your image
        bYtE = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, bYtE);
        bmp.recycle();
        byteArray = bYtE.toByteArray();
        imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);
        ref.setValue(imageFile);

        ref = new Firebase(AppConstants.FireBaseUri+"/"+AppConstants.PAINTINGS+"/"+"4"+"Image");
        bmp =  BitmapFactory.decodeResource(getResources(), R.drawable.e);//your image
        bYtE = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, bYtE);
        bmp.recycle();
        byteArray = bYtE.toByteArray();
        imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);
        ref.setValue(imageFile);

        ref = new Firebase(AppConstants.FireBaseUri+"/"+AppConstants.PAINTINGS+"/"+"5"+"Image");
        bmp =  BitmapFactory.decodeResource(getResources(), R.drawable.f);//your image
        bYtE = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, bYtE);
        bmp.recycle();
        byteArray = bYtE.toByteArray();
        imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);
        ref.setValue(imageFile);

        ref = new Firebase(AppConstants.FireBaseUri+"/"+AppConstants.PAINTINGS+"/"+"6"+"Image");
        bmp =  BitmapFactory.decodeResource(getResources(), R.drawable.g);//your image
        bYtE = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, bYtE);
        bmp.recycle();
        byteArray = bYtE.toByteArray();
        imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);
        ref.setValue(imageFile);

        ref = new Firebase(AppConstants.FireBaseUri+"/"+AppConstants.PAINTINGS+"/"+"7"+"Image");
        bmp =  BitmapFactory.decodeResource(getResources(), R.drawable.h);//your image
        bYtE = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, bYtE);
        bmp.recycle();
        byteArray = bYtE.toByteArray();
        imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);
        ref.setValue(imageFile);

        ref = new Firebase(AppConstants.FireBaseUri+"/"+AppConstants.PAINTINGS+"/"+"8"+"Image");
        bmp =  BitmapFactory.decodeResource(getResources(), R.drawable.i);//your image
        bYtE = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, bYtE);
        bmp.recycle();
        byteArray = bYtE.toByteArray();
        imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);
        ref.setValue(imageFile);

        ref = new Firebase(AppConstants.FireBaseUri+"/"+AppConstants.PAINTINGS+"/"+"9"+"Image");
        bmp =  BitmapFactory.decodeResource(getResources(), R.drawable.j);//your image
        bYtE = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, bYtE);
        bmp.recycle();
        byteArray = bYtE.toByteArray();
        imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);
        ref.setValue(imageFile);
*/

       // ref.updateChildren(mp);



    }

    ProgressDialog progress;

    private void downloadImages(){

        Firebase.setAndroidContext(this);
        progress = ProgressDialog.show(this, "", "Downloading masterpiece paintings ...", true);
        new Firebase(AppConstants.FireBaseUri+"/"+AppConstants.PAINTINGS).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            Painting painting = new Painting();
                            System.out.println(ds.getValue().toString());
                            System.out.println(ds.child("Artist").getValue().toString());
                            painting.setArtist(ds.child(AppConstants.ARTIST).getValue().toString());
                            painting.setImageURL(ds.child(AppConstants.IMAGE).getValue().toString());
                            painting.setName(ds.child(AppConstants.NAME).getValue().toString());
                            painting.setDescription(ds.child(AppConstants.DESCRIPTION).getValue().toString());
                            masterpieceGameModel.addPaintingtoAllPaintings(painting);
                            progress.dismiss();

                        }

                        ImageDownloader imageDownloader = new ImageDownloader(HomeActivity.this,masterpieceGameModel);
                        imageDownloader.downloadImages(progress);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        progress.dismiss();
                        Toast.makeText(HomeActivity.this, firebaseError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });
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
                startActivity(new Intent(HomeActivity.this, JoinGameActivity.class));
            }
        }
    }
}
