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
        java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
        g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(cor);
        g2.drawLine(x, y, x, y + altura);
        g2.drawLine(x + 2*raio, y, x + 2*raio, y + altura);
        g2.drawOval(x, y - 15, 2 * raio, 30);
        g2.drawOval(x, y + altura - 15, 2 * raio, 30);
        
        if(exibirVolume) {
            String volumeTxt = String.format("Volume: %.2f", volume());
            g2.drawString(volumeTxt, x + raio, y + altura/2);
        }
    }
    
}

