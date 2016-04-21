package com.example.adnansakel.masterpiece;

import android.app.Activity;
import android.os.Bundle;

import com.example.adnansakel.masterpiece.model.AppConstants;
import com.example.adnansakel.masterpiece.model.MasterpieceGameModel;
import com.example.adnansakel.masterpiece.model.Player;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by GC on 4/21/16.
 */
public class FirebaseActivity extends Activity {

    MasterpieceGameModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Default call to load previous state
        super.onCreate(savedInstanceState);

        //listen to any changes to this game in Firebase
        Firebase firebaseRef = new Firebase(AppConstants.GameRef);
        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("Some data changed in Firebase: " + snapshot.getValue());

                //integers
                model.setCountNonBidders((Integer) snapshot.child("CountNonBidders").getValue());
                model.setCountPlayers((Integer) snapshot.child("CountPlayers").getValue()); //maybe we don't need this
                model.setCurrentBid((Integer) snapshot.child("CurrentBid").getValue());
                model.setCurrentBidder((Integer) snapshot.child("CurrentBidder").getValue());
                //I skipped GameNumber, I don't think we need to set that from here
                model.setPaintingBeingAuctioned((Integer) snapshot.child("PaintingBeingAuctioned").getValue());
                model.setTurnTaker((Integer) snapshot.child("TurnTaker").getValue());

                //strings
                //I skipped GameState, maybe we won't need it?
                model.setTurnAction((String) snapshot.child("TurnAction").getValue());

                //reset playerNumber after each onDataChange
                Integer playerNumber = 0;

                //loop through each player
                for (DataSnapshot playersSnapshot: snapshot.child("Players").getChildren()) {
                    model.getPlayer(playerNumber).setBidAmount((Integer) snapshot.child("BidAmount").getValue());
                    model.getPlayer(playerNumber).setBidding((Boolean) snapshot.child("Bidding").getValue());
                    model.getPlayer(playerNumber).setCash((Integer) snapshot.child("Cash").getValue());
                    model.getPlayer(playerNumber).setName((String) snapshot.child("Name").getValue());
                    model.getPlayer(playerNumber).setOwnedPaintings((List<Integer>) snapshot.child("Paintings").getValue());
                    playerNumber++;
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("Firebase read failed: " + firebaseError.getMessage());
            }
        });

    }

}
