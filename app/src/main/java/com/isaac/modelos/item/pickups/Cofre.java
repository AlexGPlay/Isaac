package com.isaac.modelos.item.pickups;

import android.content.Context;

import com.isaac.R;
import com.isaac.modelos.Jugador;
import com.isaac.modelos.item.Item;
import com.isaac.modelos.nivel.Tile;

import java.util.ArrayList;
import java.util.List;

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

    public List<Item> openChest(){
        int numObjetos = (int)(Math.random()*4)+1;
        List<Item> items = new ArrayList<>();

        for(int i=0;i<numObjetos;i++)
            items.add(generatePickUps(i));

        return items;
    }

    private Item generatePickUps(int i){
        int selectedPickUp = (int)(Math.random()* (PickupID.MAX_NUM+1));
        Item item = null;

        switch (selectedPickUp){
            case PickupID.BOMBA:
                item = new Bomba(context, (x+10)*(i+1), (y+10)*(i+1));
                break;

            case PickupID.LLAVE:
                item = new Llave(context, (x+10)*(i+1), (y+10)*(i+1));
                break;

            case PickupID.MONEDA:
                item = new Moneda(context, (x+10)*(i+1), (y+10)*(i+1));
                break;

            case PickupID.VIDA:
                item = new Vida(context, (x+10)*(i+1), (y+10)*(i+1));
                break;

        }

        return item;
    }
}
