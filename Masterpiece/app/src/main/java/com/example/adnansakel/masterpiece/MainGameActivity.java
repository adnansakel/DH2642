package com.example.adnansakel.masterpiece;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.adnansakel.masterpiece.model.MasterpieceGameModel;
import com.example.adnansakel.masterpiece.model.Painting;

import java.util.Collections;
import java.util.List;

/**
 * Created by Daniel on 02/04/2016.
 */
public class MainGameActivity extends Activity {
    MasterpieceGameModel model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        // Adding the model
        model = ((MasterpieceApplication) this.getApplication()).getModel();

        /* DM TODO: OLD WAY OF CREATING IMAGES - Remove later
        LinearLayout test = (LinearLayout)findViewById(R.id.llPersonalImages);


        for (int i = 0; i < 4; i++) { // TODO: DM - Replace 4 with "getActivePlayer()" and the data of the player (e.g. image, title,...)
            LinearLayout tv = (LinearLayout)findViewById(R.id.llSinglePainting); // LinearLayout(getApplicationContext());
            //tv.setText(yourData.get(i));
            test.addView(tv);
        }
        */
    }

    /*public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_paintings_mainplayer, menu);
        return true;
    }*/

    //sets up the game, and is executed only by the game creator
    public void gameStart() {

        //randomize order of paintings
        List<Painting> thePaintings = model.getAllPaintings1();
        Collections.shuffle(thePaintings);
        model.setAllPaintings(thePaintings);

        //randomize order of painting values
        List<Integer> thePaintingValues = model.getAllPaintingValues();
        Collections.shuffle(thePaintingValues);
        model.setAllPaintingValues(thePaintingValues);

    }

}
