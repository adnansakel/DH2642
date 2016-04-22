package com.example.adnansakel.masterpiece.view;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.adnansakel.masterpiece.R;
import com.example.adnansakel.masterpiece.model.AppConstants;
import com.example.adnansakel.masterpiece.model.MasterpieceGameModel;
import com.example.adnansakel.masterpiece.model.Painting;
import com.example.adnansakel.masterpiece.model.Player;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Daniel on 02/04/2016.
 */
public class MainGameView implements Observer{
    View view;
    MasterpieceGameModel model;
    //Map<String, View> PaintingNameToViewMap = new HashMap<>();
    LinearLayout.LayoutParams layoutParams;
    LayoutInflater layoutInflater;

    RelativeLayout layoutStatusPopup;
    RelativeLayout layoutPopupGameModelSelection;
    RelativeLayout layoutPopupPrivateAuctionInProgress;
    RelativeLayout layoutPopupBankAuctionInProgress;
    RelativeLayout layoutPopupPrivateAuctionSelectPainting;
    RelativeLayout layoutPopupBankAuctionBegin;
    RelativeLayout layoutPopupPrivateAuctionBid;
    RelativeLayout layoutPopupBankAuctionBid;
    RelativeLayout layoutPopupPrivateAuctionWon;
    RelativeLayout layoutPopupBankAuctionWon;
    RelativeLayout layoutPopupPrivateAuctionLost;
    RelativeLayout layoutPopupBankAuctionLost;
    LinearLayout layoutHomeViewInMainGameView;


    public MainGameView(View view, MasterpieceGameModel model) {
        this.view = view;
        initialize();
        model.addObserver(this);
        this.model = model;


        layoutInflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //set cash
        TextView textCash = (TextView) view.findViewById(R.id.txtPlayerCash);
        textCash.setText("Cash: " + model.getMyPlayer().getCash() + " $");

        //set currently selected player text
        TextView textPlayerTitle = (TextView) view.findViewById(R.id.txtPlayerTitle);
        textPlayerTitle.setText(model.getPlayer(1).getName() + "'s paintings:");


    }

    private void initialize(){

        //find the views that will be needed
        layoutStatusPopup = (RelativeLayout)view.findViewById(R.id.fullscreenStatusPopup);
        layoutPopupGameModelSelection = (RelativeLayout)view.findViewById(R.id.game_mode_selection_view);
        layoutPopupPrivateAuctionInProgress = (RelativeLayout)view.findViewById(R.id.privateauction_inprogress_view);
        layoutPopupBankAuctionInProgress = (RelativeLayout)view.findViewById(R.id.bankauction_inprogress_view);
        layoutPopupPrivateAuctionSelectPainting = (RelativeLayout)view.findViewById(R.id.privateauction_select_painting_view);
        layoutPopupBankAuctionBegin = (RelativeLayout)view.findViewById(R.id.start_bankauction_view);
        layoutPopupPrivateAuctionBid = (RelativeLayout)view.findViewById(R.id.privateauction_bid_view);
        layoutPopupBankAuctionBid = (RelativeLayout)view.findViewById(R.id.bankauction_bid_view);
        layoutPopupPrivateAuctionWon = (RelativeLayout)view.findViewById(R.id.privateauction_bidwon_view);
        layoutPopupBankAuctionWon = (RelativeLayout)view.findViewById(R.id.bankauction_bidwon_view);
        layoutPopupPrivateAuctionLost = (RelativeLayout)view.findViewById(R.id.privateauction_bidlost_view);
        layoutPopupBankAuctionLost = (RelativeLayout)view.findViewById(R.id.bankauction_bidlost_view);
        layoutHomeViewInMainGameView = (LinearLayout)view.findViewById(R.id.linear_layout_homeview_in_MainGameView);

        //set the popup content views to invisible
        hideAllPopupContent();
        //layoutHomeViewInMainGameView.setVisibility(View.VISIBLE);
        //layoutStatusPopup.setVisibility(View.VISIBLE);
        //layoutPopupGameModelSelection.setVisibility(View.VISIBLE);
    }

