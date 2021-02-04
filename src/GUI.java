import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


public class GUI  {

    public static void main(String[] args) {
        Logic logic = new Logic();
        JFrame gui = frame();
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add labels
        JLabel labelTerminal = normal_label("Terminal:", 100, 100);
        JLabel labelVariable = normal_label("Variable:", 100, 160);
        JLabel labelStartsymbol = normal_label("Startsymbol:", 100, 220);
        JLabel labelRule = normal_label("Regeln:", 100, 340);
        JLabel labelInput =normal_label("Input:", 400, 70);
        JLabel labelOutput =normal_label("Output:", 750, 70);
        gui.add(labelTerminal);
        gui.add(labelVariable);
        gui.add(labelStartsymbol);
        gui.add(labelRule);
        gui.add(labelInput);
        gui.add(labelOutput);

        //Add textfields
        JTextField textFieldTerminal = normal_textfield(200,100);
        JTextField textFieldVariable = normal_textfield(200,160);
        JTextField textFieldStartsymbol = normal_textfield(200,220);
        JTextField textFieldRule = normal_textfield(200,340);
        gui.add(textFieldTerminal);
        gui.add(textFieldVariable);
        gui.add(textFieldStartsymbol);
        gui.add(textFieldRule);

        //Add buttons
        JButton buttonAddRule =normal_button("Regel bestätigen", 200,400);
        JButton buttonCommitInput =normal_button("Eingabe bestätigen", 200,280);
        JButton buttonCheckInput =normal_button("Alles prüfen", 200,460);
        JButton buttonSingleStep =normal_button("Einzelschritt", 400,25);
        JButton buttonMultiStep =normal_button("Multischritte", 600,25);
        JButton buttonRuleStep =normal_button("Regel ausführen", 800,25);
        JButton buttonAllStep =normal_button("Alles ausführen", 1000,25);
        JButton buttonNewInput =normal_button("Neue Eingabe", 200,25);
        buttonAllStep.setEnabled(false);
        buttonRuleStep.setEnabled(false);
        buttonSingleStep.setEnabled(false);
        buttonMultiStep.setEnabled(false);
        gui.add(buttonAddRule);
        gui.add(buttonCommitInput);
        gui.add(buttonCheckInput);
        gui.add(buttonSingleStep);
        gui.add(buttonRuleStep);
        gui.add(buttonAllStep);
        gui.add(buttonNewInput);
        gui.add(buttonMultiStep);

        //Add textAreas
        JTextArea textAreaInput = new JTextArea();
        JTextArea textAreaLog = new JTextArea();
        JTextArea textAreaOutput = new JTextArea();
        JScrollPane textAreaInputScroll = textAreaScrollable(400,100,300,400, textAreaInput);
        JScrollPane textAreaOutputScroll = textAreaScrollable(750,100,300,400, textAreaOutput);
        JScrollPane textAreaLogScroll = textAreaScrollable(25,550,1230,300, textAreaLog);
        gui.add(textAreaInputScroll);
        gui.add(textAreaLogScroll);
        gui.add(textAreaOutputScroll);


        gui.setLayout(null);//using no layout managers
        gui.setVisible(true);//making the frame visible

        buttonAddRule.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textAreaLog.append(logic.confirm_rule(textFieldRule.getText())+"\n");
                textAreaInput.setText(logic.getInputString()+"\n");
            }
        });
        buttonCommitInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textAreaLog.append(logic.commit_input(textFieldTerminal.getText(),textFieldVariable.getText(),textFieldStartsymbol.getText()) +"\n");
                textAreaInput.setText(logic.getInputString() +"\n");
            }
        });
        buttonCheckInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String a = logic.check_input();
                textAreaLog.append(a +"\n");
                textAreaInput.setText(logic.getInputString() +"\n");
                if("Eingabe stimmt.".equals(a)){
                    buttonAddRule.setEnabled(false);
                    buttonCheckInput.setEnabled(false);
                    buttonCommitInput.setEnabled(false);
                    buttonAllStep.setEnabled(true);
                    buttonRuleStep.setEnabled(true);
                    buttonSingleStep.setEnabled(true);
                    buttonMultiStep.setEnabled(true);
                    textAreaOutput.setText(logic.getOutputString());
                }
            }
        });
        buttonAllStep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> returnList = logic.all_step();
                textAreaLog.append(returnList.get(0) +"\n");
                textAreaOutput.setText(returnList.get(1) +"\n");
            }
        });
        buttonRuleStep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> returnList = logic.rule_step();
                textAreaLog.append(returnList.get(0) +"\n");
                textAreaOutput.setText(returnList.get(1) +"\n");

            }
        });
        buttonSingleStep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> returnList = logic.single_step();
                textAreaLog.append(returnList.get(0) +"\n");
                textAreaOutput.setText(returnList.get(1) +"\n");
            }
        });
        buttonNewInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //clear variables in logic
                logic.clear();

                //set Buttons on their default
                buttonAddRule.setEnabled(true);
                buttonCheckInput.setEnabled(true);
                buttonCommitInput.setEnabled(true);
                buttonAllStep.setEnabled(false);
                buttonRuleStep.setEnabled(false);
                buttonSingleStep.setEnabled(false);
                buttonMultiStep.setEnabled(false);

                textFieldRule.setText("");
                textFieldStartsymbol.setText("");
                textFieldTerminal.setText("");
                textFieldVariable.setText("");

                textAreaInput.setText("");
                textAreaOutput.setText("");
                textAreaLog.setText("");
            }
        });
        buttonMultiStep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> returnList = logic.multi_step();
                textAreaLog.append(returnList.get(0) +"\n");
                textAreaOutput.setText(returnList.get(1) +"\n");
            }
        });

    }

    public static JFrame frame(){
        JFrame gui = new JFrame();//creating instance of JFrame
        gui.setSize(1280,900);//400 width and 500 height
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

