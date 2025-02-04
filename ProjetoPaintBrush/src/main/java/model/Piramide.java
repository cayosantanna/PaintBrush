/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.awt.Graphics;

public class Piramide extends D3 {
    public int base;
    public int largura;
    
    @Override
    public float volume() {
        return (base * largura * altura)/3;
    }
    
    @Override
    public void desenhar(Graphics g){
        java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
        g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(cor);
        // Desenha a base e as linhas da pirâmide
        g2.drawRect(x, y + 2*largura, base, largura);
        g2.drawLine(x, y + 2*largura, x + base/2, y);
        g2.drawLine(x, y + 3*largura, x + base/2, y);
        g2.drawLine(x + base, y + 2*largura, x + base/2, y);
        g2.drawLine(x + base, y + 3*largura, x + base/2, y);
        if(exibirVolume){
            g2.drawString("Volume: " + volume(), x + base/2 - 20, y + 2*largura - 10);
        }
    }
    
}

