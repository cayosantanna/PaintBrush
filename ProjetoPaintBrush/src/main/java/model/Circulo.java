/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.awt.Color;
import java.awt.Graphics;

public class Circulo extends D2 {
      public int raio;
      public static final float PI = 3.14f;
    
    @Override
    public float area() { return PI * raio * raio;}
    
    @Override
    public float perimetro() { return 2 * PI * raio;}
    
    @Override public void desenhar(Graphics g){
       g.setColor(corInterna);
       g.fillOval(x - raio, y - raio, 2 * raio, 2 * raio); /*desenhar o circulo preenchido*/
       g.setColor(cor);
       g.drawOval(x - raio, y - raio, 2 * raio, 2 * raio); /*desenhar o circulo não preenchido*/
       g.setColor(Color.BLACK);
       super.desenhar(g);
    }
}
