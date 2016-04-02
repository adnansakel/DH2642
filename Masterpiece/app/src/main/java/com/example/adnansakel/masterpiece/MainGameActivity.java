package com.example.adnansakel.masterpiece;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

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

        LinearLayout test = (LinearLayout)findViewById(R.id.horizontalscroll_bottomPanel_linearlayout);

        for (int i = 0; i < 4; i++) { // TODO: replace 4 with "getActivePlayer()" and the data of the player (e.g. image, title,...)
            LinearLayout tv = (LinearLayout)findViewById(R.id.painting_single); // LinearLayout(getApplicationContext());
            //tv.setText(yourData.get(i));
            test.addView(tv);
        }
    }

    /*public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_paintings_mainplayer, menu);
        return true;
    }*/


}
