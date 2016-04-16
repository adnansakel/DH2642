package com.example.adnansakel.masterpiece;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.adnansakel.masterpiece.model.MasterpieceGameModel;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView txtHello;
    MasterpieceGameModel masterpieceGameModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        masterpieceGameModel = ((MasterpieceApplication) this.getApplication()).getModel();
       // MainView mainView = new MainView(findViewById(R.id.relative_layout_Hello),masterpieceGameModel);
        txtHello = ((TextView)findViewById(R.id.textViewHello));
        txtHello.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v  == txtHello){
           // txtHello.setTextColor(Color.parseColor("#FF0000"));
            masterpieceGameModel.setColor("#ff0000");
        }

    }
}
