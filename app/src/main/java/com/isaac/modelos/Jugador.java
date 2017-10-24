package com.isaac.modelos;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import com.isaac.R;
import com.isaac.gestores.CargadorGraficos;
import com.isaac.graficos.Sprite;

import java.util.HashMap;

/**
 * Created by Alex on 24/10/2017.
 */

public class Jugador extends Modelo{
    public static final String PARADO_DERECHA = "Parado_derecha";
    public static final String PARADO_IZQUIERDA = "Parado_izquierda";

    private Sprite sprite;
    private HashMap<String,Sprite> sprites = new HashMap<String,Sprite>();

    double xInicial;
    double yInicial;

    double aceleracionX;
    double aceleracionY;

    public Jugador(Context context, double xInicial, double yInicial) {
        super(context, 0, 0, 40, 40);

        // guardamos la posición inicial porque más tarde vamos a reiniciarlo
        this.xInicial = xInicial;
        this.yInicial = yInicial - altura/2;

        this.x =  this.xInicial;
        this.y =  this.yInicial;

        aceleracionX = 0;
        aceleracionY = 0;

        inicializar();
    }

    public void inicializar (){
        Sprite paradoDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.playeridleright),
                ancho, altura,
                4, 8, true);
        sprites.put(PARADO_DERECHA, paradoDerecha);

        Sprite paradoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.playeridle),
                ancho, altura,
                4, 8, true);
        sprites.put(PARADO_IZQUIERDA, paradoIzquierda);

// animación actual
        sprite = paradoDerecha;
    }

    public void actualizar (long tiempo) {
        sprite.actualizar(tiempo);
    }

    public void procesarOrdenes (int orientacionPad) {

        if (orientacionPad == 0) {
            aceleracionY = 0;
            aceleracionX=5;
            //orientacion = DERECHA;
        } else if (orientacionPad ==1){
            aceleracionX = 0;
            aceleracionY=5;
            //orientacion = ABAJO;
        } else if(orientacionPad==2){
            aceleracionY=0;
            aceleracionX=-5;
            //orientacion = izquierda
        }
        else if(orientacionPad==3) {
            aceleracionX = 0;
            aceleracionY = -5;
        }
        else if(orientacionPad==4)
            frenar();

        Log.d("Orientacion", String.valueOf(orientacionPad));
    }

    public void dibujar(Canvas canvas){
        sprite.dibujarSprite(canvas, (int) x, (int) y);
    }

    private void frenar(){
        aceleracionX=0;
        aceleracionY=0;
    }


}
