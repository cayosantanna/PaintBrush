/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.awt.Graphics;

public class Circulo extends D2 {
      public int raio;
      public static final float PI = 3.14f;
    
    @Override
    public float area() { return PI * raio * raio;}
    
    @Override
    public float perimetro() { return 2 * PI * raio;}
    
    @Override public void desenhar(Graphics g){
        java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
        g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(corInterna);
        g2.fillOval(x - raio, y - raio, 2 * raio, 2 * raio);
        g2.setColor(cor);
        g2.drawOval(x - raio, y - raio, 2 * raio, 2 * raio);
        if (exibirArea) {
            g2.drawString(String.format("Área: %.2f", area()), x - raio / 2, y);
        }
        super.desenhar(g);
    }
}
