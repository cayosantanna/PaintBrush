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
        g.setColor (corInterna);
        g.fillRect(super.x, super.y, base, largura);/*desenha o retangulo preenchido*/
        g.setColor (cor);
        g.drawRect(super.x, super.y, base, largura);/*desenha o retangulo sem preenchimento*/
    }
}

