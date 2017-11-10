package com.isaac.modelos.nivel;

import android.content.Context;
import android.graphics.Canvas;

import com.isaac.modelos.Jugador;
import com.isaac.modelos.disparos.BombaActiva;
import com.isaac.modelos.disparos.DisparoJugador;
import com.isaac.modelos.enemigo.EnemigoMelee;
import com.isaac.modelos.item.Altar;
import com.isaac.modelos.item.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 05/11/2017.
 */

public class Sala_boss extends Sala{

    private List<Trampilla> trampillas;

    public Sala_boss(Context context, String tipoSala, Jugador jugador, Nivel nivel) throws Exception {
        super(context,tipoSala,jugador,nivel);
    }

    @Override
    public void dibujar(Canvas canvas){
        dibujarTiles(canvas);

        for( Puerta puerta : puertas.values() )
            puerta.dibujar(canvas);

        for(Trampilla trampilla : trampillas)
            trampilla.dibujar(canvas);

        jugador.dibujar(canvas);

        for(BombaActiva bomba : bombas)
            bomba.dibujar(canvas);

        for(EnemigoMelee enemigo : enemigos)
            enemigo.dibujar(canvas);

        for(DisparoJugador disparo : disparosJugador)
            disparo.dibujar(canvas);

    }

    @Override
    protected void aplicarReglasMovimiento() throws Exception {
        super.aplicarReglasMovimiento();
        aplicarReglasMovimientoTrampillas();
    }

    private void aplicarReglasMovimientoTrampillas() throws Exception {
        for(Trampilla trampilla : trampillas)
            if (jugador.colisiona(trampilla))
                trampilla.levelChange();
    }

    @Override
    public void checkEstadoSala(){
        super.checkEstadoSala();

        if(enemigos.size()<=0)
            for(Trampilla trampilla : trampillas)
                trampilla.activa = true;

    }

    public void addTrampilla(Trampilla trampilla){
        if(trampillas == null)
            trampillas = new ArrayList<>();

        trampillas.add(trampilla);
    }

    @Override
    public int getTipoSala(){
        return Sala.SALA_BOSS;
    }



}