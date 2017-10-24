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
    public static final int MOVIMIENTO_DERECHA = 0;
    public static final int MOVIMIENTO_IZQUIERDA = 1;
    public static final int MOVIMIENTO_ABAJO = 2;
    public static final int MOVIMIENTO_ARRIBA = 3;
    public static final int PARADO = 4;

    public static final String PARADO_DERECHA = "Parado_derecha";
    public static final String PARADO_IZQUIERDA = "Parado_izquierda";

    private Sprite sprite;
    private HashMap<String,Sprite> sprites = new HashMap<String,Sprite>();

    private double xInicial;
    private double yInicial;

    public double aceleracionX;
    public double aceleracionY;

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

        sprite = paradoDerecha;

    }

    public void actualizar (long tiempo) {
        sprite.actualizar(tiempo);
    }

    public void procesarOrdenes (int orientacionPad) {

        if (orientacionPad == MOVIMIENTO_DERECHA) {
            aceleracionY = 0;
            aceleracionX = 5;
        }
        else if (orientacionPad == MOVIMIENTO_ABAJO){
            aceleracionX = 0;
            aceleracionY = 5;
            //orientacion = ABAJO;
        }
        else if(orientacionPad == MOVIMIENTO_IZQUIERDA){
            aceleracionY  =0;
            aceleracionX= -5;
            //orientacion = izquierda
        }
        else if(orientacionPad == MOVIMIENTO_ARRIBA) {
            aceleracionX = 0;
            aceleracionY = -5;
        }
        else if(orientacionPad == PARADO)
            frenar();

    }

    public void dibujar(Canvas canvas){
        sprite.dibujarSprite(canvas, (int) x - Sala.scrollEjeX, (int) y - Sala.scrollEjeY);
    }

    private void frenar(){
        aceleracionX=0;
        aceleracionY=0;
    }


}
