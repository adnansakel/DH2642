package com.example.adnansakel.masterpiece;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adnansakel.masterpiece.model.MasterpieceGameModel;
import android.os.Handler;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView txtHello;
    Button btntestbutton;
    MasterpieceGameModel masterpieceGameModel;
    int paintingCounter = 0;
    Handler handler;
    boolean mStopHandler = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        masterpieceGameModel = ((MasterpieceApplication) this.getApplication()).getModel();
       // MainView mainView = new MainView(findViewById(R.id.relative_layout_Hello),masterpieceGameModel);
        txtHello = ((TextView)findViewById(R.id.textViewHello));
        btntestbutton = (Button)findViewById(R.id.btntestButton);
        txtHello.setOnClickListener(this);
        btntestbutton.setOnClickListener(this);
        /*ProgressDialog progress;
        progress = ProgressDialog.show(this, "", "Downloading paintings ...", true);
        ImageDownloader imageDownloader = new ImageDownloader(MainActivity.this,masterpieceGameModel);
        imageDownloader.downloadImages(progress);
        handler = new Handler();*/
        FirebaseCalls firebaseCalls = new FirebaseCalls(this,masterpieceGameModel);
        firebaseCalls.distributeShuffledPaintingandValues();
    }

    @Override
    public void onClick(View v) {
        if(v  == txtHello){
            txtHello.setTextColor(Color.parseColor("#FF0000"));
            //masterpieceGameModel.setColor("#ff0000");
/*
                    ((ImageView) findViewById(R.id.imgTest1)).setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(
                            masterpieceGameModel.getPaintingbyPosition(0).getImagebytearray(), 0,
                            masterpieceGameModel.getPaintingbyPosition(0).getImagebytearray().length),100,100,true));

            ((ImageView)findViewById(R.id.imgTest2)).setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(
                    masterpieceGameModel.getPaintingbyPosition(1).getImagebytearray(), 0,
                    masterpieceGameModel.getPaintingbyPosition(1).getImagebytearray().length),100,100,true));

            ((ImageView)findViewById(R.id.imgTest3)).setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(
                    masterpieceGameModel.getPaintingbyPosition(2).getImagebytearray(), 0,
                    masterpieceGameModel.getPaintingbyPosition(2).getImagebytearray().length),100,100,true));

            ((ImageView)findViewById(R.id.imgTest4)).setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(
                    masterpieceGameModel.getPaintingbyPosition(3).getImagebytearray(), 0,
                    masterpieceGameModel.getPaintingbyPosition(3).getImagebytearray().length),100,100,true));

            ((ImageView)findViewById(R.id.imgTest5)).setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(
                    masterpieceGameModel.getPaintingbyPosition(4).getImagebytearray(), 0,
                    masterpieceGameModel.getPaintingbyPosition(4).getImagebytearray().length),100,100,true));

            ((ImageView)findViewById(R.id.imgTest6)).setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(
                    masterpieceGameModel.getPaintingbyPosition(5).getImagebytearray(), 0,
                    masterpieceGameModel.getPaintingbyPosition(5).getImagebytearray().length),100,100,true));

            ((ImageView)findViewById(R.id.imgTest7)).setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(
                    masterpieceGameModel.getPaintingbyPosition(6).getImagebytearray(), 0,
                    masterpieceGameModel.getPaintingbyPosition(6).getImagebytearray().length),100,100,true));

            ((ImageView)findViewById(R.id.imgTest8)).setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(
                    masterpieceGameModel.getPaintingbyPosition(7).getImagebytearray(), 0,
                    masterpieceGameModel.getPaintingbyPosition(7).getImagebytearray().length),100,100,true));

            ((ImageView)findViewById(R.id.imgTest9)).setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(
                    masterpieceGameModel.getPaintingbyPosition(8).getImagebytearray(), 0,
                    masterpieceGameModel.getPaintingbyPosition(8).getImagebytearray().length),100,100,true));

            ((ImageView)findViewById(R.id.imgTest10)).setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(
                    masterpieceGameModel.getPaintingbyPosition(9).getImagebytearray(), 0,
                    masterpieceGameModel.getPaintingbyPosition(9).getImagebytearray().length),100,100,true));
                    */
            /*
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    // do your stuff - don't create a new runnable here!
                    if(paintingCounter == masterpieceGameModel.getAllPaintings().size()){//10 images
                        handler.removeCallbacks(this);
                        //progress.dismiss();
                        System.out.println("done..."+paintingCounter);
                        mStopHandler = true;

                    }
                    else if(paintingCounter<masterpieceGameModel.getAllPaintings().size()){

                        Bitmap imgbmp = BitmapFactory.decodeByteArray(masterpieceGameModel.getPaintingbyPosition(paintingCounter).getImagebytearray()
                                , 0, masterpieceGameModel.getPaintingbyPosition(paintingCounter).getImagebytearray().length);

                        ((ImageView)findViewById(R.id.imgTest)).setImageBitmap(imgbmp);
                        paintingCounter++;


                    }
                    if (!mStopHandler) {
                        handler.postDelayed(this, 1000);
                    }
                }
            };

// start it with:
            handler.post(runnable);*/
        }
        else if(v == btntestbutton){
            Toast.makeText(this,"Button clicked",Toast.LENGTH_LONG).show();
        }

    }
}
