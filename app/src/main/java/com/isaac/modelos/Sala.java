package com.isaac.modelos;

import android.graphics.Canvas;

import com.isaac.GameView;
import com.isaac.gestores.CargadorSalas;

/**
 * Created by Alex on 24/10/2017.
 */

public class Sala{

    public static final String SALA_CUADRADA_1 = "Sala_cuadrada_1";

    private Tile[][] mapaTiles;
    private Jugador jugador;

    public Sala(String tipoSala) throws Exception {
        mapaTiles = CargadorSalas.inicializarMapaTiles(tipoSala);
    }

    public void moveToRoom(Jugador jugador){
        this.jugador = jugador;
    }

    public void actualizar(long time){
        jugador.actualizar(time);
    }

    public void dibujar(Canvas canvas){
        dibujarTiles(canvas);
        jugador.dibujar(canvas);
    }

    private void dibujarTiles(Canvas canvas){
        // Calcular que tiles serán visibles en la pantalla
        // La matriz de tiles es más grande que la pantalla

        int izquierda = 0; //El primer tile

        int derecha = izquierda +
                ( GameView.pantallaAncho / Tile.ancho ) + 1;

        // el ultimo tile visible
        derecha = Math.min(derecha, mapaTiles.length - 1);

        for (int y = 0; y < mapaTiles[0].length ; ++y) {
            for (int x = izquierda; x <= derecha; ++x) {
                if (mapaTiles[x][y].imagen != null) {
                    // Calcular la posición en pantalla correspondiente
                    // izquierda, arriba, derecha , abajo

                    mapaTiles[x][y].imagen.setBounds(
                            x  * Tile.ancho,
                            y * Tile.altura,
                            x * Tile.ancho + Tile.ancho,
                            y * Tile.altura + Tile.altura);

                    mapaTiles[x][y].imagen.draw(canvas);
                }
            }
        }
    }

}
