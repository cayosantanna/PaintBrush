/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.awt.Color;
import java.awt.Graphics;


public abstract class D2 extends Ponto{
    public Color corInterna;
    public boolean exibirArea = false;

    abstract public float area(); 
    abstract  public float perimetro();
    
    @Override
    public void desenhar (Graphics g) {
        if(exibirArea) g.drawString("Área: " + Float.toString(area()), x, y);/*se for verdade, vai transformar 
        o calculo da area em uma string para ser exibido na tela*/
    }
}   

