package com.example.adnansakel.masterpiece.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.text.DecimalFormat;
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

    Button button_secondPlayer;
    Button button_thirdPlayer;
    Button button_fourthPlayer;
    Button button_end_round;
    Button button_status_bar;
    TextView textWinnerName;

    DecimalFormat formatter = new DecimalFormat("#,###,###");

    public MainGameView(View view, MasterpieceGameModel model) {
        this.view = view;
        button_status_bar = (Button)view.findViewById(R.id.buttonStatusBar);
        initialize();
        model.addObserver(this);
        this.model = model;

        layoutInflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //set cash
        TextView textCash = (TextView) view.findViewById(R.id.txtPlayerCash);
        String cashFormatted = formatter.format(model.getMyPlayer().getCash());
        textCash.setText("Cash: " + cashFormatted  + " $");

        //set currently selected player text3
        TextView textPlayerTitle = (TextView) view.findViewById(R.id.txtPlayerTitle);
        textPlayerTitle.setText(model.getPlayer((model.getMyPlayer().getPlayerpositionID() + 1)%4).getName() + "'s paintings:");

        button_secondPlayer = (Button)view.findViewById(R.id.btnSecondPlayer);
        button_thirdPlayer = (Button)view.findViewById(R.id.btnThirdPlayer);
        button_fourthPlayer = (Button)view.findViewById(R.id.btnFourthPlayer);
        button_end_round = (Button)view.findViewById(R.id.btn_end_round);
        textWinnerName = (TextView)view.findViewById(R.id.textViewWinnerName);
        button_end_round.setVisibility(View.INVISIBLE);
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

        //set the popup and content views to invisible
        layoutStatusPopup.setVisibility(View.INVISIBLE);
        hideAllPopupContent();
        button_status_bar.setBackgroundResource(R.drawable.downarrow);

        //layoutHomeViewInMainGameView.setVisibility(View.VISIBLE);
        //layoutStatusPopup.setVisibility(View.VISIBLE);
        //layoutPopupGameModelSelection.setVisibility(View.VISIBLE);
    }

    public void populatePaintingsOtherPlayers(Integer selectedPlayerID){
        LinearLayout layoutPaintingsOtherPlayers = (LinearLayout)view.findViewById(R.id.llPaintingsOfOtherPlayers);
        HorizontalScrollView hsvTopPanel = (HorizontalScrollView)view.findViewById(R.id.hsvTopPanel);
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
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
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
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
            String secretValueFormatted = formatter.format(model.paintingValue.get(paintingID));
            textSecretValue.setText(secretValueFormatted + " $");
            //textSecretValue.setWidth(200);

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

            //Setting the paintingID as the id of the image to be able to distinguish when it is clicked upon on ate Auction
            image.setId(paintingID);

            TextView textSecretValue = (TextView) singlePainting.findViewById(R.id.txtSecretValue);
            String secretValueFormatted = formatter.format(model.paintingValue.get(paintingID));
            textSecretValue.setText(secretValueFormatted + " $");
            textSecretValue.setWidth(200);

            ll.addView(singlePainting, layoutParams);
            counter++;
        }
    }

    public void hideAllPopupContent() {
        layoutPopupGameModelSelection.setVisibility(View.INVISIBLE);
        //layoutHomeViewInMainGameView.setTranslationY(-layoutHomeViewInMainGameView.getHeight());
        layoutPopupPrivateAuctionInProgress.setVisibility(View.INVISIBLE);
        //layoutPopupPrivateAuctionInProgress.setTranslationY(-layoutPopupPrivateAuctionInProgress.getHeight());
        layoutPopupBankAuctionInProgress.setVisibility(View.INVISIBLE);
        //layoutPopupPrivateAuctionInProgress.setTranslationY(-layoutPopupBankAuctionInProgress.getHeight());
        layoutPopupPrivateAuctionSelectPainting.setVisibility(View.INVISIBLE);
        //layoutPopupPrivateAuctionSelectPainting.setTranslationY(-layoutPopupPrivateAuctionSelectPainting.getHeight());
        layoutPopupBankAuctionBegin.setVisibility(View.INVISIBLE);
        //layoutPopupBankAuctionBegin.setTranslationY(-layoutPopupBankAuctionBegin.getHeight());
        layoutPopupPrivateAuctionBid.setVisibility(View.INVISIBLE);
        //layoutPopupPrivateAuctionBid.setTranslationY(-layoutPopupPrivateAuctionBid.getHeight());
        layoutPopupBankAuctionBid.setVisibility(View.INVISIBLE);
        //layoutPopupBankAuctionBid.setTranslationY(-layoutPopupPrivateAuctionBid.getHeight());
        layoutPopupPrivateAuctionWon.setVisibility(View.INVISIBLE);
        //layoutPopupPrivateAuctionWon.setTranslationY(-layoutPopupPrivateAuctionBid.getHeight());
        layoutPopupBankAuctionWon.setVisibility(View.INVISIBLE);
        //layoutPopupBankAuctionWon.setTranslationY(-layoutPopupBankAuctionWon.getHeight());
        layoutPopupPrivateAuctionLost.setVisibility(View.INVISIBLE);
        //layoutPopupPrivateAuctionLost.setTranslationY(-layoutPopupPrivateAuctionLost.getHeight());
        layoutPopupBankAuctionLost.setVisibility(View.INVISIBLE);
        //layoutPopupBankAuctionLost.setTranslationY(-layoutPopupBankAuctionLost.getHeight());
        //layoutHomeViewInMainGameView.setVisibility(View.INVISIBLE);
        layoutStatusPopup.setVisibility(View.INVISIBLE);
        layoutStatusPopup.setTranslationY(-layoutStatusPopup.getHeight());
        button_status_bar.setBackgroundResource(R.drawable.uparrow);
    }

    @Override
    public void update(Observable observable, Object data) {
        if(observable instanceof  MasterpieceGameModel){

            if(data.toString().equals("currentPlayerToDisplayChanged")){
                int myPlayerID = Integer.valueOf(model.getMyPlayer().getPlayerpositionID());
                int secondPlayerID = (myPlayerID + 1)%4;
                int thirdPlayerID = (myPlayerID + 2)%4;
                int fourthPlayerID = (myPlayerID + 3)%4;

                if(model.getCurrentPlayerToDisplay()==secondPlayerID){

                    button_secondPlayer.setBackgroundResource(R.drawable.rounded_rect_blue_pressed);
                    button_thirdPlayer.setBackgroundResource(R.drawable.rounded_rect_blue);
                    button_fourthPlayer.setBackgroundResource(R.drawable.rounded_rect_blue);
                }
                else if(model.getCurrentPlayerToDisplay()==thirdPlayerID){

                    button_secondPlayer.setBackgroundResource(R.drawable.rounded_rect_blue);
                    button_thirdPlayer.setBackgroundResource(R.drawable.rounded_rect_blue_pressed);
                    button_fourthPlayer.setBackgroundResource(R.drawable.rounded_rect_blue);
                }
                else if(model.getCurrentPlayerToDisplay()==fourthPlayerID){

                    button_secondPlayer.setBackgroundResource(R.drawable.rounded_rect_blue);
                    button_thirdPlayer.setBackgroundResource(R.drawable.rounded_rect_blue);
                    button_fourthPlayer.setBackgroundResource(R.drawable.rounded_rect_blue_pressed);

                }
            }
            //if model's popupContent changed
            if(data.toString().equals("popupContentChanged")){
                /*
                * Commented by Sakel
                * */
                /*
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
                    //show cash

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
                */
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
                populatePaintingsOtherPlayers((model.getMyPlayer().getPlayerpositionID() + 1) % 4);
                populatePaintingsMyPlayer(model.getMyPlayer().getPlayerpositionID(), (LinearLayout) view.findViewById(R.id.llPaintingsOfMyPlayer));
            }

            //TODO cashChanged
            if(data.toString().equals("cashChanged")) {
                String cashValueFormatted = formatter.format(Integer.valueOf(model.getMyPlayer().getCash()));
                TextView textCash = (TextView) view.findViewById(R.id.txtPlayerCash);
                textCash.setText("Cash: " + cashValueFormatted + " $");

            }
            if(data.toString().equals(AppConstants.NOTIFY_FOR_UPDATED_PAINTING_AND_CASH)){
                populatePaintingsMyPlayerPrivateAuction(model.getMyPlayer().getPlayerpositionID(), (LinearLayout) view.findViewById(R.id.llPaintingsOfMyPlayer));
                populatePaintingsOtherPlayers((model.getMyPlayer().getPlayerpositionID()+1)%4);
            }

            if(data.toString().equals(AppConstants.WINNERFOUND) && model.getWinner().length()>0){
                System.out.println("Winner is player " + model.getWinner());

                textWinnerName.setText(model.getAllPlayers().get(Integer.valueOf(model.getWinner())).getName());
                TextView winningAmount = (TextView)view.findViewById(R.id.txtHighestBidResult);

                String bidValueFormatted = formatter.format(Integer.valueOf(model.getCurrentBid()));
                String strWinningAmount = "Winning Bid Amount: \n" +bidValueFormatted + " $";
                winningAmount.setText(strWinningAmount);
                if(model.getWinner().equals(model.getMyPlayer().getPlayerpositionID()+"")){
                    //Make the btn_end_round visible
                    button_end_round.setVisibility(View.VISIBLE);
                }
            }

            if(model.getCountNonBidders().equals("3")){
                //end round
                //Display result screen
                if(AppConstants.IamCreator){
                    //update turntaker remove painting add paiting to winner cash change in firebase
                    //then hide the result screen
                }
                /*
                hideAllPopupContent();
                layoutStatusPopup.setVisibility(View.VISIBLE);
                layoutPopupPrivateAuctionWon.setVisibility(View.VISIBLE);*/
                if(!data.toString().equals("currentPlayerToDisplayChanged")){
                    ShowAnimatedView(layoutPopupPrivateAuctionWon);
                }

            }
            else{
                if(data.toString().equals(AppConstants.TURN_TAKER_CHANGED)){
                    model.setWinner("");
                    button_end_round.setVisibility(View.INVISIBLE);
                    if(model.getTurnTaker().equals(String.valueOf(model.getMyPlayer().getPlayerpositionID()))){
                        /*
                        hideAllPopupContent();
                        layoutStatusPopup.setVisibility(View.VISIBLE);
                        layoutPopupGameModelSelection.setVisibility(View.VISIBLE);
                        */
                        ShowAnimatedView(layoutPopupGameModelSelection);

                    }
                    else{
                        //Any thing else if we wish to display
                    }
                }
                if(data.toString().equals(AppConstants.CURRENT_BIDDER_CHANGED)){

                    if(model.getCurrentBidder().equals(String.valueOf(model.getMyPlayer().getPlayerpositionID()))){
                        if(model.getMyPlayer().isBidding()){
                            //Display bidding screen. Let's use same bidding screen for both type of auction
                            /*hideAllPopupContent();
                            layoutStatusPopup.setVisibility(View.VISIBLE);
                            layoutPopupPrivateAuctionBid.setVisibility(View.VISIBLE);
                            */
                            ShowAnimatedView(layoutPopupPrivateAuctionBid);
                            ImageView image = (ImageView) view.findViewById(R.id.img_PrivateAuction_PaintingBeingAuctioned);
                            image.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(
                                    model.getPaintingbyPosition(Integer.valueOf(model.getPaintingBeingAuctioned())).getImagebytearray(), 0,
                                    model.getPaintingbyPosition(Integer.valueOf(model.getPaintingBeingAuctioned())).getImagebytearray().length), 200, 200, true));
                            TextView paintingTitle = (TextView)view.findViewById(R.id.txtPaintingTitle);
                            paintingTitle.setText(model.getPaintingbyPosition(Integer.valueOf(model.getPaintingBeingAuctioned())).getName());
                            TextView artist = (TextView)view.findViewById(R.id.txtArtist);
                            artist.setText("by " + model.getPaintingbyPosition(Integer.valueOf(model.getPaintingBeingAuctioned())).getArtist());
                            String currentBidValueFormatted = formatter.format(Integer.valueOf(model.getCurrentBid()));
                            TextView bidText = (TextView)view.findViewById(R.id.txtHighestBid);
                            bidText.setText("Current Highest Bid: " + currentBidValueFormatted);
                        }

                    }
                    else if(!model.getCurrentBidder().equals("100")){
                        /*hideAllPopupContent();
                        layoutStatusPopup.setVisibility(View.VISIBLE);
                        layoutPopupPrivateAuctionInProgress.setVisibility(View.VISIBLE);*/
                        ShowAnimatedView(layoutPopupPrivateAuctionInProgress);

                    }
                }
                if(data.toString().equals(AppConstants.TURN_ACTION_CHANGED)){
                    if(model.getTurnAction().equals(AppConstants.PRIVATE)){
                        if (model.getTurnTaker().equals(model.getMyPlayer().getPlayerpositionID() + "")){
                            //Display screen for private auction
                            /*hideAllPopupContent();
                            layoutStatusPopup.setVisibility(View.VISIBLE);
                            layoutPopupPrivateAuctionSelectPainting.setVisibility(View.VISIBLE);*/
                            ShowAnimatedView(layoutPopupPrivateAuctionSelectPainting);
                            populatePaintingsMyPlayerPrivateAuction(model.getMyPlayer().getPlayerpositionID(),(LinearLayout)view.findViewById(R.id.ll_PrivateAuction_PaintingsToSelect));

                        }
                        else{
                            //Display screen for private auction opn progress
                            /*hideAllPopupContent();
                            layoutPopupPrivateAuctionInProgress.setVisibility(View.VISIBLE);*/
                            ShowAnimatedView(layoutPopupPrivateAuctionInProgress);
                        }
                    }
                    else if(model.getTurnAction().equals(AppConstants.BANK)){
                        //Display bank auction on progress or display bank auction screen
                    }
                }
            }

            if(data.toString().equals("turnTakerChanged")) {

                /*
                * Commented by Sakel
                * */
                /*
                //if I'm the turntaker
                System.out.println("From MainGameView: Turn taker set " + model.getTurnTaker() + ", PlayerID: " +
                        model.getMyPlayer().getPlayerpositionID());

                if (model.getTurnTaker().equals(String.valueOf(model.getMyPlayer().getPlayerpositionID()))) {
                    System.out.println("Came here");
                    hideAllPopupContent();
                    layoutStatusPopup.setVisibility(View.VISIBLE);
                    layoutPopupGameModelSelection.setVisibility(View.VISIBLE);
                }*/


            }

            if(data.toString().equals("ViewToShowPopupBid")) {
                /*
                * Commented by Sakel
                * */

                /*System.out.println("ViewToShowPopupBid in view");
                if (model.getCurrentBidder().equals(String.valueOf(model.getMyPlayer().getPlayerpositionID()))) {
                    System.out.println("Current bidder matches my player");
                    hideAllPopupContent();
                    layoutStatusPopup.setVisibility(View.VISIBLE);
                    layoutPopupPrivateAuctionBid.setVisibility(View.VISIBLE);
                    //show highest bid
                    TextView bidText = (TextView)view.findViewById(R.id.txtHighestBid);
                    bidText.setText("Current Highest Bid: " + model.getCurrentBid());
                }*/


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

            /*
            * Commented by Sakel
            * */

            /*if(data.toString().equals("turnActionChanged")) {

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
            */
        }

    }

    private void ShowAnimatedView(View view){
        if(layoutStatusPopup.getVisibility()==View.INVISIBLE){
            hideAllPopupContent();
            view.setVisibility(View.VISIBLE);
            layoutStatusPopup.setVisibility(View.VISIBLE);
            layoutStatusPopup.animate().translationY(0)
                    .setDuration(1000).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    layoutStatusPopup.setVisibility(View.VISIBLE);
                    button_status_bar.setBackgroundResource(R.drawable.uparrow);
                }
            });
        }
        else{
            hideAllPopupContent();
            view.setVisibility(View.VISIBLE);
            layoutStatusPopup.setVisibility(View.VISIBLE);
            layoutStatusPopup.animate().translationY(0)
                    .setDuration(0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    layoutStatusPopup.setVisibility(View.VISIBLE);
                    button_status_bar.setBackgroundResource(R.drawable.uparrow);
                }
            });
        }


    }
}
