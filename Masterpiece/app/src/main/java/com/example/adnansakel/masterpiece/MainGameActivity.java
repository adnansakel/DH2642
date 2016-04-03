package com.example.adnansakel.masterpiece;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.adnansakel.masterpiece.model.MasterpieceGameModel;
import com.example.adnansakel.masterpiece.model.Painting;
import com.example.adnansakel.masterpiece.model.Player;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

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

        //access list of paintings and players
        List<Painting> thePaintings = model.getAllPaintings1();
        List<Player> thePlayers =  model.getAllPlayers();

        //randomize order of paintings
        Collections.shuffle(thePaintings);
        model.setAllPaintings(thePaintings);

        //randomize order of painting values
        List<Integer> thePaintingValues = model.getAllPaintingValues();
        Collections.shuffle(thePaintingValues);
        model.setAllPaintingValues(thePaintingValues);

        //for each player in the game
        for(int i=0; i<4; i++){

            //assign the first value in PaintingValues[] to the first painting in Paintings[]
            thePaintings.get(0).setValue(thePaintingValues.get(0));

            //add the first painting in Paintings[] to player’s painting list
            Player thisPlayer = thePlayers.get(i);
            thisPlayer.addPainting(thePaintings.get(0));

            //remove the first painting and value from their lists
            thePaintings.remove(0);
            thePaintingValues.remove(0);

        }

        //since 4 paintings and 4 values have been removed, we need to update the model
        model.setAllPaintings(thePaintings);
        model.setAllPaintingValues(thePaintingValues);
        //*** maybe we'll need a separate list for available paintings? (which would be different than all paintings)

        //randomly select a player and set them as TurnTaker
        Random rn = new Random();
        int index = rn.nextInt(3);
        Player selectedPlayer = thePlayers.get(index);
        model.setTurnTaker(selectedPlayer);

    }

    //executed whenever the turnTaker changes and is equal to my player
    public void startTurn() {

        //randomly select a turn type (roll the dice)
        Random rn = new Random();
        int roll = rn.nextInt(1); //there are only 2 types so far

        if(roll == 0){

            //PRIVATE AUCTION

            //set turn action
            model.setTurnAction("privateAuction");

            //choose painting being auctioned (for now I'm just choosing my player's first painting)
            Painting chosenPaintingToAuction = model.getMyPlayer().getOwnedPaintings().get(0);
            //TODO: allow my player to select a painting, or allow another player to

            //set painting being auctioned
            model.setPaintingBeingAuctioned(chosenPaintingToAuction);

            //set current bidder as the next person in allPlayers[]
            model.setCurrentBidder(model.getNextPlayer());

        } else if(roll == 1){

            //BANK AUCTION

            //set turn action
            model.setTurnAction("bankAuction");

            //choose painting being auctioned (first painting in Paintings[])
            Painting chosenPaintingToAuction = model.getAllPaintings1().get(0);

            //set painting being auctioned
            model.setPaintingBeingAuctioned(chosenPaintingToAuction);

            //set current bidder as the next person in allPlayers[]
            model.setCurrentBidder(model.getNextPlayer());
        }

    }

    //executed when currentBidder = myPlayer
    public void Auction() {

    }

}
