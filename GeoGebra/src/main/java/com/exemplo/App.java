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
    
    private JTextField input_function; // Campo para a primeira função
    private JTextField input_function2; // Campo para a segunda função
    private JTextArea resultArea; // Área de resultados
    private List<Double> valorX; // Valores de X
    private List<Double> valorY; // Valores de f(X)
    private Graph graph; // Gráfico

    public App() {
        setTitle("GeoGebra");
        setSize(1000, 600); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea), BorderLayout.EAST); // Exibir o resultado à direita

        valorX = new ArrayList<>();
        valorY = new ArrayList<>();
        graph = new Graph(valorX, valorY);

        // Painel dedicado para as entradas das funções e cálculos
        JPanel inputPanel = new JPanel(new GridLayout(9, 1, 5, 5)); // Adiciona espaçamento entre os componentes
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Digite a primeira função (f(x)):"));
        input_function = new JTextField();
        inputPanel.add(input_function);

        inputPanel.add(new JLabel("Digite a segunda função (g(x)):"));
        input_function2 = new JTextField();
        inputPanel.add(input_function2);

        // Botão para inserir valores de X e calcular f(x)
        JButton calcButton = new JButton("Calcular f(x) para valores de X");
        calcButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inserirValoresDeX();
            }
        });
        inputPanel.add(calcButton);

        // Botão para calcular integral indefinida
        JButton integrButton = new JButton("Calcular Integral Indefinida");
        integrButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calcularIntegralIndefinida();
            }
        });
        inputPanel.add(integrButton);

        // Botão para calcular integral definida
        JButton definedIntegralButton = new JButton("Calcular Integral Definida");
        definedIntegralButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calcularIntegralDefinida();
            }
        });
        inputPanel.add(definedIntegralButton);

        // Botão para calcular a área entre as funções
        JButton areaButton = new JButton("Calcular Área Entre Funções");
        areaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calcularAreaEntreFuncoes();
            }
        });
        inputPanel.add(areaButton);

        add(inputPanel, BorderLayout.WEST); // Adiciona o painel de entrada ao lado esquerdo da janela
        add(graph, BorderLayout.CENTER);    // Gráfico no centro da janela

        // Ajuste do gráfico quando a janela é redimensionada
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension newSize = new Dimension((int)(getWidth() * 0.7), getHeight());
                graph.setPreferredSize(newSize);
                graph.revalidate(); 
                graph.repaint();
            }
        });
    }

    private void inserirValoresDeX() {
        int qntd_x = Integer.parseInt(JOptionPane.showInputDialog("Quantos valores de X deseja inserir (O valor deve ser entre 1 - 5)"));

        valorX.clear();
        valorY.clear();
        resultArea.setText(""); // Limpa a área de resultados para exibir os novos valores

        for (int i = 0; i < qntd_x; i++) {
            double x = Double.parseDouble(JOptionPane.showInputDialog("Insira o valor de X: "));
            valorX.add(x);

            double result = calcularValorFuncao(input_function.getText(), x);
            valorY.add(result);
            resultArea.append("x = " + x + ", f(" + x + ") = " + result + "\n");
        }

        // Atualiza o gráfico com os novos pontos
        graph.updatePoints(valorX, valorY);
        graph.repaint();
    }

    private double calcularValorFuncao(String funcao, double x) {
        Expression expression = new ExpressionBuilder(funcao)
            .variables("x")
            .build()
            .setVariable("x", x);

        return expression.evaluate();
    }

    private void calcularIntegralIndefinida() {
        String funcao = input_function.getText();

        String integral;
        try {
            integral = integrarPolinomio(funcao);
            resultArea.append("\n∫ f(x) dx = " + integral + "\n");
        } catch (Exception e) {
            resultArea.append("\nErro ao calcular a integral indefinida: " + e.getMessage() + "\n");
        }
    }

    private String integrarPolinomio(String funcao) {
        // Aqui vamos apenas simular a integração de uma função do tipo ax + b
        if (funcao.matches("\\d*x")) { // Exemplo para "ax"
            String coeficiente = funcao.replace("x", "");
            double a = coeficiente.isEmpty() ? 1 : Double.parseDouble(coeficiente);
            return (a / 2) + "x^2"; // Resultado simbólico da integral de ax
        } else if (funcao.matches("\\d+")) { // Exemplo para constante "b"
            return funcao + "x"; // Integral de uma constante
        } else {
            return "Integral não suportada";
        }
    }

    private void calcularIntegralDefinida() {
        double a = Double.parseDouble(JOptionPane.showInputDialog("Insira o valor de a:"));
        double b = Double.parseDouble(JOptionPane.showInputDialog("Insira o valor de b:"));
        
        double integralDefinida = calcularIntegralNumerica(input_function.getText(), a, b, 1000);
        resultArea.append("∫ f(x) dx de " + a + " a " + b + " = " + integralDefinida + "\n");
    }

    private double calcularIntegralNumerica(String funcao, double a, double b, int n) {
        double h = (b - a) / n;
        double integral = 0.0;

        for (int i = 0; i < n; i++) {
            double x1 = a + i * h;
            double x2 = a + (i + 1) * h;
            integral += (calcularValorFuncao(funcao, x1) + calcularValorFuncao(funcao, x2)) * h / 2;
        }

        return integral;
    }

   private void calcularAreaEntreFuncoes() {
    try {
        double a = Double.parseDouble(JOptionPane.showInputDialog("Insira o valor de a (limite inferior):"));
        double b = Double.parseDouble(JOptionPane.showInputDialog("Insira o valor de b (limite superior):"));
        int n = Integer.parseInt(JOptionPane.showInputDialog("Insira o número de retângulos para a soma de Riemann:"));

        String funcao1 = input_function.getText();
        String funcao2 = input_function2.getText();

        double areaRiemann = calcularSomaRiemann(funcao1, funcao2, a, b, n);
        resultArea.append("Área entre as funções usando soma de Riemann: " + areaRiemann + "\n");

        // Calcula os pontos de g(x) para desenhar a área entre as funções
        List<Double> y2Values = new ArrayList<>();
        for (Double x : valorX) {
            y2Values.add(calcularValorFuncao(funcao2, x));
        }

        graph.setFuncoesComArea(valorX, valorY, y2Values, a, b);

    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Entrada inválida. Por favor, insira números válidos para a, b e n.");
    }
}

    

    private double calcularSomaRiemann(String funcao1, String funcao2, double a, double b, int n) {
        double largura = (b - a) / n;
        double area = 0.0;

        for (int i = 0; i < n; i++) {
            double x = a + i * largura;
            double y1 = calcularValorFuncao(funcao1, x);
            double y2 = calcularValorFuncao(funcao2, x);
            double altura = Math.abs(y1 - y2);
            area += altura * largura;
        }

        return area;
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
