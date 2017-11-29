package com.isaac.modelos.enemigo;

import android.content.Context;
import android.graphics.Canvas;

import com.isaac.graficos.Sprite;
import com.isaac.modelos.Modelo;
import com.isaac.modelos.nivel.Sala;

import java.util.HashMap;

/**
 * Created by Pelayo on 29/11/2017.
 */

public abstract class EnemigoBase extends Modelo {
    public final static int ESTADO_VIVO = 0;
    public final static int ESTADO_MUERTO = 1;

    public int estado;

    protected Sprite spriteCabeza;
    protected Sprite spriteCuerpo;
    protected HashMap<String,Sprite> sprites = new HashMap<String,Sprite>();

    protected double xInicial;

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

    protected double yInicial;

    protected double aceleracionX;
    protected double aceleracionY;

    private final static int alturaCabeza = 25;
    private final static int anchoCabeza = 29;
    private final static int alturaCuerpo = 14;
    private final static int anchoCuerpo = 32;

    public double getHP() {
        return HP;
    }

    public void setHP(double HP) {
        this.HP = HP;
    }

    protected double HP;

    public EnemigoBase(Context context, double xInicial, double yInicial, int alturaCabezaCuerpo,int CabezaCuerpo,int modelo) {
        super(context, 0, 0, alturaCabeza+alturaCuerpo, Math.max(anchoCabeza,anchoCuerpo), Modelo.SOLIDO);

    }
    public void actualizar (long tiempo) {

    }

    public void dibujar(Canvas canvas){

    }
}
