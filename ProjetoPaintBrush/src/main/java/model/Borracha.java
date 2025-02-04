/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.awt.Graphics;


/*Classe Borracha herda atributos e métodos da classe Retangulo*/
public class Borracha extends Retangulo {
    /*Construtor*/
    public Borracha(int largura) {
        this.largura = largura;
    }
    
    @Override
    public void desenhar(Graphics g){
        g.setColor (corInterna);
        g.fillRect(super.x - largura/2, super.y - largura/2, largura, largura);/*desenhar um retangulo preenchida com essas coordenadas*/
    }
    
}
    
