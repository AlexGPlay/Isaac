package com.isaac.gestores;

import android.content.Context;
import android.util.Log;

import com.isaac.R;
import com.isaac.modelos.Tile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Alex on 24/10/2017.
 */

public class CargadorSalas {

    private static Context context;
    private static int anchoMapa;
    private static int altoMapa;

    private CargadorSalas(){}

    public static void setContext(Context contexto){
        context = contexto;
    }

    public static Tile[][] inicializarMapaTiles(String numeroNivel) throws Exception {
        InputStream is = context.getAssets().open(numeroNivel+".txt");
        int anchoLinea;

        List<String> lineas = new LinkedList<String>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        {
            String linea = reader.readLine();
            anchoLinea = linea.length();
            while (linea != null)
            {
                lineas.add(linea);
                if (linea.length() != anchoLinea)
                {
                    Log.e("ERROR", "Dimensiones incorrectas en la línea");
                    throw new Exception("Dimensiones incorrectas en la línea.");
                }
                linea = reader.readLine();
            }
        }

        // Inicializar la matriz
        Tile[][] mapaTiles = new Tile[anchoLinea][lineas.size()];
        anchoMapa = anchoLinea;
        altoMapa = lineas.size();
        // Iterar y completar todas las posiciones
        for (int y = 0; y < mapaTiles[0].length; ++y) {
            for (int x = 0; x < mapaTiles.length; ++x) {
                char tipoDeTile = lineas.get(y).charAt(x);//lines[y][x];
                mapaTiles[x][y] = inicializarTile(tipoDeTile,x,y);
            }
        }

        return mapaTiles;
    }

    private static Tile inicializarTile(char codigoTile,int x, int y) {
        switch (codigoTile) {
            case '#':
                // en blanco, sin textura
                if(x==0 && y==0)
                    return new Tile(CargadorGraficos.cargarDrawable(context,
                            R.drawable.pared_0_0), Tile.SOLIDO);

                else if(x==0 && y==altoMapa-1)
                    return new Tile(CargadorGraficos.cargarDrawable(context,
                            R.drawable.pared_l_0), Tile.SOLIDO);

                else if(x==anchoMapa-1 && y==0)
                    return new Tile(CargadorGraficos.cargarDrawable(context,
                            R.drawable.pared_0_a), Tile.SOLIDO);

                else if(x==anchoMapa-1 && y==altoMapa-1)
                    return new Tile(CargadorGraficos.cargarDrawable(context,
                            R.drawable.pared_l_a), Tile.SOLIDO);

                else if(x==0 || x==anchoMapa-1)
                    return new Tile(CargadorGraficos.cargarDrawable(context,
                            R.drawable.pared_2), Tile.SOLIDO);

                else
                    return new Tile(CargadorGraficos.cargarDrawable(context,
                            R.drawable.pared_1), Tile.SOLIDO);

            case '.':
                // bloque de musgo, no se puede pasar
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_1), Tile.PASABLE);
            default:
                //cualquier otro caso
                return new Tile(null, Tile.PASABLE);
        }
    }





}
