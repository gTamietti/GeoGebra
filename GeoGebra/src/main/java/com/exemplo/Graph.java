package com.exemplo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class Graph extends JPanel {
    private List<Double> valorX; // Lista dos valores de X
    private List<Double> valorY; // Lista dos valores de Y (f(x))
    private List<Double> valorY2; // Lista dos valores de Y para a segunda função g(x), se existir
    private boolean areaEntreFuncoes; // Flag para indicar se deve exibir a área entre as funções
    private double a; // Limite inferior para o cálculo da área
    private double b; // Limite superior para o cálculo da área

    public Graph(List<Double> valorX, List<Double> valorY) {
        this.valorX = valorX;
        this.valorY = valorY;
        this.valorY2 = new ArrayList<>();
        this.areaEntreFuncoes = false;
        setPreferredSize(new Dimension(700, 600));
        setBackground(Color.WHITE);
    }

    // Atualiza os pontos da função principal f(x)
    public void updatePoints(List<Double> valorX, List<Double> valorY) {
        this.valorX = valorX;
        this.valorY = valorY;
        this.areaEntreFuncoes = false; // Reseta a visualização da área entre funções, se aplicável
        repaint();
    }

    // Define as funções e o intervalo para a área entre f(x) e g(x)
    public void setFuncoesComArea(List<Double> valorX, List<Double> valorY, List<Double> valorY2, double a, double b) {
        this.valorX = valorX;
        this.valorY = valorY;
        this.valorY2 = valorY2;
        this.a = a;
        this.b = b;
        areaEntreFuncoes = true; // Ativa a flag para desenhar a área entre funções
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Desenha os eixos
        drawAxes(g2d);

        // Desenha a função principal f(x)
        g2d.setColor(Color.BLUE);
        drawFunction(g2d, valorX, valorY);
        
        // Desenha os pontos de f(x)
        g2d.setColor(Color.BLUE);
        drawPoints(g2d, valorX, valorY);

        // Se existir, desenha a segunda função g(x) e a área entre as funções
        if (areaEntreFuncoes && valorY2.size() == valorX.size()) {
            g2d.setColor(Color.RED);
            drawFunction(g2d, valorX, valorY2);

            // Preenche a área entre f(x) e g(x) no intervalo [a, b]
            fillAreaBetweenFunctions(g2d, valorX, valorY, valorY2);
        }
    }

    // Método para desenhar os eixos
    private void drawAxes(Graphics2D g2d) {
        int width = getWidth();
        int height = getHeight();

        // Linha do eixo X (horizontal)
        g2d.setColor(Color.BLACK);
        g2d.drawLine(0, height / 2, width, height / 2);

        // Linha do eixo Y (vertical)
        g2d.drawLine(width / 2, 0, width / 2, height);
    }

    // Método para desenhar uma função (f(x) ou g(x)) com base nos valores de X e Y
    private void drawFunction(Graphics2D g2d, List<Double> xs, List<Double> ys) {
        for (int i = 0; i < xs.size() - 1; i++) {
            Double x1 = xs.get(i);
            Double y1 = ys.get(i);
            Double x2 = xs.get(i + 1);
            Double y2 = ys.get(i + 1);

            if (x1 != null && y1 != null && x2 != null && y2 != null) {
                int xPixel1 = toPixelX(x1);
                int yPixel1 = toPixelY(y1);
                int xPixel2 = toPixelX(x2);
                int yPixel2 = toPixelY(y2);

                g2d.drawLine(xPixel1, yPixel1, xPixel2, yPixel2);
            }
        }
    }

    // Método para desenhar pontos de f(x) como pequenos círculos
    private void drawPoints(Graphics2D g2d, List<Double> xs, List<Double> ys) {
        g2d.setColor(Color.BLUE);
        for (int i = 0; i < xs.size(); i++) {
            Double x = xs.get(i);
            Double y = ys.get(i);

            if (x != null && y != null) {
                int xPixel = toPixelX(x);
                int yPixel = toPixelY(y);
                g2d.fillOval(xPixel - 3, yPixel - 3, 6, 6); // Desenha um pequeno círculo em cada ponto
            }
        }
    }

    // Método para preencher a área entre duas funções
    private void fillAreaBetweenFunctions(Graphics2D g2d, List<Double> xs, List<Double> ys1, List<Double> ys2) {
        g2d.setColor(new Color(0, 100, 200, 50)); // Cor semi-transparente para a área

        Path2D.Double path = new Path2D.Double();
        boolean pathStarted = false;

        for (int i = 0; i < xs.size(); i++) {
            Double x = xs.get(i);
            Double y1 = ys1.get(i);
            Double y2 = ys2.get(i);

            if (x != null && y1 != null && y2 != null && x >= a && x <= b) {
                int xPixel = toPixelX(x);
                int yPixel1 = toPixelY(y1);
                int yPixel2 = toPixelY(y2);

                // Inicia o caminho na primeira coordenada válida
                if (!pathStarted) {
                    path.moveTo(xPixel, yPixel1);
                    pathStarted = true;
                }

                // Adiciona o ponto na primeira função f(x)
                path.lineTo(xPixel, yPixel1);
            }
        }

        // Liga a segunda função g(x) no caminho da área
        for (int i = xs.size() - 1; i >= 0; i--) {
            Double x = xs.get(i);
            Double y2 = ys2.get(i);

            if (x != null && y2 != null && x >= a && x <= b) {
                int xPixel = toPixelX(x);
                int yPixel2 = toPixelY(y2);
                path.lineTo(xPixel, yPixel2);
            }
        }

        path.closePath();
        g2d.fill(path);
    }

    // Converte a coordenada X para pixels (escala simplificada para ilustração)
    private int toPixelX(double x) {
        int width = getWidth();
        return (int) ((x + 10) / 20 * width); // Ajuste de escala arbitrário
    }

    // Converte a coordenada Y para pixels (escala simplificada para ilustração)
    private int toPixelY(double y) {
        int height = getHeight();
        return (int) (height / 2 - y * 20); // Ajuste de escala arbitrário
    }
}
