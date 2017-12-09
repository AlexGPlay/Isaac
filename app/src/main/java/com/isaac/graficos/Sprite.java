package com.isaac.graficos;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class Sprite {
    private Bitmap bitmap;
    private Bitmap tempBitmap;
    private boolean tempDraw;
    private boolean alreadyColored;

    // Fichero con los frames.
    private Rect rectanguloDibujo;
    // El rectangulo sobre el que se pinta el dibujo
    private int framesTotales;
    // N�mero total de frames en el bitmap.
    private int frameActual;
    // El frame que se esta pintando actualmente
    private long tiempoUltimaActualizacion;
    // El tiempo que ha pasado desde que se ha cambiado de frame
    private int interavaloEntreFrames;
    // Milisegundos, tiempo entre frames (1000/fps)

    // Medidas reales del Bitmap del sprite, el .png.
    private int spriteAncho;
    private int spriteAltura;

    // Medidas en pixeles del modelo que se representara en la pantalla del dispositivo
    private int modeloAncho;
    private int modeloAltura;

    private boolean bucle;

    public Sprite(Drawable drawable, int modeloAncho, int modeloAltura, int fps, int framesTotales
            , boolean bucle) {
        this.bitmap = ((BitmapDrawable)drawable).getBitmap();
        this.modeloAncho = modeloAncho;
        this.modeloAltura = modeloAltura;
        this.framesTotales = framesTotales;
        this.bucle = bucle;

        frameActual = 0;
        spriteAncho = bitmap.getWidth() / framesTotales;
        spriteAltura = bitmap.getHeight();
        rectanguloDibujo = new Rect(0, 0, spriteAncho, spriteAltura);
        interavaloEntreFrames = 1000 / fps;
        tiempoUltimaActualizacion = 0l;

        alreadyColored = false;
        tempDraw = false;
    }

    public void colorize(int dstColor) {

        if(!alreadyColored) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            float srcHSV[] = new float[3];
            float dstHSV[] = new float[3];

            Bitmap dstBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    int pixel = bitmap.getPixel(col, row);
                    int alpha = Color.alpha(pixel);
                    Color.colorToHSV(pixel, srcHSV);
                    Color.colorToHSV(dstColor, dstHSV);

                    // If it area to be painted set only value of original image
                    dstHSV[2] = srcHSV[2];  // value
                    dstBitmap.setPixel(col, row, Color.HSVToColor(alpha, dstHSV));
                }
            }
            tempBitmap = dstBitmap;
            alreadyColored = true;
        }

        tempDraw = true;

    }

    public boolean actualizar (long tiempo) {
        boolean finSprite = false;
        // Tiempo es el tiempo que pasa entre cada frame.
        tiempoUltimaActualizacion += tiempo;
        if (tiempoUltimaActualizacion >= interavaloEntreFrames) {
            tiempoUltimaActualizacion = 0;
            // actualizar el frame
            frameActual++;
            if (frameActual >= framesTotales) {
                if (bucle){
                    frameActual = 0;
                } else {
                    frameActual = framesTotales;
                    finSprite = true;
                }
            }
        }
        // definir el rectangulo
        this.rectanguloDibujo.left = frameActual * spriteAncho;
        this.rectanguloDibujo.right = this.rectanguloDibujo.left + spriteAncho;

        return finSprite;
    }


    public void dibujarSprite (Canvas canvas, int x, int y) {
        if(!tempDraw) {
            Rect destRect = new Rect(x - modeloAncho / 2, y - modeloAltura / 2, x
                    + modeloAncho / 2, y + modeloAltura / 2);
            canvas.drawBitmap(bitmap, rectanguloDibujo, destRect, null);
        }
        else{
            Rect destRect = new Rect(x - modeloAncho / 2, y - modeloAltura / 2, x
                    + modeloAncho / 2, y + modeloAltura / 2);
            canvas.drawBitmap(tempBitmap, rectanguloDibujo, destRect, null);
            tempDraw = false;
        }
    }

    public void setFrameActual(int frameActual) {
        this.frameActual = frameActual;
    }
}