    public void populatePaintingsOtherPlayers(Integer selectedPlayerID){
        LinearLayout layoutPaintingsOtherPlayers = (LinearLayout)view.findViewById(R.id.llPaintingsOfOtherPlayers);
        HorizontalScrollView hsvTopPanel = (HorizontalScrollView)view.findViewById(R.id.hsvTopPanel);
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(2, 0, 2, 0);

        layoutPaintingsOtherPlayers.removeAllViews();

        for (int paintingID : model.getPlayer(selectedPlayerID).getOwnedPaintingIDs()) {
            View singlePainting = layoutInflater.inflate(R.layout.item_image, null);
            ImageView image = (ImageView) singlePainting.findViewById(R.id.imgPainting);
            image.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(
                    model.getPaintingbyPosition(paintingID).getImagebytearray(), 0,
                    model.getPaintingbyPosition(paintingID).getImagebytearray().length), 200, 200, true));

            layoutPaintingsOtherPlayers.addView(singlePainting, layoutParams);
        }
    }

    public void populatePaintingsMyPlayer(Integer myPlayerID, LinearLayout ll){
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(2, 0, 2, 0);

        ll.removeAllViews();

        int counter = 0;
        for(int paintingID: model.getPlayer(myPlayerID).getOwnedPaintingIDs()) {
            View singlePainting = layoutInflater.inflate(R.layout.item_image_value, null);
            ImageView image = (ImageView) singlePainting.findViewById(R.id.imgPainting);
            image.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(
                    model.getPaintingbyPosition(paintingID).getImagebytearray(), 0,
                    model.getPaintingbyPosition(paintingID).getImagebytearray().length), 200, 200, true));

            TextView textSecretValue = (TextView) singlePainting.findViewById(R.id.txtSecretValue);
            textSecretValue.setText(String.valueOf(model.getPlayer(myPlayerID).getOwnedPaintingValues().get(counter) + " $"));
            textSecretValue.setWidth(200);

            ll.addView(singlePainting, layoutParams);
            counter++;
        }
    }

    public void populatePaintingsMyPlayerPrivateAuction(Integer myPlayerID, LinearLayout ll){
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(2, 0, 2, 0);

        ll.removeAllViews();

        int counter = 0;
        for(int paintingID: model.getPlayer(myPlayerID).getOwnedPaintingIDs()) {
            View singlePainting = layoutInflater.inflate(R.layout.item_image_value_clickable, null);
            ImageView image = (ImageView) singlePainting.findViewById(R.id.imgPainting);
            image.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(
                    model.getPaintingbyPosition(paintingID).getImagebytearray(), 0,
                    model.getPaintingbyPosition(paintingID).getImagebytearray().length), 200, 200, true));

            //Setting the paintingID as the id of the image to be able to distinguish when it is clicked upon on Private Auction
            image.setId(paintingID);

            TextView textSecretValue = (TextView) singlePainting.findViewById(R.id.txtSecretValue);
            textSecretValue.setText(String.valueOf(model.getPlayer(myPlayerID).getOwnedPaintingValues().get(counter) + " $"));
            textSecretValue.setWidth(200);

            ll.addView(singlePainting, layoutParams);
            counter++;
        }
    }



    public void hideAllPopupContent() {
        layoutPopupGameModelSelection.setVisibility(View.INVISIBLE);
        layoutPopupPrivateAuctionInProgress.setVisibility(View.INVISIBLE);
        layoutPopupBankAuctionInProgress.setVisibility(View.INVISIBLE);
        layoutPopupPrivateAuctionSelectPainting.setVisibility(View.INVISIBLE);
        layoutPopupBankAuctionBegin.setVisibility(View.INVISIBLE);
        layoutPopupPrivateAuctionBid.setVisibility(View.INVISIBLE);
        layoutPopupBankAuctionBid.setVisibility(View.INVISIBLE);
        layoutPopupPrivateAuctionWon.setVisibility(View.INVISIBLE);
        layoutPopupBankAuctionWon.setVisibility(View.INVISIBLE);
        layoutPopupPrivateAuctionLost.setVisibility(View.INVISIBLE);
        layoutPopupBankAuctionLost.setVisibility(View.INVISIBLE);
        //layoutHomeViewInMainGameView.setVisibility(View.INVISIBLE);
        layoutStatusPopup.setVisibility(View.INVISIBLE);
    }

    @Override
    public void update(Observable observable, Object data) {
        if(observable instanceof  MasterpieceGameModel){

            //if model's popupContent changed
            if(data.toString().equals("popupContentChanged")){

                hideAllPopupContent();

                //set layout as visible for each case
                if(model.getPopupContent().equals("startTurn")) {
                    //show the start turn popup layout (game model selection)
                    layoutPopupGameModelSelection.setVisibility(View.VISIBLE);
                }
                else if(model.getPopupContent().equals("privateAuctionInProgress")) {
                    layoutPopupPrivateAuctionInProgress.setVisibility(View.VISIBLE);
                }
                else if(model.getPopupContent().equals("bankAuctionInProgress")) {
                    layoutPopupBankAuctionInProgress.setVisibility(View.VISIBLE);
                }
                else if(model.getPopupContent().equals("privateAuctionSelectPainting")) {
                    layoutPopupPrivateAuctionSelectPainting.setVisibility(View.VISIBLE);
                }
                else if(model.getPopupContent().equals("bankAuctionBegin")) {
                    layoutPopupBankAuctionBegin.setVisibility(View.VISIBLE);
                }
                else if(model.getPopupContent().equals("privateAuctionBid")) {
                    layoutPopupPrivateAuctionBid.setVisibility(View.VISIBLE);
                }
                else if(model.getPopupContent().equals("bankAuctionBid")) {
                    layoutPopupBankAuctionBid.setVisibility(View.VISIBLE);
                }
                else if(model.getPopupContent().equals("privateAuctionWon")) {
                    layoutPopupPrivateAuctionWon.setVisibility(View.VISIBLE);
                }
                else if(model.getPopupContent().equals("bankAuctionWon")) {
                    layoutPopupBankAuctionWon.setVisibility(View.VISIBLE);
                }
                else if(model.getPopupContent().equals("privateAuctionLost")) {
                    layoutPopupPrivateAuctionLost.setVisibility(View.VISIBLE);
                }
                else if(model.getPopupContent().equals("bankAuctionLost")) {
                    layoutPopupBankAuctionLost.setVisibility(View.VISIBLE);
                }
            }

            if(data.toString().equals("currentPlayerToDisplayChanged")) {

                //populate images
                populatePaintingsOtherPlayers(model.getCurrentPlayerToDisplay());

                //update player title
                TextView textPlayerTitle = (TextView) view.findViewById(R.id.txtPlayerTitle);
                textPlayerTitle.setText(model.getPlayer(model.getCurrentPlayerToDisplay()).getName() + "'s paintings:");

                //TODO maybe add the highlighting of the buttons
            }

            if(data.toString().equals("PaintingAdded")) {
                populatePaintingsOtherPlayers((model.getMyPlayer().getPlayerpositionID()+1)%4);
                populatePaintingsMyPlayer(model.getMyPlayer().getPlayerpositionID(), (LinearLayout) view.findViewById(R.id.llPaintingsOfMyPlayer));
            }

            //TODO cashChanged
            if(data.toString().equals("cashChanged")) {
                TextView textCash = (TextView) view.findViewById(R.id.txtPlayerCash);
                textCash.setText("Cash: " + model.getMyPlayer().getCash() + " $");
            }


            if(data.toString().equals("turnTakerChanged")) {

                //if I'm the turntaker
                System.out.println("From MainGameView: Turn taker set " + model.getTurnTaker() + ", PlayerID: " +
                        model.getMyPlayer().getPlayerpositionID());

                if (model.getTurnTaker().equals(String.valueOf(model.getMyPlayer().getPlayerpositionID()))) {
                    System.out.println("Came here");
                    hideAllPopupContent();
                    layoutStatusPopup.setVisibility(View.VISIBLE);
                    layoutPopupGameModelSelection.setVisibility(View.VISIBLE);
                }


            }

            if(data.toString().equals("ViewToShowPopupBid")) {
                hideAllPopupContent();
                layoutPopupBankAuctionBid.setVisibility(View.VISIBLE);
                //show highest bid
                TextView bidText = (TextView)view.findViewById(R.id.txtHighestBid);
                bidText.setText("Current Highest Bid: " + model.getCurrentBid());

                /*
                //if I'm the current bidder
                if (model.getCurrentBidder().equals(String.valueOf(model.getMyPlayer().getPlayerpositionID()))) {
                    //if it's a private auction
                    if(model.getTurnAction().equals("privateAuction")) {
                        //if there's only one bidder left(me), show private auction win screen
                        if(model.getCountNonBidders().equals("2")) {
                            hideAllPopupContent();
                            layoutPopupPrivateAuctionWon.setVisibility(View.VISIBLE);
                        }
                        //otherwise show bid screen
                        else {
                            hideAllPopupContent();
                            layoutPopupPrivateAuctionBid.setVisibility(View.VISIBLE);

                            // Update the painting to be selected
                            ImageView image = (ImageView) view.findViewById(R.id.img_PrivateAuction_PaintingBeingAuctioned);
                            image.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(
                                    model.getPaintingbyPosition(Integer.valueOf(model.getPaintingBeingAuctioned())).getImagebytearray(), 0,
                                    model.getPaintingbyPosition(Integer.valueOf(model.getPaintingBeingAuctioned())).getImagebytearray().length), 200, 200, true));

                            //Setting the paintingID as the id of the image to be able to distinguish when it is clicked upon on Private Auction
                            image.setId(Integer.valueOf(model.getPaintingBeingAuctioned()));

                            //show highest bid
                            TextView bidText = (TextView)view.findViewById(R.id.txtHighestBid);
                            bidText.setText("Current Highest Bid: " + model.getCurrentBid());
                        }
                    }
                    //if it's a bank auction
                    else if (model.getTurnAction().equals("bankAuction")) {
                        //if there's only one bidder left(me), show bank auction win screen
                        if(model.getCountNonBidders().equals("2")) {
                            hideAllPopupContent();
                            layoutPopupBankAuctionWon.setVisibility(View.VISIBLE);
                        }
                        //otherwise show bid screen
                        else {
                            hideAllPopupContent();
                            layoutPopupBankAuctionBid.setVisibility(View.VISIBLE);
                            //show highest bid
                            TextView bidText = (TextView)view.findViewById(R.id.txtHighestBid);
                            bidText.setText("Current Highest Bid: " + model.getCurrentBid());
                        }
                    }
                }
                //if bidder isn't me
                else {
                    //if it's a private auction
                    System.out.println(model.getTurnAction());
                    if(model.getTurnAction().equals("privateAuction")) {
                        //if there's only one bidder left(and it's not me), show private auction lose screen
                        if(model.getCountNonBidders().equals("2")) {
                            hideAllPopupContent();
                            layoutPopupPrivateAuctionLost.setVisibility(View.VISIBLE);
                        }
                    }
                    //if it's a bank auction
                    else if (model.getTurnAction().equals("bankAuction")) {
                        //if there's only one bidder left(and it's not me), show bank auction lose screen
                        if(model.getCountNonBidders().equals("2")) {
                            hideAllPopupContent();
                            layoutPopupBankAuctionLost.setVisibility(View.VISIBLE);
                        }
                    }
                }*/
            }

            if(data.toString().equals("turnActionChanged")) {

                //if the turn is a private auction
                if (model.getTurnAction().equals("privateAuction")) {
                    hideAllPopupContent();
                    layoutPopupPrivateAuctionInProgress.setVisibility(View.VISIBLE);
                }
                //if the turn is a bank auction
                else if (model.getTurnAction().equals("bankAuction")) {
                    hideAllPopupContent();
                    layoutPopupBankAuctionInProgress.setVisibility(View.VISIBLE);
                }
            }
        }

    }
}
