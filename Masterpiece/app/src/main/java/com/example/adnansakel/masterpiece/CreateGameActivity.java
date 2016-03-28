package com.example.adnansakel.masterpiece;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Adnan Sakel on 3/28/2016.
 */
public class CreateGameActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Default call to load previous state
        super.onCreate(savedInstanceState);

        // Set the view for the main activity screen
        // it must come before any call to findViewById method
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_creategame);
    }
}
