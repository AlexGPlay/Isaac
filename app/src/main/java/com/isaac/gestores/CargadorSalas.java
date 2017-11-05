package com.isaac.gestores;

import android.content.Context;
import android.util.Log;

import com.isaac.R;
import com.isaac.modelos.item.Altar;
import com.isaac.modelos.nivel.Puerta;
import com.isaac.modelos.nivel.Sala;
import com.isaac.modelos.nivel.Sala_boss;
import com.isaac.modelos.nivel.Sala_tesoro;
import com.isaac.modelos.nivel.Tile;
import com.isaac.modelos.nivel.Trampilla;

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

    private static Sala salaTemp;

    private CargadorSalas(){}

    public static void setContext(Context contexto){
        context = contexto;
    }

    public static Tile[][] inicializarMapaTiles(String numeroNivel, Sala sala) throws Exception {
        InputStream is = context.getAssets().open(numeroNivel+".txt");
        int anchoLinea;
        salaTemp = sala;

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
                return getPared(x,y);

            case '.':
                return getSuelo();

            case 'P':
                return getPuerta(x,y);

            case 'I':
                return getAltar(x,y);

            case 'T':
                return getTrampilla(x,y);

            default:
                return new Tile(null, Tile.PASABLE);
        }
    }

    private static Tile getTrampilla(int x, int y){
        Sala_boss temp = (Sala_boss)salaTemp;
        temp.addTrampilla(new Trampilla(context,x*Tile.ancho,y*Tile.altura,salaTemp.nivel));

        return getSuelo();
    }

    private static Tile getAltar(int x, int y){
        Sala_tesoro temp = (Sala_tesoro)salaTemp;
        temp.addAltar(new Altar(context,x*Tile.ancho,y*Tile.altura,salaTemp.nivel));

        return getSuelo();
    }

    private static Tile getPuerta(int x, int y){
        if(x==0) {
            Puerta temp = new Puerta(context, Tile.ancho * x + Tile.ancho / 2, Tile.altura * y + Tile.altura / 2, 40, 40, R.drawable.puerta_sala_izquierda, R.drawable.puerta_sala_izquierda_cerrada);
            temp.setTile(x+1,y);
            temp.setTileDoor(x,y);
            salaTemp.addPuerta(Sala.PUERTA_IZQUIERDA, temp);
        }

        else if(y==0) {
            Puerta temp = new Puerta(context, Tile.ancho * x + Tile.ancho / 2, Tile.altura * y + Tile.altura / 2, 32, 40, R.drawable.puerta_sala_arriba, R.drawable.puerta_sala_arriba_cerrada);
            temp.setTile(x,y+1);
            temp.setTileDoor(x,y);
            salaTemp.addPuerta(Sala.PUERTA_ARRIBA, temp);
        }

        else if(x==anchoMapa-1) {
            Puerta temp = new Puerta(context, Tile.ancho * x + Tile.ancho / 2, Tile.altura * y + Tile.altura / 2, 40, 40, R.drawable.puerta_sala_derecha, R.drawable.puerta_sala_derecha_cerrada);
            temp.setTile(x-1,y);
            temp.setTileDoor(x,y);
            salaTemp.addPuerta(Sala.PUERTA_DERECHA, temp);
        }

        else if(y==altoMapa-1) {
            Puerta temp = new Puerta(context, Tile.ancho * x + Tile.ancho / 2, Tile.altura * y + Tile.altura / 2, 32, 40, R.drawable.puerta_sala_abajo, R.drawable.puerta_sala_abajo_cerrada);
            temp.setTile(x,y-1);
            temp.setTileDoor(x,y);
            salaTemp.addPuerta(Sala.PUERTA_ABAJO, temp);
        }

        Tile tile = getPared(x,y);
        tile.tipoDeColision = Tile.SOLIDO;

        return tile;
    }

    private static Tile getPared(int x, int y){
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

        else if(x==0)
            return new Tile(CargadorGraficos.cargarDrawable(context,
                    R.drawable.pared_vertical), Tile.SOLIDO);

        else if(x==anchoMapa-1)
            return new Tile(CargadorGraficos.cargarDrawable(context,
                    R.drawable.pared_vertical_1), Tile.SOLIDO);

        else if(y==0)
            return new Tile(CargadorGraficos.cargarDrawable(context,
                    R.drawable.pared_horizontal), Tile.SOLIDO);

        else if(y==altoMapa-1)
            return new Tile(CargadorGraficos.cargarDrawable(context,
                    R.drawable.pared_horizontal_1), Tile.SOLIDO);


        return null;
    }

    private static Tile getSuelo(){
        int suelo = (int)(Math.random()*14);

        switch(suelo){
            case 0:
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_1), Tile.PASABLE);

            case 1:
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_2), Tile.PASABLE);

            case 2:
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_3), Tile.PASABLE);

            case 3:
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_4), Tile.PASABLE);

            case 4:
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_5), Tile.PASABLE);

            case 5:
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_6), Tile.PASABLE);

            case 6:
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_7), Tile.PASABLE);

            case 7:
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_8), Tile.PASABLE);

            case 8:
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_9), Tile.PASABLE);

            case 9:
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_10), Tile.PASABLE);

            case 10:
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_11), Tile.PASABLE);

            case 11:
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_12), Tile.PASABLE);

            case 12:
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_13), Tile.PASABLE);

            case 13:
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_14), Tile.PASABLE);

            case 14:
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_15), Tile.PASABLE);
        }

        return null;
    }





}
