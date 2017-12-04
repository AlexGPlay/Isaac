package com.isaac;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class TitleActivity extends AppCompatActivity {

    public static final boolean DEBUG = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(DEBUG)
            changeToGame();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        setContentView(R.layout.activity_title);
    }

    public void changeToSelect(View v){
        Intent actividadJuego = new Intent(this, CharacterActivity.class);
        startActivity(actividadJuego);
    }

    public void changeToGame(){
        Intent actividadJuego = new Intent(this, MainActivity.class);
        startActivity(actividadJuego);
    }

}
