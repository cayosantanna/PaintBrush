/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.awt.Graphics;
import java.util.ArrayList;


public class Poligono extends D2 {
    public ArrayList<Ponto> listaPontos;
    
    public Poligono() {
        listaPontos = new ArrayList();
    }
    
    @Override
    public float area() {
        return -1;
    }

    @Override
    public float perimetro() {
        return -1;
    }
    
    public void adicionarPonto(int x, int y){
        Ponto p = new Ponto();
        p.x = x;
        p.y = y;
        listaPontos.add(p);
    }
        
    public void removerPontos(){
        listaPontos.clear();
    }
    
    @Override
    public void desenhar(Graphics g){
        int[] pontosX = new int[listaPontos.size()]; /*declaração do array definido com o mesmo tamanho que a lista de pontos*/
        int[] pontosY = new int[listaPontos.size()]; /*declaração do array definido com o mesmo tamanho que a lista de pontos*/
        for(int i = 0; i < listaPontos.size(); i++){ /*aumentara a cada interação da listaPontos*/
            pontosX[i] = listaPontos.get(i).x; /*copiando os pontos da coordenada x dos para a lista de array*/
            pontosY[i] = listaPontos.get(i).y; /*copiando os pontos da coordenada y dos para a lista de array*/
        }
        g.setColor(corInterna);
        g.fillPolygon(pontosX, pontosY,listaPontos.size());
        g.setColor(cor);
        g.drawPolygon(pontosX, pontosY,listaPontos.size());
        removerPontos();
    }
    
}
