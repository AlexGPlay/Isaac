package com.isaac.modelos;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

public class Modelo {

    public static final int PASABLE = 0;
    public static final int SOLIDO = 1;
    public static final int MOVIBLE = 2;

    public static final int NO_COLISION = 0;
    public static final int COLISION_TILE = 1;
    public static final int COLISION_MODELO = 2;

    public int colision;

    protected Context context;
    protected double x;
    protected double y;
    protected int altura;
    protected int ancho;

    protected Drawable imagen;

    protected int cDerecha;
    protected int cIzquierda;
    protected int cArriba;
    protected int cAbajo;

    public Modelo(Context context, double x, double y, int altura, int ancho){
        this(context,x,y,altura,ancho,PASABLE);
    }

    public Modelo(Context context, double x, double y, int altura, int ancho, int colision){
        this.context = context;
        this.x = x;
        this.y = y;
        this.altura = altura;
        this.ancho = ancho;

        cDerecha = ancho/2;
        cIzquierda = ancho/2;
        cArriba = altura/2;
        cAbajo = altura/2;

        this.colision = colision;
    }

    public void dibujar(Canvas canvas){
        int yArriba = (int)  y - altura / 2;
        int xIzquierda = (int) x - ancho / 2;

        imagen.setBounds(xIzquierda, yArriba, xIzquierda
                + ancho, yArriba + altura);
        imagen.draw(canvas);
    }

    public void actualizar (long tiempo){}

    public boolean colisiona (Modelo modelo){
        boolean colisiona = false;

        if (modelo.x - modelo.cIzquierda / 2 <= (x + cDerecha)
                && (modelo.x + modelo.cDerecha / 2) >= (x - cIzquierda)
                && (y + cAbajo) >= (modelo.y - modelo.cArriba)
                && (y - cArriba) < (modelo.y + modelo.cAbajo)) {

            colisiona = true;
        }
        return colisiona;
    }

    public boolean colisionanCoordenadas(Modelo modelo, int x, int y){
        boolean colisiona = false;

        if (x - modelo.cIzquierda / 2 <= (this.x + cDerecha)
                && (x + modelo.cDerecha / 2) >= (this.x - cIzquierda)
                && (this.y + cAbajo) >= (y - modelo.cArriba)
                && (this.y - cArriba) < (y + modelo.cAbajo)) {

            colisiona = true;
        }
        return colisiona;
    }

    public boolean colisionanCoordenadas(int x, int y){
        boolean colisiona = false;

        if (x <= (this.x + cDerecha)
                && (x) >= (this.x - cIzquierda)
                && (this.y + cAbajo) >= (y)
                && (this.y - cArriba) < (y)) {

            colisiona = true;
        }
        return colisiona;
    }

    public double getX(){
        return x;
    }

    public void setX(double x){
        this.x = x;
    }

    public double getY(){
        return y;
    }

    public void setY(double y){
        this.y = y;
    }

    public int getAltura(){
        return altura;
    }

    public void setAltura(int altura){
        this.altura = altura;
    }

    public int getAncho(){
        return ancho;
    }

    public void setAncho(int ancho){
        this.ancho = ancho;
    }

}


