package com.isaac;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.isaac.gestores.GestorAudio;

public class MainActivity extends Activity {
    GameView gameView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        gameView = new GameView(this);
        setContentView(gameView);
        gameView.numeroNivel = 0;
        gameView.requestFocus();
    }

    @Override
    public void onBackPressed() {
        finish();
        System.gc();

        synchronized(gameView.gameloop)
        {
            gameView.context = null;
            gameView.gameloop.setRunning(false);
            gameView = null;
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        gameView.gameloop = null;
        gameView = null;

        GestorAudio.getInstancia().reproducirSonido(GestorAudio.ISAAC_DIES);

        GestorAudio.getInstancia().pararMusicaAmbiente();
        GestorAudio.getInstancia().changeSound(R.raw.main_theme);
        GestorAudio.getInstancia().reproducirMusicaAmbiente();

    }

}