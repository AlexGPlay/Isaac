package com.isaac.modelos.enemigo.monsters;

import android.content.Context;
import android.graphics.Canvas;

import com.isaac.R;
import com.isaac.gestores.CargadorGraficos;
import com.isaac.graficos.Sprite;
import com.isaac.modelos.Modelo;
import com.isaac.modelos.enemigo.EnemigoBase;
import com.isaac.modelos.enemigo.bosses.TheLambBody;
import com.isaac.modelos.enemigo.bosses.TheLambHead;
import com.isaac.modelos.nivel.Sala;

import java.util.ArrayList;

/**
 * Created by Pelayo on 08/12/2017.
 */

public class Spider extends EnemigoBase {
    private final static int altura = 44;
    private final static int ancho = 53;

    private boolean hasShot = false;

    private int spawnDelay;

    public final static String MOVIMIENTO = "moviendose";
    public final static String INVOCACION = "invocando";


    public Spider(Context context, double xInicial, double yInicial) {
        super(context, xInicial, yInicial, altura, ancho, Modelo.SOLIDO);

        comportamiento = ALEATORIO;

        this.x = xInicial;
        this.y = yInicial;
        spawnDelay = 0;

        aceleracionX=2;
        aceleracionY=2;


        HP = 15;

        estado = ESTADO_VIVO;

        inicializar();
    }

    public void inicializar(){
        Sprite movimiento = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.spider_baby_movimiento),
                ancho, altura,
                5, 5, true);
        Sprite invocacion =  new Sprite(CargadorGraficos.cargarDrawable(context, R.drawable.spider_baby_summon),
                ancho, altura,
                3, 3, true);
        spriteCuerpo=movimiento;

        sprites.put(MOVIMIENTO, movimiento);
        sprites.put(INVOCACION, invocacion);


    }
    @Override
    public void actualizar(long tiempo) {
        boolean finalizado = spriteCuerpo.actualizar(tiempo);

        if(getHP()<=0) {
            estado = ESTADO_MUERTO;
        }
        spawnDelay+=tiempo;
    }



    @Override
    public void dibujar(Canvas canvas) {
        spriteCuerpo.dibujarSprite(canvas,(int)x- Sala.scrollEjeX,(int)y - Sala.scrollEjeY);
    }
    @Override
    public ArrayList<EnemigoBase> summon(){
        ArrayList<EnemigoBase> toAdd = new ArrayList<>();


        if(spawnDelay>=5000) {
            spriteCuerpo= sprites.get(INVOCACION);
            toAdd.add(new BabySpider(context, x+aceleracionX*20, y+aceleracionY*20));
            spawnDelay=0;
        }
        else
            spriteCuerpo = sprites.get(MOVIMIENTO);
        return toAdd;
    }

}
