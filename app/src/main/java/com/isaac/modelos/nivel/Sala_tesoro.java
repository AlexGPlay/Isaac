package com.isaac.modelos.nivel;

import android.content.Context;
import android.graphics.Canvas;

import com.isaac.gestores.CargadorSalas;
import com.isaac.modelos.Jugador;
import com.isaac.modelos.disparos.BombaActiva;
import com.isaac.modelos.disparos.DisparoEnemigo;
import com.isaac.modelos.disparos.DisparoJugador;
import com.isaac.modelos.enemigo.EnemigoBase;
import com.isaac.modelos.enemigo.EnemigoMelee;
import com.isaac.modelos.item.Altar;
import com.isaac.modelos.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by alexgp1234 on 29/10/17.
 */

public class Sala_tesoro extends Sala{

    private List<Altar> altares;

    public Sala_tesoro(Context context, String tipoSala, Jugador jugador, Nivel nivel) throws Exception {
        super(context,tipoSala,jugador,nivel);

        itemsDropped = true;
        enemigos.clear();
    }

    public void addAltar(Altar altar){
        if(altares==null)
            altares = new ArrayList<>();

        itemsDropped = true;
        altares.add(altar);
    }

    @Override
    public void dibujar(Canvas canvas){
        dibujarTiles(canvas);

        for( Puerta puerta : puertas.values() )
            puerta.dibujar(canvas);

        for(Altar altar : altares)
            altar.dibujar(canvas);

        jugador.dibujar(canvas);

        for(BombaActiva bomba : bombas)
            bomba.dibujar(canvas);

        for(EnemigoBase enemigo : enemigos)
            enemigo.dibujar(canvas);

        for(DisparoJugador disparo : disparosJugador)
            disparo.dibujar(canvas);

        for(DisparoEnemigo disparo : disparosEnemigo)
            disparo.dibujar(canvas);

    }

    @Override
    protected void aplicarReglasMovimiento() throws Exception {
        super.aplicarReglasMovimiento();
        aplicarReglasMovimientoAltares();
    }

    private void aplicarReglasMovimientoAltares(){
        for(Altar altar : altares) {
            if (jugador.colisiona(altar)){
                Item item = altar.pickItem();

                if(item!=null)
                    item.doStuff(jugador);
            }
        }
    }

    @Override
    public int getTipoSala(){
        return Sala.SALA_DORADA;
    }

}
