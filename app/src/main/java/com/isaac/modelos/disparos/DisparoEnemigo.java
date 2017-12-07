package com.isaac.modelos.disparos;

import android.content.Context;
import android.graphics.Canvas;

import com.isaac.R;
import com.isaac.gestores.CargadorGraficos;
import com.isaac.graficos.Sprite;
import com.isaac.modelos.Jugador;
import com.isaac.modelos.Modelo;
import com.isaac.modelos.enemigo.EnemigoBase;
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
    private final static double velocidad = 5;
    private int aceleracionX;
    private int aceleracionY;

    private long tearRange;
    private long actualRange;

    private int damage;

    private int orientacion;
    private HashMap<String,Sprite> sprites = new HashMap<>();
    private String disparando = "DISPARANDO";
    private String desapareciendo = "DESAPARECIENDO";

    public int TIPO_DISPARO;
    public static final int DISPARO_NORMAL = 1;
    public static final int DISPARO_EXPLOSIVO = 2;

    public DisparoEnemigo(Context context, double xInicial, double yInicial, long tearRange, int damage, int orientacion, int sprite, int fps) {
        super(context, xInicial, yInicial, 32, 32);

        TIPO_DISPARO = DISPARO_NORMAL;

        cDerecha = 6;
        cIzquierda = 6;
        cArriba = 6;
        cAbajo = 6;

        if(orientacion == EnemigoBase.MOVIMIENTO_DERECHA){
            aceleracionY = 0;
            aceleracionX = (int)velocidad;
        }

        else if(orientacion == EnemigoBase.MOVIMIENTO_IZQUIERDA){
            aceleracionY = 0;
            aceleracionX = -(int)velocidad;
        }

        else if(orientacion == EnemigoBase.MOVIMIENTO_ABAJO){
            aceleracionX = 0;
            aceleracionY = (int)velocidad;
        }

        else if(orientacion == EnemigoBase.MOVIMIENTO_ARRIBA){
            aceleracionX = 0;
            aceleracionY = -(int)velocidad;
        }

        else if(orientacion == EnemigoBase.MOVIMIENTO_ABAJO_DERECHA){
            aceleracionX = (int)velocidad;
            aceleracionY = (int)velocidad;
        }

        else if(orientacion == EnemigoBase.MOVIMIENTO_ABAJO_IZQUIERDA){
            aceleracionX = -(int)velocidad;;
            aceleracionY = (int)velocidad;
        }

        else if(orientacion == EnemigoBase.MOVIMIENTO_ARRIBA_DERECHA){
            aceleracionX = (int)velocidad;
            aceleracionY = -(int)velocidad;
        }

        else if(orientacion == EnemigoBase.MOVIMIENTO_ARRIBA_IZQUIERDA){
            aceleracionX = -(int)velocidad;
            aceleracionY = -(int)velocidad;
        }

        this.orientacion = orientacion;

        this.tearRange = tearRange;
        this.damage = damage;
        actualRange = 0;

        estado = DISPARANDO;
        inicializar(sprite,fps);
    }

    private void inicializar (int sprite, int fps){
        Sprite disparo = new Sprite(CargadorGraficos.cargarDrawable(context, sprite), ancho, altura, fps, fps, true);
        sprites.put(disparando, disparo);

        this.sprite = sprites.get(disparando);
    }

    public void actualizar (long tiempo) {
        boolean finSprite = sprite.actualizar(tiempo);

        if(estado == FINALIZANDO){
            estado = FINALIZADO;
        }

        else if(estado == DISPARANDO){
            sprite = sprites.get(disparando);
        }

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

    public int getTipoModelo(){
        return Modelo.DISPARO;
    }
}
