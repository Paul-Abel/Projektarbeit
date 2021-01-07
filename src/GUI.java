import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


public class GUI  {

    public static void main(String[] args) {
        Logic logic = new Logic();
        JFrame gui = frame();
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Alle Label werden hinzugefügt
        JLabel labelTerminal = normal_label("Terminal:", 100, 100);
        JLabel labelNichtTerminal = normal_label("Nicht-Terminal:", 100, 160);
        JLabel labelStartsymbol = normal_label("Startsymbol:", 100, 220);
        JLabel labelRegel = normal_label("Regeln:", 100, 280);
        gui.add(labelTerminal);
        gui.add(labelNichtTerminal);
        gui.add(labelStartsymbol);
        gui.add(labelRegel);

        //Alle Eingabefelder werden hinzugefügt
        JTextField textFieldTerminal = normal_textfield(200,100);
        JTextField textFieldNichtTerminal = normal_textfield(200,160);
        JTextField textFieldStartsymbol = normal_textfield(200,220);
        JTextField textFieldRegel = normal_textfield(200,280);
        gui.add(textFieldTerminal);
        gui.add(textFieldNichtTerminal);
        gui.add(textFieldStartsymbol);
        gui.add(textFieldRegel);

        //Alle Buttons werden hinzugefügt
        JButton buttonAddRule =normal_button("Regel bestätigen", 200,340);
        JButton buttonCommitInput =normal_button("Eingabe bestätigen", 200,400);
        JButton buttonCheckInput =normal_button("Alles prüfen", 200,25);
        JButton buttonSingleStep =normal_button("Einzelschritt", 400,25);
        JButton buttonMultiStep =normal_button("Regel ausführen", 600,25);
        JButton buttonAllStep =normal_button("Alles ausführen", 800,25);
        JButton buttonNewInput =normal_button("Neue Eingabe", 1000,25);
        buttonAllStep.setEnabled(false);
        buttonMultiStep.setEnabled(false);
        buttonSingleStep.setEnabled(false);
        gui.add(buttonAddRule);
        gui.add(buttonCommitInput);
        gui.add(buttonCheckInput);
        gui.add(buttonSingleStep);
        gui.add(buttonMultiStep);
        gui.add(buttonAllStep);
        gui.add(buttonNewInput);

        //Alle TextAreas werden hinzugefügt
        JTextArea textAreaInput = new JTextArea();
        JTextArea textAreaLog = new JTextArea();
        JTextArea textAreaOutput = new JTextArea();
        JScrollPane textAreaInputScroll = textAreaScrollable(400,100,200,300, textAreaInput);
        JScrollPane textAreaOutputScroll = textAreaScrollable(650,100,200,300, textAreaOutput);
        JScrollPane textAreaLogScroll = textAreaScrollable(25,500,1230,150, textAreaLog);
        gui.add(textAreaInputScroll);
        gui.add(textAreaLogScroll);
        gui.add(textAreaOutputScroll);


        gui.setLayout(null);//using no layout managers
        gui.setVisible(true);//making the frame visible

        buttonAddRule.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> returnList = logic.confirm_rule(textFieldRegel.getText());
                textAreaLog.append(returnList.get(0) +"\n");
                textAreaInput.setText(returnList.get(1) +"\n");
            }
        });
        buttonCommitInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> returnList = logic.commit_input(textFieldTerminal.getText(),textFieldNichtTerminal.getText(),textFieldStartsymbol.getText());
                textAreaLog.append(returnList.get(0) +"\n");
                textAreaInput.setText(returnList.get(1) +"\n");
            }
        });
        buttonCheckInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> returnList = logic.check_input();
                textAreaLog.append(returnList.get(0) +"\n");
                textAreaInput.setText(returnList.get(1) +"\n");
            }
        });
        buttonAllStep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textAreaLog.append("All Steps\n");
            }
        });
        buttonMultiStep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textAreaLog.append("Multi Steps\n");

            }
        });
        buttonSingleStep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> returnList = logic.single_step();
                textAreaLog.append(returnList.get(0) +"\n");
                textAreaOutput.append(returnList.get(1) +"\n");
            }
        });
        buttonNewInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textAreaLog.append("Neuer Input\n");
            }
        });

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
    public static JScrollPane textAreaScrollable(int x, int y, int width, int height, JTextArea textArea){
        textArea.setEditable(false);
        JScrollPane scrollableTextArea = new JScrollPane();
        scrollableTextArea.setBounds(x,y,width,height);
        scrollableTextArea.setViewportView(textArea);
        return scrollableTextArea;
    }
}

