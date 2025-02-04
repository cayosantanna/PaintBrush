/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.awt.Graphics;


public class Cilindro extends D3 {
    public int raio;
    
    @Override
    public float volume() {
        return Circulo.PI * raio * raio * altura;
    }
    
    @Override
    public void desenhar(Graphics g){
        g.setColor(cor);
        g.drawLine(x, y, x, y + altura);
        g.drawLine(x + 2*raio, y, x + 2*raio, y + altura);
        g.drawOval(x, y - 15, 2 * raio, 30);
        g.drawOval(x, y + altura - 15, 2 * raio, 30);
    }
    
}

