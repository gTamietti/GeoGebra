import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class App extends JFrame{
    
    private JTextField input_function;
    private JTextArea resultArea;
    private List<Double> xValues;

    public App(){
        setTitle("GeoGebra");
        setSize(400,200); // definir altura e largura por pixels
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //encerramento do programa ao fechar a janela
        setLayout(new BorderLayout());

        // Parte superior do painel de interface

        JPanel painelSup = new JPanel();
        painelSup.setLayout(new GridLayout(1, 2));
        painelSup.add(new JLabel("Digite a função:"));
        input_function = new JTextField();
        painelSup.add(input_function);
        add(painelSup, BorderLayout.NORTH);

        // Área onde serão impressos os dados

        resultArea = new JTextArea();
        resultArea.setEditable(false); //usuário não pode editar o resultado do programa
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        // Inserção do botão de executar a função

        JButton calcButton = new JButton("Inserir valores de X");
        calcButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                inserirValoresDeX(); //Funciona como um OnClick para executar a função ao botão ser clicado
            }
        });
        add(calcButton, BorderLayout.SOUTH); // Adiciona o botão na parte inferior da janela

        xValues = new ArrayList<>();
    }

    // Função cahamada no OnClick

    private void inserirValoresDeX(){
        
        int qntd_x = Integer.parseInt(JOptionPane.showInputDialog("Quantos valores de X deseja inserir (O valor deve ser entre 1 - 5)"));

        // Métodos de limpeza de valores anteriores
        xValues.clear();
        resultArea.setText("");

        for(int i=0;i<qntd_x;i++){
            double x = Double.parseDouble(JOptionPane.showInputDialog("Insira o valor de X: "));
            xValues.add(x);

            double result = calcularValorFuncao(x);
            resultArea.append("a = " + x + ", f(" + x + ") = " + result + "\n"); // Imprimir na tela o resultado da funcao em f(x)
        }

    }


    private double calcularValorFuncao(double x){
        String funcao = input_function.getText(); // Receber a função do input

        Expression expression = new ExpressionBuilder(funcao)  //Cria um objeto definindo a variavel como x
            .variables("x")
            .build()
            .setVariable("x", x);

        return expression.evaluate(); // Retorna o resultado para a expressão em função de x
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run(){
                new App().setVisible(true); // método de aparição da janela para usuário
            }

        });

    }

}
