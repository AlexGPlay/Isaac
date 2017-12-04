package com.isaac;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.isaac.R;
import com.isaac.modelos.Jugador;

public class CharacterActivity extends AppCompatActivity {

    private ImageButton jugador;
    private int actual = 0;
    private int[] imagenes = {R.drawable.isaac_seleccion, R.drawable.samson_seleccion};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        setContentView(R.layout.activity_character);

        jugador = (ImageButton) findViewById(R.id.btnJugador);

        setImage();
    }

    public void changeRight(View v){
        actual++;

        if(actual>=imagenes.length)
            actual = 0;

        setImage();
    }

    public void changeLeft(View v){
        actual--;

        if(actual<0)
            actual = imagenes.length-1;

        setImage();
    }

    public void launchGame(View v){
        Jugador.JUGADOR_ACTUAL = actual;
        Intent actividadJuego = new Intent(this, MainActivity.class);
        startActivity(actividadJuego);
    }

    public void setImage(){
        jugador.setImageResource(imagenes[actual]);
    }

}
