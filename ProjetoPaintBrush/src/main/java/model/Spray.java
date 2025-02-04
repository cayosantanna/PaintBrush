/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

public class Spray extends Ponto {
    public int densidade = 20;
    public int raio = 8;
    
    private static BufferedImage patternBuffer;
    private static Color lastColor;
    private int lastX = -1;
    private int lastY = -1;
    private int acumulador = 0;
    private static final int MAX_DENSIDADE = 100;
    
    private void atualizarPattern() {
        patternBuffer = new BufferedImage(raio*2, raio*2, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = patternBuffer.createGraphics();
        g2.setColor(cor);
        
        for(int i = 0; i < densidade; i++) {
            int dx = ThreadLocalRandom.current().nextInt(raio*2);
            int dy = ThreadLocalRandom.current().nextInt(raio*2);
            double distancia = Math.sqrt(Math.pow(dx - raio, 2) + Math.pow(dy - raio, 2));
            if(distancia < raio) {
                patternBuffer.setRGB(dx, dy, cor.getRGB());
            }
        }
        g2.dispose();
    }
    
    public void setRaio(int novoRaio) {
        if (novoRaio != raio) {
            raio = novoRaio;
            densidade = raio * 2; // Ajusta densidade proporcionalmente ao raio
            patternBuffer = null; // Força recriação do pattern
        }
    }
    
    @Override
    public void desenhar(Graphics g) {
        // Se está na mesma posição, aumenta densidade
        if(x == lastX && y == lastY && acumulador < MAX_DENSIDADE) {
            densidade += 5;
            acumulador += densidade;
        } else {
            densidade = 20; // Reset densidade quando move
            acumulador = 0;
        }
        
        atualizarPattern();
        g.drawImage(patternBuffer, x-raio, y-raio, null);
        
        lastX = x;
        lastY = y;
    }
}
