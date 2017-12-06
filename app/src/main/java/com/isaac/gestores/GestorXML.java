package com.isaac.gestores;

import android.content.Context;

import com.isaac.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GestorXML {
    private static GestorXML instancia = null;

    private GestorXML(){}

    public static GestorXML getInstance(){
        if(instancia == null){
            instancia = new GestorXML();
        }

        return instancia;
    }

    public List<Integer> getItemPool(Context context) {

        ParserXML parser = new ParserXML();
        String textoFicheroNivel = "";
        try {
            InputStream inputStream = context.getResources().openRawResource(R.raw.itempool);
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream));
            String linea = bufferedReader.readLine();
            while (linea != null) {
                textoFicheroNivel += linea;
                linea = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Document doc = parser.getDom(textoFicheroNivel);

        List<Integer> itemPool = new ArrayList<>();
        NodeList nodos = doc.getElementsByTagName("item");
        for (int i = 0; i < nodos.getLength(); i++) {
            Element elementoActual = (Element) nodos.item(i);
            int id = Integer.valueOf( parser.getValor(elementoActual, "id") );
            itemPool.add( id );

        }
        return itemPool;
    }

    public List<Integer> getEnemyPools(Context context, int pool){
        ParserXML parser = new ParserXML();
        String textoFicheroNivel = "";

        try {
            InputStream inputStream = context.getResources().openRawResource(pool);
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream));
            String linea = bufferedReader.readLine();
            while (linea != null) {
                textoFicheroNivel += linea;
                linea = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Document doc = parser.getDom(textoFicheroNivel);

        List<Integer> enemyPool = new ArrayList<>();
        NodeList nodos = doc.getElementsByTagName("monster");
        for (int i = 0; i < nodos.getLength(); i++) {
            Element elementoActual = (Element) nodos.item(i);
            int id = Integer.valueOf( parser.getValor(elementoActual, "id") );
            enemyPool.add( id );

        }
        return enemyPool;
    }

}
