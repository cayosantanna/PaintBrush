/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;


public abstract class D2 extends Ponto{
    public Color corInterna;
    public boolean exibirArea = false;

    abstract public float area(); 
    abstract  public float perimetro();
    
    @Override
    public void desenhar(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        if(exibirArea) {
            String areaText = String.format("Área: %.2f", area());
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(areaText);
            g2.drawString(areaText, x - textWidth / 2, y);
        }
    }
}

