/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.awt.Color;
import java.awt.Graphics;

/*Classe Borracha herda atributos e métodos da classe Retangulo*/
public class Borracha extends Retangulo {
    /*Construtor*/
    public Borracha(int largura) {
        this.largura = largura;
    }
    
    @Override
    public void desenhar(Graphics g) {
        java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
        g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(corInterna);
        g2.fillRect(x - largura / 2, y - largura / 2, largura, largura);
        // Feedback visual com contorno suave
        g2.setColor(new Color(200, 200, 200));
        g2.drawRect(x - largura / 2, y - largura / 2, largura, largura);
    }
    
}

