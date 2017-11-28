package com.isaac.modelos.nivel;

import android.content.Context;
import android.graphics.Canvas;

import com.isaac.gestores.CargadorGraficos;
import com.isaac.modelos.Modelo;

/**
 * Created by Alejandro García Parrondo on 12/11/2017.
 */

public class PuertaLlave extends Puerta{

    private int llavesNecesarias;
    protected int puertaLlave;

    public PuertaLlave(Context context, double x, double y, int alto, int ancho, int puertaAbierta, int puertaCerrada, int puertaLlave) {
        super(context, x, y, alto, ancho, puertaAbierta, puertaCerrada);

        this.colision = Modelo.SOLIDO;

        llavesNecesarias = 1;
        this.puertaLlave = puertaLlave;
    }

    public PuertaLlave(Context context, Puerta puerta, int puertaLlave){
        super(context, puerta.getX(), puerta.getY(), puerta.getAltura(), puerta.getAncho(), puerta.puertaAbierta, puerta.puertaCerrada);
        this.puertaLlave = puertaLlave;

        setTile(puerta.getXSalida(), puerta.getYSalida());
        setTileDoor(puerta.getXEntrada(), puerta.getYEntrada());

        this.colision = Modelo.SOLIDO;

        llavesNecesarias = 1;
    }

    public void insertKey(){
        if(llavesNecesarias>0)
            llavesNecesarias--;

        if(llavesNecesarias==0) {
            setAbierta(true);
            this.colision = Modelo.PASABLE;

        }
    }

    public int getLlavesNecesarias(){
        return llavesNecesarias;
    }

    @Override
    public void setForzada(boolean forzada){}

    @Override
    public void dibujar(Canvas canvas) {
        if (isAbierta())
            imagen = CargadorGraficos.cargarDrawable(context, puertaAbierta);

        else if(!isAbierta() && llavesNecesarias==0)
            imagen = CargadorGraficos.cargarDrawable(context, puertaCerrada);

        else
            imagen = CargadorGraficos.cargarDrawable(context, puertaLlave);

        int yArriba = (int) y - altura / 2 - Sala.scrollEjeY;
        int xIzquierda = (int) x - ancho / 2 - Sala.scrollEjeX;

        imagen.setBounds(xIzquierda, yArriba, xIzquierda
                + ancho, yArriba + altura);
        imagen.draw(canvas);
    }

}
