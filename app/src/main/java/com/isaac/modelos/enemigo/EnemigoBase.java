package com.isaac.modelos.enemigo;

import android.content.Context;
import android.graphics.Canvas;

import com.isaac.graficos.Sprite;
import com.isaac.modelos.Jugador;
import com.isaac.modelos.Modelo;
import com.isaac.modelos.disparos.DisparoEnemigo;
import com.isaac.modelos.nivel.Sala;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Pelayo on 29/11/2017.
 */

public abstract class EnemigoBase extends Modelo {
    public final static int ESTADO_VIVO = 0;
    public final static int ESTADO_MUERTO = 1;

    public static final int MOVIMIENTO_DERECHA = 0;
    public static final int MOVIMIENTO_IZQUIERDA = 1;
    public static final int MOVIMIENTO_ABAJO = 2;
    public static final int MOVIMIENTO_ARRIBA = 3;
    public static final int MOVIMIENTO_PARADO = 4;

    public int estado;

    protected Sprite spriteCabeza;
    protected Sprite spriteCuerpo;
    protected HashMap<String,Sprite> sprites = new HashMap<String,Sprite>();

    protected double xInicial;

    protected double yInicial;

    protected double aceleracionX;
    protected double aceleracionY;

    protected double HP;
    protected int movimiento;

    public EnemigoBase(Context context, double xInicial, double yInicial, int alturaCabezaCuerpo,int CabezaCuerpo,int tipoModelo) {
        super(context, xInicial, yInicial, alturaCabezaCuerpo, CabezaCuerpo, tipoModelo);
    }

    public double getxInicial() {
        return xInicial;
    }

    public void setxInicial(double xInicial) {
        this.xInicial = xInicial;
    }

    public double getyInicial() {
        return yInicial;
    }

    public void setyInicial(double yInicial) {
        this.yInicial = yInicial;
    }

    public double getAceleracionX() {
        return aceleracionX;
    }

    public void setAceleracionX(double aceleracionX) {
        this.aceleracionX = aceleracionX;
    }

    public double getAceleracionY() {
        return aceleracionY;
    }

    public void setAceleracionY(double aceleracionY) {
        this.aceleracionY = aceleracionY;
    }

    public double getHP() {
        return HP;
    }

    public void setHP(double HP) {
        this.HP = HP;
    }

    public int getTipoModelo(){
        return Modelo.ENEMIGO;
    }

    public int getMovimiento(){
        return movimiento;
    }

    public void setMovimiento(int movimiento){
        this.movimiento = movimiento;
    }

    public abstract void actualizar (long tiempo);

    public abstract void dibujar(Canvas canvas);

    public ArrayList<DisparoEnemigo> disparar(Jugador jugador){
        return new ArrayList<DisparoEnemigo>();
    }

}
