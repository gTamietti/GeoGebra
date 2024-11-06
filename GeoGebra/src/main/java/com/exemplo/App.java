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
    
    private JTextField input_function; // Campo para a função
    private JTextArea resultArea; // Área de resultados
    private List<Double> valorX; // Valores de X
    private List<Double> valorY; // Valores de f(X)
    private Graph graph; // Gráfico

    public App() {
        setTitle("GeoGebra");
        setSize(800, 600); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // encerra o prograa simultaneamente ao fechar a janela
        setLayout(new BorderLayout());

        // Área onde serão impressos os dados
        resultArea = new JTextArea();
        resultArea.setEditable(false); // Desabilita para que o usuário nao possa editar o resultado
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        valorX = new ArrayList<>();
        valorY = new ArrayList<>();
        graph = new Graph(valorX, valorY);

        // Criar um novo painel para o gráfico
        JPanel graphPanel = new JPanel();
        graphPanel.setLayout(new BorderLayout()); 
        graphPanel.setBorder(new EmptyBorder(20, 10, 20, 10)); // topo , esq , baixo, dir - adicionando margem

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 1)); // Separar o Label do input

        inputPanel.add(new JLabel("Digite a função:")); // Adiciona o label
        input_function = new JTextField(); // Campo para a função
        inputPanel.add(input_function); // Adiciona o campo de texto

        graphPanel.add(inputPanel, BorderLayout.NORTH); // Adiciona o painel de entrada ao topo do gráfico
        graphPanel.add(graph, BorderLayout.CENTER); // Adiciona o gráfico ao painel

        add(graphPanel, BorderLayout.EAST); // Adiciona o painel gráfico ao lado direito

        // Inserção do botão de executar a função
        JButton calcButton = new JButton("Inserir valores de X");
        calcButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inserirValoresDeX(); // Funciona como um OnClick para executar a função ao botão ser clicado
            }
        });
        add(calcButton, BorderLayout.SOUTH); // Adiciona o botão na parte inferior da janela

        addComponentListener(new ComponentAdapter() {
            @Override
            // Ajuse de dimensão da tela exibida, não precisa preocupar muito com isso
            public void componentResized(ComponentEvent e) {
                Dimension newSize = new Dimension((int)(getWidth() * 0.7), getHeight());
                graphPanel.setPreferredSize(newSize);
                graphPanel.revalidate(); 
                graphPanel.repaint();
            }
        });
    }

    // Função chamada no OnClick
    private void inserirValoresDeX() {
        int qntd_x = Integer.parseInt(JOptionPane.showInputDialog("Quantos valores de X deseja inserir (O valor deve ser entre 1 - 5)"));

        // Métodos de limpeza de valores anteriores
        valorX.clear();
        valorY.clear();
        resultArea.setText("");

        for (int i = 0; i < qntd_x; i++) {
            double x = Double.parseDouble(JOptionPane.showInputDialog("Insira o valor de X: "));
            valorX.add(x);

            double result = calcularValorFuncao(x);
            valorY.add(result);
            resultArea.append("a = " + x + ", f(" + x + ") = " + result + "\n"); // Formatação de resposta
        }

        graph.repaint();
    }

    private double calcularValorFuncao(double x) {
        String funcao = input_function.getText(); // Receber a função do input

        Expression expression = new ExpressionBuilder(funcao)  // Cria um objeto definindo a variável como x
            .variables("x")
            .build()
            .setVariable("x", x);

        return expression.evaluate(); // Retorna o resultado para a expressão em função de x
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new App().setVisible(true); // Exibe a janela ao usuario
            }
        });
    }
}
