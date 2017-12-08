package com.isaac.modelos.nivel;

import android.content.Context;
import android.graphics.Canvas;

import com.isaac.R;
import com.isaac.gestores.GestorAudio;
import com.isaac.modelos.Jugador;
import com.isaac.modelos.disparos.BombaActiva;
import com.isaac.modelos.disparos.DisparoEnemigo;
import com.isaac.modelos.disparos.DisparoJugador;
import com.isaac.modelos.enemigo.EnemigoBase;
import com.isaac.modelos.enemigo.bosses.DukeOfFlies;
import com.isaac.modelos.enemigo.bosses.TheLamb;

import java.util.ArrayList;
import java.util.Iterator;
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
    protected void addEnemies(){
        enemigos.add(new DukeOfFlies(context,  anchoMapaTiles()*Tile.ancho/2, altoMapaTiles()*Tile.altura/2));
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

    @Override
    public void generatePickUps(){ }

    public void addTrampilla(Trampilla trampilla){
        if(trampillas == null)
            trampillas = new ArrayList<>();

        trampillas.add(trampilla);
    }

    @Override
    public int getTipoSala(){
        return Sala.SALA_BOSS;
    }

    @Override
    protected void modelsInExplosion(BombaActiva bomba){

        if( checkExplosion(bomba, jugador) )
            jugador.takeDamage(1);

        for(EnemigoBase enemigo : enemigos)
            if( checkExplosion(bomba, enemigo) )
                enemigo.takeDamage(bomba.getDaño(), bomba);

        for(Iterator<Roca> iterator = rocas.iterator(); iterator.hasNext();) {
            Roca roca = iterator.next();
            if (checkExplosion(bomba, roca)) {
                iterator.remove();
                continue;
            }
        }

    }

}
