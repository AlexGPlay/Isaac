package com.isaac;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.isaac.gestores.GestorAudio;
import com.isaac.modelos.Jugador;

public class TitleActivity extends AppCompatActivity {

    private GestorAudio gestorAudio;
    public static final boolean DEBUG = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inicializarGestorAudio(this);

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

    @Override
    protected void onPause() {
        if (GestorAudio.getInstancia() != null){
            GestorAudio.getInstancia().pararMusicaAmbiente();
        }
        super.onPause();
    }
    @Override
    protected void onResume() {
        if (GestorAudio.getInstancia() != null){
            GestorAudio.getInstancia().reproducirMusicaAmbiente();
        }
        super.onResume();
    }

    public void changeToSelect(View v){
        Intent actividadJuego = new Intent(this, CharacterActivity.class);
        startActivity(actividadJuego);
    }

    public void changeToGame(){
        Jugador.JUGADOR_ACTUAL = Jugador.DEBUG;
        Intent actividadJuego = new Intent(this, MainActivity.class);
        startActivity(actividadJuego);
    }

    private void inicializarGestorAudio(Context context){
        gestorAudio = GestorAudio.getInstancia(context, R.raw.main_theme);
        gestorAudio.reproducirMusicaAmbiente();

        registrarSonidos();
    }

    private void registrarSonidos(){
        gestorAudio.registrarSonido(GestorAudio.DISPARAR_LAGRIMA, R.raw.tear_fire);
        gestorAudio.registrarSonido(GestorAudio.DESAPARECER_LAGRIMA, R.raw.tear_disappear);
        gestorAudio.registrarSonido(GestorAudio.BOMBA_EXPLOTAR, R.raw.bomb_explosion);
        gestorAudio.registrarSonido(GestorAudio.ISAAC_DAÑO, R.raw.isaac_hurt);
        gestorAudio.registrarSonido(GestorAudio.PUERTA_ABRIR, R.raw.door_open);
        gestorAudio.registrarSonido(GestorAudio.PUERTA_CERRAR, R.raw.door_close);
        gestorAudio.registrarSonido(GestorAudio.DROP_COFRE, R.raw.chest_drop);
        gestorAudio.registrarSonido(GestorAudio.DROP_LLAVE, R.raw.key_drop);
        gestorAudio.registrarSonido(GestorAudio.DROP_MONEDA, R.raw.coin_drop);
        gestorAudio.registrarSonido(GestorAudio.PICK_LLAVE, R.raw.key_pick);
        gestorAudio.registrarSonido(GestorAudio.PICK_MONEDA, R.raw.coin_pick);
        gestorAudio.registrarSonido(GestorAudio.PICK_COFRE, R.raw.chest_pick);
        gestorAudio.registrarSonido(GestorAudio.PICK_ITEM, R.raw.item_pick);
        gestorAudio.registrarSonido(GestorAudio.ISAAC_DIES, R.raw.isaac_dies);
        gestorAudio.registrarSonido(GestorAudio.SELECT_LEFT, R.raw.select_left);
        gestorAudio.registrarSonido(GestorAudio.SELECT_RIGHT, R.raw.select_right);
        gestorAudio.registrarSonido(GestorAudio.START_UP, R.raw.start_up);
    }

}
