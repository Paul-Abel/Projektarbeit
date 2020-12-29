import javax.swing.*;


public class GUI {

    public static void main(String[] args) {
        JFrame gui = frame();
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JLabel labelTerminal = normal_label("Terminal:", 100, 100);
        JLabel labelNichtTerminal = normal_label("Nicht-Terminal:", 100, 160);
        JLabel labelStartsymbol = normal_label("Startsymbol:", 100, 220);
        JLabel labelRegel = normal_label("Regeln:", 100, 280);
        gui.add(labelTerminal);
        gui.add(labelNichtTerminal);
        gui.add(labelStartsymbol);
        gui.add(labelRegel);

        JTextField textFieldTerminal = normal_textfield(200,100);
        JTextField textFieldNichtTerminal = normal_textfield(200,160);
        JTextField textFieldStartsymbol = normal_textfield(200,220);
        JTextField textFieldRegel = normal_textfield(200,280);
        gui.add(textFieldTerminal);
        gui.add(textFieldNichtTerminal);
        gui.add(textFieldStartsymbol);
        gui.add(textFieldRegel);

        JButton buttonAddRule =normal_button("Regel bestätigen", 200,340);
        JButton buttonCommitInput =normal_button("Eingabe bestätigen", 200,400);
        gui.add(buttonAddRule);
        gui.add(buttonCommitInput);



        JTextArea textArea = new JTextArea();
        textArea.setBounds(400, 100 ,200,300);
        gui.add(textArea);
        JScrollPane scrollableTextArea = new JScrollPane();
        scrollableTextArea.setBounds(400, 100 ,200,300);
        scrollableTextArea.setViewportView(textArea);
        gui.getContentPane().add(scrollableTextArea);

        gui.setLayout(null);//using no layout managers
        gui.setVisible(true);//making the frame visible

    }

    public static JFrame frame(){
        JFrame gui = new JFrame();//creating instance of JFrame
        gui.setSize(1280,720);//400 width and 500 height
        return gui;
    }
    public static JButton normal_button(String button_text, int x, int y){
        JButton button = new JButton(button_text);
        button.setBounds(x,y,150, 40);
        return button;
    }
    public static JLabel normal_label(String label_text, int x, int y){
        JLabel label = new JLabel(label_text);
        label.setBounds(x,y,100, 40);
        return label;
    }
    public static JTextField normal_textfield(int x, int y){
        JTextField textField= new JTextField();
        textField.setBounds(x,y,150, 40);
        return textField;
    }


}

