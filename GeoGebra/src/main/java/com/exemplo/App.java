package com.exemplo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class App extends JFrame {
    
    private JTextField input_function; // Campo para a função
    private JTextArea resultArea; // Área de resultados
    private List<Double> valorX; // Valores de X
    private List<Double> valorY; // Valores de f(X)
    private Graph graph; // Gráfico

    public App() {
        setTitle("GeoGebra");
        setSize(800, 600); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        valorX = new ArrayList<>();
        valorY = new ArrayList<>();
        graph = new Graph(valorX, valorY);

        JPanel graphPanel = new JPanel();
        graphPanel.setLayout(new BorderLayout()); 
        graphPanel.setBorder(new EmptyBorder(20, 10, 20, 10));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 1));

        inputPanel.add(new JLabel("Digite a função:"));
        input_function = new JTextField();
        inputPanel.add(input_function);

        graphPanel.add(inputPanel, BorderLayout.NORTH);
        graphPanel.add(graph, BorderLayout.CENTER);

        add(graphPanel, BorderLayout.EAST);

        JButton calcButton = new JButton("Inserir valores de X");
        calcButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inserirValoresDeX();
            }
        });
        add(calcButton, BorderLayout.SOUTH);

        JButton integrButton = new JButton("Calcular Integral");
        integrButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calcularIntegralIndefinida();
                int resposta = JOptionPane.showConfirmDialog(App.this, "Deseja calcular uma integral definida?", "Integral Definida", JOptionPane.YES_NO_OPTION);
                if (resposta == JOptionPane.YES_OPTION) {
                    calcularIntegralDefinida();
                }
            }
        });
        add(integrButton, BorderLayout.NORTH);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension newSize = new Dimension((int)(getWidth() * 0.7), getHeight());
                graphPanel.setPreferredSize(newSize);
                graphPanel.revalidate(); 
                graphPanel.repaint();
            }
        });
    }

    private void inserirValoresDeX() {
        int qntd_x = Integer.parseInt(JOptionPane.showInputDialog("Quantos valores de X deseja inserir (O valor deve ser entre 1 - 5)"));

        valorX.clear();
        valorY.clear();
        resultArea.setText("");

        for (int i = 0; i < qntd_x; i++) {
            double x = Double.parseDouble(JOptionPane.showInputDialog("Insira o valor de X: "));
            valorX.add(x);

            double result = calcularValorFuncao(x);
            valorY.add(result);
            resultArea.append("x = " + x + ", f(" + x + ") = " + result + "\n");
        }

        graph.repaint();
    }

    private double calcularValorFuncao(double x) {
        String funcao = input_function.getText();

        Expression expression = new ExpressionBuilder(funcao)
            .variables("x")
            .build()
            .setVariable("x", x);

        return expression.evaluate();
    }

    private void calcularIntegralIndefinida() {
        String funcao = input_function.getText();
        String integral = integrarPolinomio(funcao);
        resultArea.append("\n∫ f(x) dx = " + integral + "\n");
    }

    private void calcularIntegralDefinida() {
        double a = Double.parseDouble(JOptionPane.showInputDialog("Insira o valor de a:"));
        double b = Double.parseDouble(JOptionPane.showInputDialog("Insira o valor de b:"));
        double resultA = calcularValorFuncao(a);
        double resultB = calcularValorFuncao(b);
        double integralDefinida = resultB - resultA;
        resultArea.append("∫ f(x) dx de " + a + " a " + b + " = " + integralDefinida + "\n");
    }

    private String integrarPolinomio(String funcao) {
        StringBuilder integral = new StringBuilder();
        Pattern pattern = Pattern.compile("([+-]?\\d*)x\\^?(\\d*)");
        Matcher matcher = pattern.matcher(funcao);

        while (matcher.find()) {
            String coefStr = matcher.group(1);
            String expStr = matcher.group(2);

            int coeficiente = coefStr.isEmpty() || coefStr.equals("+") ? 1 : coefStr.equals("-") ? -1 : Integer.parseInt(coefStr);
            int expoente = expStr.isEmpty() ? 1 : Integer.parseInt(expStr);

            int novoCoef = coeficiente / (expoente + 1);
            int novoExpoente = expoente + 1;

            integral.append(novoCoef).append("x^").append(novoExpoente).append(" + ");
        }

        integral.append("C");
        return integral.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new App().setVisible(true);
            }
        });
    }
}
