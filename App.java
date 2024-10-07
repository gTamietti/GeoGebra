import java.awt.*;
import javax.swing.*;


public class App extends JFrame{
    
    private JTextField input_function;
    private JTextArea resultArea;

    public App(){
        setTitle("GeoGebra");
        setSize(400,200); // definir altura e largura por pixels
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //encerramento do programa ao fehcar a janela
        setLayout(new BorderLayout());

        //Parte superior do painel de interface

        JPanel painelSup = new JPanel();
        painelSup.setLayout(new GridLayout(1, 2));
        painelSup.add(new JLabel("Digite a função:"));
        input_function = new JTextField();
        painelSup.add(input_function);
        // adicionando a parte do topo da interface
        add(painelSup, BorderLayout.NORTH);

        //Area onde serão impressos os dados

        resultArea = new JTextArea();
        resultArea.setEditable(false); //usuário não pode editar o resultado do programa
        add(new JScrollPane(resultArea),BorderLayout.CENTER);

    }


    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run(){
                new App().setVisible(true); //metodo de aparição da janela para usuario
            }
        });
        
    }

}
