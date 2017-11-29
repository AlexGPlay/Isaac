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
 * Created by Pelayo on 29/11/2017.
 */

public class DisparoEnemigo extends Modelo {
    public static final int DISPARANDO = 0;
    public static final int FINALIZANDO = 1;
    public static final int FINALIZADO = 2;

    public int estado;

    private Sprite sprite;
    private final static double velocidad = 10;
    private int aceleracionX;
    private int aceleracionY;

    private long tearRange;
    private long actualRange;

    private double damage;

    private int orientacion;
    private HashMap<String,Sprite> sprites = new HashMap<>();
    private String disparando = "DISPARANDO";
    private String desapareciendo = "DESAPARECIENDO";

    public DisparoEnemigo(Context context, double xInicial, double yInicial, long tearRange, double damage, int orientacion) {
        super(context, xInicial, yInicial, 12, 12);

        cDerecha = 6;
        cIzquierda = 6;
        cArriba = 6;
        cAbajo = 6;
        aceleracionX=3;
        aceleracionY=3;

        this.orientacion = orientacion;

        this.tearRange = tearRange;
        this.damage = damage;
        actualRange = 0;

        estado = DISPARANDO;
        inicializar();
    }

    private void inicializar (){
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
        x+=aceleracionX;

        actualRange += tiempo;
    }

    public boolean isOutOfRange(){
        return actualRange >= tearRange;
    }

    public void dibujar(Canvas canvas){
        sprite.dibujarSprite(canvas, (int) x - Sala.scrollEjeX, (int) y - Sala.scrollEjeY);
    }

    public int getAceleracionX(){
        return aceleracionX;
    }

    public int getAceleracionY(){
        return aceleracionY;
    }

    public double getDamage(){
        return damage;
    }

    public DisparoJugador clone(){
        return new DisparoJugador(context,x,y,tearRange,damage,orientacion);
    }

    public int getTipoModelo(){
        return Modelo.DISPARO;
    }
}
