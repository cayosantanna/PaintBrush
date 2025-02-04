/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.awt.Graphics;

public abstract class D3 extends Ponto {
    public int altura;
    public boolean exibirVolume;
    
    abstract public float volume();
    
    @Override
    public void desenhar (Graphics g) {
     
    }
}
    
