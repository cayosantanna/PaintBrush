/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.awt.Graphics;

public class Retangulo extends D2 {
    public int base, largura;
    
    @Override
    public float area() { return base * largura;}
    
    @Override
    public float perimetro() { return 2 * (base + largura); }
    
    @Override public void desenhar(Graphics g){
        
        super.desenhar(g); 
        java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
        g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(corInterna);
        g2.fillRect(super.x, super.y, base, largura);/*desenha o retangulo preenchido*/
        g2.setColor(cor);
        g2.drawRect(super.x, super.y, base, largura);/*desenha o retangulo sem preenchimento*/
        if(exibirArea){
            g2.drawString("Área: " + area(), super.x + base/2 - 20, super.y + largura/2);
        }
    }
}

