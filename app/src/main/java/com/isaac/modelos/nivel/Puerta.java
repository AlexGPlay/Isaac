package com.isaac.modelos.nivel;

import android.content.Context;
import android.graphics.Canvas;

import com.isaac.gestores.CargadorGraficos;
import com.isaac.modelos.Modelo;

/**
 * Created by Alex on 25/10/2017.
 */

public class Puerta extends Modelo {

    private boolean abierta;
    private boolean forzada;

    private int xSalida, ySalida;
    private int xEntrada, yEntrada;

    protected int puertaAbierta;
    protected int puertaCerrada;

    public Puerta(Context context, double x, double y, int alto, int ancho, int puertaAbierta, int puertaCerrada) {
        super(context, x, y, alto, ancho);

        this.puertaAbierta = puertaAbierta;
        this.puertaCerrada = puertaCerrada;

        imagen = CargadorGraficos.cargarDrawable(context, puertaAbierta);
        abierta = true;
        forzada = false;
    }

    public void setTile(int x, int y){
        this.xSalida = x;
        this.ySalida = y;
    }

    public void setTileDoor(int x, int y){
        this.xEntrada = x;
        this.yEntrada = y;
    }

    public int getXSalida(){
        return xSalida;
    }

    public int getYSalida(){
        return ySalida;
    }

    public int getYEntrada() { return yEntrada; }

    public int getXEntrada() { return xEntrada; }

    public boolean isAbierta(){
        return abierta;
    }

    public void setAbierta(boolean abierta){
        this.abierta = abierta;
    }

    public boolean isForzada(){
        return forzada;
    }

    public void setForzada(boolean forzada){
        this.forzada = forzada;
    }

    @Override
    public void dibujar(Canvas canvas){
        if(abierta)
            imagen = CargadorGraficos.cargarDrawable(context, puertaAbierta);

        else
            imagen = CargadorGraficos.cargarDrawable(context, puertaCerrada);

        int yArriba = (int)  y - altura / 2 - Sala.scrollEjeY;
        int xIzquierda = (int) x - ancho / 2 - Sala.scrollEjeX;

        imagen.setBounds(xIzquierda, yArriba, xIzquierda
                + ancho, yArriba + altura);
        imagen.draw(canvas);
    }

}
