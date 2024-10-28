package com.exemplo;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;

public class Graph extends JPanel {
    private List<Double> xValues; // Valores de X
    private List<Double> yValues; // Valores de f(X)

    public Graph(List<Double> xValues, List<Double> yValues) {
        this.xValues = xValues;
        this.yValues = yValues;
        setPreferredSize(new Dimension(800, 600)); // Definir tamanho do painel
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = getWidth();
        int height = getHeight();
        int originX = width / 2; // Centralizar na parte destinada ao grafico os eixos
        int originY = height / 2; 

        // Ajuste a escala baseado na altura do painel para ocupar o máximo de espaço
        int scaleX = (int) (width / 12.0); // Escala para o eixo X
        int scaleY = (int) (height / 12.0); // Escala para o eixo Y

        // Desenhar eixos
        g.drawLine(originX, 0, originX, height); // Eixo Y (vertical)
        g.drawLine(0, originY, width, originY); // Eixo X (horizontal)

        // Desenhar marcas nos eixos
        for (int i = -6; i <= 6; i++) {
            // Marcas no eixo X
            
            int xPosition = originX + i * scaleX - 0;
            g.drawLine(xPosition, originY - 5, xPosition, originY + 5);
            g.drawString(String.valueOf(i), xPosition - 5, originY + 20);

            // Marcas no eixo Y 
            int yPosition = originY - i * scaleY;
            g.drawLine(originX - 5, yPosition, originX + 5, yPosition);
            g.drawString(String.valueOf(i), originX + 10, yPosition + 5);
        }

        // Desenhar pontos
        for (int i = 0; i < xValues.size(); i++) {
            int x = (int) (originX + xValues.get(i) * scaleX); // Escalar X para caber no painel
            int y = (int) (originY - yValues.get(i) * scaleY); // Escalar Y e inverter a direção
            g.fillOval(x - 3, y - 3, 6, 6); // Desenhar ponto
        }
    }
}
