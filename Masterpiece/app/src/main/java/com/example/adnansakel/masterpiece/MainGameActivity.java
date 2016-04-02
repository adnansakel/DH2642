package com.example.adnansakel.masterpiece;

import android.app.Activity;
import android.os.Bundle;

import com.example.adnansakel.masterpiece.model.MasterpieceGameModel;

/**
 * Created by Daniel on 02/04/2016.
 */
public class MainGameActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_allimages);

        // Adding the model
        MasterpieceGameModel model = ((MasterpieceApplication) this.getApplication()).getModel();
    }

}
