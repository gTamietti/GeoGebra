import java.awt.*;
import javax.swing.*;


public class App extends JFrame{
    
    private JTextField input_function;

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
