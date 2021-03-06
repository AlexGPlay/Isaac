package com.isaac.modelos.item.pickups;

import android.content.Context;

import com.isaac.R;
import com.isaac.gestores.GestorAudio;
import com.isaac.modelos.Jugador;
import com.isaac.modelos.item.Item;
import com.isaac.modelos.nivel.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Alex on 01/11/2017.
 */

public class Cofre extends Item {

    private int maxItems = 4;

    public Cofre(Context context, double x, double y) {
        super(context, x, y, 23, 30, R.drawable.cofre_normal);
    }

    @Override
    public void doStuff(Jugador jugador){}

    public List<Item> openChest(int orientacion){
        int numObjetos = (int)(Math.random()*maxItems)+1;
        List<Item> items = new ArrayList<>();

        for(int i=0;i<numObjetos;i++)
            items.add(generatePickUps(i, orientacion));

        GestorAudio.getInstancia().reproducirSonido(GestorAudio.PICK_COFRE);
        return items;
    }

    private Item generatePickUps(int i, int orientacion){
        int selectedPickUp = new Random().nextInt(PickupID.MAX_NUM-1)+1;

        int modX = (int)x;
        int modY = (int)y;

        if (orientacion == Jugador.MOVIMIENTO_DERECHA) {
            modX += 10 + ancho;
            modY = modY + (20*i);
        }

        else if (orientacion == Jugador.MOVIMIENTO_ABAJO) {
            modY += 10 + altura;
            modX = modX + (20*i);
        }

        else if(orientacion == Jugador.MOVIMIENTO_IZQUIERDA) {
            modX += -10;
            modY = modY + (20*i);
        }

        else if(orientacion == Jugador.MOVIMIENTO_ARRIBA) {
            modY += -10;
            modX = modX + (20*i);
        }

        switch (selectedPickUp){
            case PickupID.BOMBA:
                return new Bomba(context, modX, modY);

            case PickupID.LLAVE:
                return new Llave(context, modX, modY);

            case PickupID.MONEDA:
                return new Moneda(context, modX, modY);

            case PickupID.VIDA:
                return new Vida(context, modX, modY);

        }

        return null;
    }
}
