package com.isaac.modelos.enemigo.bosses;

import android.content.Context;
import android.graphics.Canvas;

import com.isaac.R;
import com.isaac.gestores.CargadorGraficos;
import com.isaac.graficos.Sprite;
import com.isaac.modelos.Jugador;
import com.isaac.modelos.Modelo;
import com.isaac.modelos.disparos.DisparoEnemigo;
import com.isaac.modelos.disparos.DisparoExplosivo;
import com.isaac.modelos.enemigo.EnemigoBase;
import com.isaac.modelos.enemigo.monsters.Fly;
import com.isaac.modelos.nivel.Sala;

import java.util.ArrayList;

/**
 * Created by Pelayo on 08/12/2017.
 */

public class DukeOfFlies extends EnemigoBase {
    //VARIABLES GRAFICAS
    protected static final String MOVIMIENTO_NORMAL = "movimiento";

    private final static int altura = 64;
    private final static int ancho = 80;
    private int spawn;

    public DukeOfFlies (Context context, double xInicial, double yInicial) {
        super(context, xInicial, yInicial, altura, ancho, Modelo.SOLIDO);

        comportamiento = ALEATORIO;

        spawn =2000;
        this.xInicial = xInicial;
        this.yInicial = yInicial - altura/2;

        this.x =  this.xInicial;
        this.y =  this.yInicial;

        aceleracionX = 1;
        aceleracionY = 1;
        fly=true;


        HP = 300;

        inicializar();
    }

    public void inicializar(){

        Sprite movimiento = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.duke_of_flies_movimiento),
                ancho, altura,
                4, 4, true);
        sprites.put(MOVIMIENTO_NORMAL, movimiento);



        spriteCuerpo = movimiento;
    }

    @Override
    public void actualizar(long tiempo) {
        boolean finalizado = spriteCuerpo.actualizar(tiempo);

        spawn+=tiempo;
        if(HP<=0){
            estado= ESTADO_MUERTO;
        }

    }



    @Override
    public void dibujar(Canvas canvas) {
        spriteCuerpo.dibujarSprite(canvas,(int)x- Sala.scrollEjeX,(int)y - Sala.scrollEjeY);
    }

    @Override
    public ArrayList<EnemigoBase> summon(){
        ArrayList<EnemigoBase> toAdd = new ArrayList<>();

        if(spawn>=2000){
            toAdd.add(new Fly(context, x, y));
            spawn=0;

        }

        return toAdd;
    }

}
