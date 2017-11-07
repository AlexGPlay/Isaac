package com.isaac.modelos.disparos;

import android.content.Context;
import android.graphics.Canvas;

import com.isaac.R;
import com.isaac.gestores.CargadorGraficos;
import com.isaac.graficos.Sprite;
import com.isaac.modelos.Jugador;
import com.isaac.modelos.Modelo;
import com.isaac.modelos.nivel.Sala;

import java.util.HashMap;

/**
 * Created by alexgp1234 on 29/10/17.
 */

public class DisparoJugador extends Modelo {
    public static final int DISPARANDO = 0;
    public static final int FINALIZANDO = 1;
    public static final int FINALIZADO = 2;

    public int estado;

    private Sprite sprite;
    public final static double velocidadX = 10;
    public final static double velocidadY = 10;
    public int aceleracionX;
    public int aceleracionY;

    public long tearRange;
    public long actualRange;

    public double damage;

    private int orientacion;
    private HashMap<String,Sprite> sprites = new HashMap<String,Sprite>();
    private String disparando = "DISPARANDO";
    private String desapareciendo = "DESAPARECIENDO";

    public DisparoJugador(Context context, double xInicial, double yInicial, long tearRange, double damage, int orientacion) {
        super(context, xInicial, yInicial, 12, 12);

        cDerecha = 6;
        cIzquierda = 6;
        cArriba = 6;
        cAbajo = 6;

        this.orientacion = orientacion;

        this.tearRange = tearRange;
        this.damage = damage;
        actualRange = 0;

        if(orientacion == Jugador.DISPARO_DERECHA){
            aceleracionY = 0;
            aceleracionX = (int)velocidadX;
        }

        else if(orientacion == Jugador.DISPARO_IZQUIERDA){
            aceleracionY = 0;
            aceleracionX = -(int)velocidadX;
        }

        else if(orientacion == Jugador.DISPARO_ABAJO){
            aceleracionX = 0;
            aceleracionY = (int)velocidadY;
        }

        else if(orientacion == Jugador.DISPARO_ARRIBA){
            aceleracionX = 0;
            aceleracionY = -(int)velocidadY;
        }

        estado = DISPARANDO;
        inicializar();
    }

    public void inicializar (){
        Sprite disparo = new Sprite(CargadorGraficos.cargarDrawable(context, R.drawable.isaac_tear), ancho, altura, 1, 1, true);
        sprites.put(disparando, disparo);

        Sprite desaparecer = new Sprite(CargadorGraficos.cargarDrawable(context, R.drawable.isaac_tear_effect), ancho, altura, 120, 16, false);
        sprites.put(desapareciendo, desaparecer);

        sprite = sprites.get(disparando);
    }

    public void actualizar (long tiempo) {
        boolean finSprite = sprite.actualizar(tiempo);

        if(estado == FINALIZANDO && finSprite){
            estado = FINALIZADO;
        }

        else if(estado == FINALIZANDO){
            sprite = sprites.get(desapareciendo);
        }

        else if(estado == DISPARANDO){
            sprite = sprites.get(disparando);
        }

        actualRange += tiempo;
    }

    public boolean isOutOfRange(){
        if(actualRange >= tearRange)
            return true;

        return false;
    }

    public void dibujar(Canvas canvas){
        sprite.dibujarSprite(canvas, (int) x - Sala.scrollEjeX, (int) y - Sala.scrollEjeY);
    }

    public DisparoJugador clone(){
        return new DisparoJugador(context, x, y, tearRange, damage, orientacion);
    }

}
