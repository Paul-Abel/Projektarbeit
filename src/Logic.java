import java.util.*;

public class Logic {
    //add all classas needed
    Hilfmethoden help_method = new Hilfmethoden();

    //define everything for input
    List<String> input_terminal = new ArrayList<>();
    List<String> input_variable = new ArrayList<>();
    List<String> input_start_variable = new ArrayList<>();
    Map<String, List<String>> input_rules = new HashMap<>();

    //define everything for output
    List<String> output_terminal = new ArrayList<>();
    List<String> output_variable = new ArrayList<>();
    List<String> output_start_variable = new ArrayList<>();
    Map<String, List<String>> output_rules = new HashMap<>();

    //add some Map/Lists to cache some data
    Map<String, List<String>> chain_rule_map = new HashMap<>();
    List<String> chain_rule_list = new ArrayList<>();
    Map<String,String> start_variable_translate = new HashMap<>();
    Map<String,String> terminal_translate = new HashMap<>();
    Map<String, Map<String, List<String>>> grammar_rule= new HashMap<>();

    //define variables to save the states
    final int count_rule = 6;
    int next_rule = 0;
    String next_rule_variable = "";
    String next_rule_content = "";
    String next_grammar = "";
    int chainrule_state = 0;



    //Checks the input and add a rule to the input_rule_map
    //rule_input is a rule from the gui, which should be wrote in a certain way. (Variable:rule1,rule2,rule3...)
    public String confirm_rule(String rule_input ){

        //Definition for intern variables
        StringBuilder log_output = new StringBuilder();
        boolean rule_key_input_valid = true;
        boolean rule_value_input_valid = true;

        //Split input to rule_key and rule_value
        rule_input = rule_input.replaceAll("[^a-zA-Z],:", "");
        String rule_key =rule_input.split(":")[0].trim().toUpperCase();
        String[] rule_value = rule_input.split(":")[1].trim().split(",");

        //Check if rule key is valid
        if(rule_key.length()!=1){
            log_output.append("Bitte eine Regel eingeben.\n");
            rule_key_input_valid = false;
        }
        if(!input_variable.contains(rule_key)){
            log_output.append("Bitte eine Regel für eine Variable definieren, die auch als Variable deklariert wurde.\n");
            rule_key_input_valid = false;
        }

        //Check if rule value is valid
        for(String value: rule_value){
            for (int i = 0; i<value.length();i++) {
                char ch = value.charAt(i);
                if (!input_terminal.contains(String.valueOf(ch))  && !input_variable.contains(String.valueOf(ch))) {
                    log_output.append("Das Zeichen ").append(ch).append(" muss als Variable oder Terminal definiert sein.\n");
                    rule_value_input_valid = false;
                }
            }
        }

        // rule will be added if the input was correct
        if(rule_key_input_valid && rule_value_input_valid){
            if(!input_rules.containsKey(rule_key)){
                input_rules.put(rule_key, new ArrayList<>());
            }
            for(String a: rule_value){
                if(!input_rules.get(rule_key).contains(a)) {
                    input_rules.get(rule_key).add(a);
                    log_output.append("Für ").append(rule_key).append(" wurde die Regel ").append(a).append(" hizugefügt.\n");
                }
                else{
                    log_output.append("Für ").append(rule_key).append(" existiert die Regel ").append(a).append(" bereits.\n");
                }
            }
        }

        //send the log and input back to the gui
        return log_output.toString();
    }

    //Checks the input and add a terminal, variable and the start-variable to their lists
    //terminal, variable and start_variable are the Input from their Textarea from the gui
    public String commit_input(String terminal, String variable, String start_variable){

        //Definition for intern variables
        StringBuilder log_output= new StringBuilder();

        boolean terminal_valid = true;
        boolean variable_valid = true;
        boolean start_variable_valid = true;

        //Clear the list so only the current data will be saved
        input_terminal.clear();
        input_variable.clear();
        input_start_variable.clear();

        //Check if the input for terminal are correct
        String[] terminal_array = null;
        terminal = terminal.replaceAll("[^a-zA-Z],", "");
        if(terminal.length()==0){
            log_output.append("Bitte bei den Terminalen etwas eingeben.\n");
            terminal_valid = false;
        }
        else {
            terminal = terminal.trim();
            terminal_array = terminal.split(",");

            for (String a : terminal_array) {
                if (a.length() != 1) {
                    terminal_valid = false;
                    log_output.append("Bitte bei Terminalen nur einzelne Buchstaben eingeben.\n");
                }
            }
        }

        //Add terminals if valid
        if (terminal_valid) {
            for (String a : terminal_array) {
                input_terminal.add(a.toLowerCase());
            }
            log_output.append("Die Terminale ").append(input_terminal.toString()).append(" wurden erfolgreich hinzugefügt.\n");
        }

        //Check if the input for variables are correct
        String[] not_terminal_array = null;
        variable = variable.replaceAll("[^a-zA-Z],", "");
        if(variable.length()==0){
            log_output.append("Bitte bei den Variablen etwas eingeben.\n");
            variable_valid = false;
        }
        else {
            variable = variable.trim();
            not_terminal_array = variable.split(",");
            for (String a : not_terminal_array) {
                if (a.length() != 1) {
                    variable_valid = false;
                    log_output.append("Bitte bei den Variablen nur einzelne Buchstaben eingeben.\n");
                }
            }
        }

        //add variables if valid
        if (variable_valid) {
            for (String a : not_terminal_array) {
                input_variable.add(a.toUpperCase());
            }
            log_output.append("Die Variablen ").append(input_variable.toString()).append(" wurden erfolgreich hinzugefügt.\n");
        }

        //Check if the input for start_variable are correct
        String[] start_var_array = null;
        start_variable = start_variable.replaceAll("[^a-zA-Z],", "");
        if(start_variable.length()==0){
            log_output.append("Bitte bei der Startvariablen etwas eingeben.\n");
            start_variable_valid = false;
        }
        else {
            start_variable = start_variable.trim();
            start_var_array= start_variable.split(",");
            for (String a : start_var_array) {
                if(!input_variable.contains(a.toUpperCase())){
                    start_variable_valid = false;
                    log_output.append("Startvaribale ").append(a).append(" ist nicht in den Nichtterminalen definiert.\n");
                }
                if (a.length() != 1) {
                    start_variable_valid = false;
                    log_output.append("Bitte bei der Startvariablen nur einzelne Buchstaben eingeben.\n");
                }
            }
        }

        //add start_variables if valid
        if (start_variable_valid) {
            for (String a : start_var_array) {
                input_start_variable.add(a.toUpperCase());
            }
            log_output.append("Die Startvariable ").append(input_start_variable.toString()).append(" wurde erfolgreich hinzugefügt.\n");
        }

        //send the log and input back to the gui
        return log_output.toString();

    }

    //Makes a final input check before the input will be used
    public String check_input(){

        //Definition for intern variables
        StringBuilder log_output = new StringBuilder();
        boolean input_correct = true;

        //Check if input is empty
        if(input_start_variable.size()==0){
            input_correct = false;
            log_output.append("Bitte mindestens eine Startvariable einfügen.\n");
        }
        if(input_variable.size()==0){
            input_correct = false;
            log_output.append("Bitte mindestens eine Variable einfügen.\n");
        }
        if(input_terminal.size()==0){
            input_correct = false;
            log_output.append("Bitte mindestens eine Terminale einfügen.\n");
        }
        if(input_rules.keySet().size()==0) {
            input_correct = false;
            log_output.append("Bitte mindestens eome Regel einfügen.\n");
        }

        //Check if startvariable is in variable
        for (String a : input_start_variable) {
            if (!input_variable.contains(a.toUpperCase())) {
                input_correct = false;
                log_output.append("Startvaribale ").append(a).append(" ist nicht in den Nichtterminalen definiert.\n");
            }
        }

        //Check if for every rule variable exists a defined variable
        Set<String> keys = input_rules.keySet();
        for(String key: keys){
            if (!input_variable.contains(key)) {
                input_correct = false;
                log_output.append("Es wurde für die Regel die Variable ").append(key).append(" verwedendet welche nicht definiert ist.\n");
            }
        }

        //If: Check if for every rule variable exists a defined variable
        //Else: Check if all terminals and variable used in the rules are defined
        for(String variable: input_variable){
            if (!keys.contains(variable)) {
                input_correct = false;
                log_output.append("Es wurde eine Variabel ").append(variable).append(" definiert, für welche es keine Regel gibt\n");
            }
            else{
                for(String a: input_rules.get(variable)){
                    for (int i = 0; i<a.length();i++) {
                        String ch = String.valueOf(a.charAt(i));
                        if (!input_terminal.contains(ch)  && !input_variable.contains(ch)) {
                            log_output.append("Das Zeichen ").append(ch).append(" muss als Nicht-Terminal oder Terminal definiert sein\n");
                            input_correct = false;
                        }
                    }
                }
            }
        }

        //Makes a log_output on succes and set init values
        if(input_correct){
            output_rules = input_rules;
            output_variable = input_variable;
            output_terminal = input_terminal;
            output_start_variable = input_start_variable;
            next_rule_variable = (String) output_rules.keySet().toArray()[0];
            next_rule_content = output_rules.get(next_rule_variable).get(0);
            log_output.append("Eingabe stimmt.");
        }

        //send the log and input back to the gui
        return log_output.toString();
    }

    //Resets all global variables
    public void clear(){
        //input variables
        input_terminal.clear();
        input_variable.clear();
        input_start_variable.clear();
        input_rules.clear();

        //output variables
        output_rules.clear();
        output_start_variable.clear();
        output_terminal.clear();
        output_variable.clear();

        //cache variables
        start_variable_translate.clear();
        terminal_translate.clear();
        chain_rule_map.clear();
        chain_rule_list.clear();
        grammar_rule.clear();

        //state variables
        chainrule_state =0;
        next_rule = 0;
        next_rule_variable = "";
        next_rule_content = "";
        next_grammar = "";
    }

    //Execute the entire program
    public List<String> all_step(){
        //Define some variables
        List<String> returnList = new ArrayList<>();
        StringBuilder log_output = new StringBuilder();

        //Execute everything till the last rule
        for(int i = next_rule; i<count_rule; i++ ){
            log_output.append(rule_step().get(0));
        }
        log_output.append("Umformung ist fertig!\n");

        //Convert and finished return content
        returnList.add(help_method.convertOutput(log_output.toString()));
        returnList.add(getOutputString());
        return returnList;
    }

    //Execute only one rule
    public List<String> rule_step(){
        //Define some variables
        List<String> returnList = new ArrayList<>();
        StringBuilder log_output = new StringBuilder();
        int old_rule_state = next_rule;

        //Execute everything till one rule run through
        while(old_rule_state == next_rule){
            if(next_rule==6){
                log_output.append("Umformung ist fertig!\n");
                break;
            }
            log_output.append(multi_step().get(0));
        }

        //Convert and finished return content
        returnList.add(help_method.convertOutput(log_output.toString()));
        returnList.add(getOutputString());
        return returnList;
    }

    //Execute rule on all rules from the same variable
    public List<String> multi_step(){
        //Define some variables
        List<String> returnList = new ArrayList<>();
        StringBuilder log_output = new StringBuilder();

        //different execution on multi_step to delete chainrules(rule 1)
        if(next_rule == 1){
            while(chainrule_state ==0){
                log_output.append(single_step().get(0));
            }
            while(chainrule_state ==1){
                log_output.append(single_step().get(0));
            }
        }
        else if(next_rule==6){
            log_output.append("Umformung ist fertig!\n");
        }

        //Execute rule on all rules from the same variable
        else if(next_rule==5){
            String old_next_rule_variable = next_rule_variable;
            while(next_rule_variable==old_next_rule_variable){
                log_output.append(single_step().get(0));
            }
        }

        //Convert and finished return content
        returnList.add(help_method.convertOutput(log_output.toString()));
        returnList.add(getOutputString());
        return returnList;
    }

    //Execute rule on one specific rule
    public List<String> single_step(){
        //Define some variables
        List<String> returnList = new ArrayList<>();
        String log_output = "";

        //Execute rule on one specific rule
        switch(next_rule) {
            case 0:
                log_output =replaceStartvariable();
                break;
            case 1:
                log_output =deleteChainRule();
                break;
            case 2:
                log_output =replaceTerminal();
                break;
            case 3:
                log_output =reduceLength();
                break;
            case 4:
                log_output = createGrammars();
                break;
            case 5:
                log_output = replaceFirstVar();
                break;
            case 6:
                log_output+= "Umformung ist fertig!\n";

        }

        //Convert and finished return content
        returnList.add(help_method.convertOutput(log_output.toString()));
        returnList.add(getOutputString());
        return returnList;
    }

    //returns the string for the input-textarea
    public String getInputString(){
        //Define some variables
        StringBuilder rule = new StringBuilder();

        //Generate input_rules as a String
        for(String a: input_variable){
            if(input_rules.containsKey(a)){
                rule.append("\t").append(a).append(": ").append(input_rules.get(a).toString()).append("\n");
            }
        }

        //Return input
        return "Terminal:\t"+ input_terminal.toString()+"\n"+
                "Variable:\t" + input_variable.toString() + "\n" +
                "Startvariable:\t" + input_start_variable.toString() + "\n"+
                "Regeln" + rule;
    }

    //returns the string for the output-textarea
    public String getOutputString(){
        //Define some variables
        StringBuilder rule = new StringBuilder();
        String next_rule_output = "";

        //Generate output_rules as a String
        for(String a: output_variable){
            if(output_rules.containsKey(a)){
                rule.append("\t").append(a).append(": ").append(output_rules.get(a).toString()).append("\n");
            }
        }

        //Get the text fits to the rule
        switch (next_rule){
            case 0:
                next_rule_output = "Startvariable auf rechter Seite entfernen.";
                break;
            case 1:
                next_rule_output = "Entferne Kettenregel.";
                break;
            case 2:
                next_rule_output = "Ersetze Terminale durch eine Variable.";
                break;
            case 3:
                next_rule_output = "Erstelle nur Regeln der Länge 2.";
                break;
            case 4:
                next_rule_output = "Erstelle neue Grammatiken für "+ next_grammar+".";
                break;
            case 5:
                next_rule_output = "Ersetze erste Variable mit allen Regeln von S(var).";
                break;
            case 6:
                next_rule_output=" Umformung fertig";
        }

        //Generate Output
        StringBuilder output = new StringBuilder("Nächster Schritt:" + next_rule_output + "\n" +
                "Nächste Regel:" + next_rule_variable + ": " + next_rule_content + "\n\n" +
                "Terminal:\t" + output_terminal.toString() + "\n" +
                "Variable:\t" + output_variable.toString() + "\n" +
                "Startvariable:\t" + output_start_variable.toString() + "\n" +
                "Regeln" + rule + "\n");

        //Generate the new grammars and add to the output if its needed
        if(next_rule>=4){
            output.append("Neue Regeln:\n");
            for(String a: output_variable){
                if(grammar_rule.containsKey(a)){
                    output.append(a).append(": ").append(grammar_rule.get(a)).append("\n");
                }
            }
        }
        return output.toString();
    }

    //set the next state
    public void setNextState(){
        //Define some variables
        int key_index = 0;
        int value_index = output_rules.get(next_rule_variable).indexOf(next_rule_content);

        //Check if a rule from the same variable exists
        if(value_index+1 < output_rules.get(next_rule_variable).size()){
            next_rule_content = output_rules.get(next_rule_variable).get(value_index+1);
        }
        //Go to the next variable
        else {
            //Get the index of the next variable
            for(int i = 0; i < output_variable.size(); i++) {
                if(output_variable.get(i).equals(next_rule_variable)) {
                    key_index = i;
                    break;
                }
            }
            //Check if a next variable exists
            if(key_index+1< output_variable.size()){
                next_rule_variable = output_variable.get(key_index+1);
            }
            //Go to next rule
            else{
                next_rule =next_rule+1;
                next_rule_variable = output_variable.get(0);
            }
            next_rule_content = output_rules.get(next_rule_variable).get(0);
        }

    }

    //replace every startvariable on the right side of a rule
    public String replaceStartvariable(){

        //Define some intern variables
        boolean add_new_variable = true;
        StringBuilder log_output = new StringBuilder();
        String to_replaced;
        String replaced;

        //Check for each letter if its contain a startvariable
        for(int i=0; i<next_rule_content.length(); i++) {
            to_replaced = String.valueOf(next_rule_content.charAt(i));
            if(output_start_variable.contains(to_replaced)){

                //Check if already a replace variable exits
                for(String keys: start_variable_translate.keySet()){
                    if(keys.equals(to_replaced)){
                        add_new_variable = false;
                        break;
                    }
                }

                //Define new terminal and add it to the lists
                if(add_new_variable) {
                    output_rules.put("S" + start_variable_translate.size(), output_rules.get(to_replaced));
                    output_variable.add("S" + start_variable_translate.size());
                    log_output.append("Es wurde die Hiflsvariable S").append(start_variable_translate.size()).append(" erstellt und alle Regeln von ").append(to_replaced).append(" dieser hinzugefügt.\n");
                    start_variable_translate.put(to_replaced, "S" + start_variable_translate.size());
                }

                //Replace the startvariable
                replaced = help_method.replaceChar(i,next_rule_content, start_variable_translate.get(to_replaced));
                int replaced_index = output_rules.get(next_rule_variable).indexOf(next_rule_content);
                output_rules.get(next_rule_variable).set(replaced_index,replaced);
                log_output.append("Die Startvariable ").append(to_replaced).append(" wurde durch die Variable ").append(start_variable_translate.get(to_replaced)).append(" ersetzt.\n");
                next_rule_content = replaced;
            }
        }
        setNextState();
        return log_output.toString();
    }

    //delete every chainrule
    public String deleteChainRule(){
        //Define some inter variables
        StringBuilder log_output = new StringBuilder();

        //chainrule_state = 0: look for a single variable
        if (chainrule_state == 0) {
            String old_next_rule_terminal = next_rule_variable;

            //Check each rule for a single Variable
            while (old_next_rule_terminal.equals(next_rule_variable) &&next_rule==1) {
                if (output_variable.contains(next_rule_content)) {
                    if (!chain_rule_map.containsKey(next_rule_variable)) {
                        chain_rule_map.put(next_rule_variable, new ArrayList<>());
                    }
                    chain_rule_map.get(next_rule_variable).add(next_rule_content);
                    log_output.append("Es wurde für die Regel ").append(next_rule_variable).append(" die Kettenregel ").append(next_rule_content).append(" gefunden\n");
                }
                setNextState();
            }
        }
        //chainrule_state = 1: Add the rule to the new
        if(chainrule_state ==1){
            if(chain_rule_map.containsKey(next_rule_variable)) {

                //List with the rules to add
                List<String> replace_list = chain_rule_map.get(next_rule_variable);
                log_output.append("Die Kettenregel wird für ").append(next_rule_variable).append(" aufgelöst und die Regeln ").append(replace_list.toString()).append(" hinzugefügt\n");

                //For every Variable to replace
                for (String replace_variable : replace_list) {

                    //List with the rules for the variables
                    List<String> add_rule_list = output_rules.get(replace_variable);

                    //For every rule to add
                    for (String add_rule : add_rule_list) {

                        //Rule can not be a single Variable because we replaced them with the new rules
                        if (!output_variable.contains(add_rule)) {

                            //Check if the rule already exits
                            if (!output_rules.get(next_rule_variable).contains(add_rule)) {
                                output_rules.get(next_rule_variable).add(add_rule);
                                log_output.append("Es wurde ").append(add_rule).append(" bei ").append(next_rule_variable).append(" hinzugefügt.\n");
                            }
                        }
                    }
                }
            }
            for(String delete_var: output_variable){
                if (output_rules.get(next_rule_variable).contains(delete_var)) {
                    output_rules.get(next_rule_variable).remove(delete_var);
                    log_output.append("Wegen der Kettenregel wurde ").append(delete_var).append(" aus ").append(next_rule_variable).append(" entfernt\n");
                }
            }

            //Go to the next variable
            String old_next_var = next_rule_variable;
            while(old_next_var.equals(next_rule_variable) &&next_rule==1){
                setNextState();
            }

        }

        //handle the special states + output
        if(next_rule==2 && chainrule_state ==0){
            next_rule=1;
            chainrule_state =1;
            Map<String, List<String>> chain_rule_map_copy = chain_rule_map;
            for(String left_side: chain_rule_map_copy.keySet()) {
                chain_rule_list.clear();
                addToChainRule(left_side);
                chain_rule_map.get(left_side).clear();
                for(String a:chain_rule_list){
                    chain_rule_map.get(left_side).add(a);
                }
            }
            if(chain_rule_map.size()!= 0) {
                log_output.append("Alle Kettenregeln wurden erkannt und werden folgendermaßen augelöst:\n").append(chain_rule_map.toString()).append("\n");
            }
        }
        if(next_rule==2&& chainrule_state ==1){
            chainrule_state =2;}

        return log_output.toString();
    }

    //adds every chainrule
    public void addToChainRule(String left_side){
        if(chain_rule_map.containsKey(left_side)) {
            List<String> help_list = chain_rule_map.get(left_side);
            for (String variable : help_list) {
                if (!chain_rule_list.contains(variable)) {
                    chain_rule_list.add(variable);
                    addToChainRule(variable);
                }
            }
        }
    }

    //replace terminals with variables
    public String replaceTerminal(){
        //Define some inter variables
        StringBuilder log_output = new StringBuilder();
        String word_to_check = next_rule_content;
        boolean add_new_variable = true;
        int count_char_replace = 0;

        //Check for every char if its a variable or terminal
        for(int i=0; i<word_to_check.length();i++) {
            String char_to_check =String.valueOf(word_to_check.charAt(i));

            //check if the char is in the terminal list
            if (output_terminal.contains(char_to_check)&&word_to_check.length()>1) {

                //Check if already the replace variable exits
                for(String keys: start_variable_translate.keySet()){
                    if(keys.equals(char_to_check)){
                        add_new_variable = false;
                        break;
                    }
                }

                //Define new terminal and add it to the lists
                if(add_new_variable) {
                    List<String> rule_to_add = new ArrayList<>();
                    start_variable_translate.put(char_to_check,"T" + terminal_translate.size());
                    rule_to_add.add(char_to_check);
                    output_rules.put("T" + terminal_translate.size(), rule_to_add);
                    output_variable.add("T" + terminal_translate.size());
                    log_output.append("Es wurde die Hiflsvariable T").append(terminal_translate.size()).append(" erstellt und diese ersetzt ").append(char_to_check).append(".\n");
                    terminal_translate.put(char_to_check, "T" + terminal_translate.size());
                }

                String replace_Char = terminal_translate.get(char_to_check);
                String replace = help_method.replaceChar(i+count_char_replace, next_rule_content, replace_Char);
                log_output.append("Die Regel: "+next_rule_variable+"->"+next_rule_content+" wird zu "+next_rule_variable+"->"+replace+".\n");
                int j = output_rules.get(next_rule_variable).indexOf(next_rule_content);
                output_rules.get(next_rule_variable).set(j,replace);
                output_rules.put(replace_Char,new ArrayList<>());
                output_rules.get(replace_Char).add(char_to_check);
                next_rule_content=replace;
                count_char_replace++;
            }
        }

        setNextState();
        return log_output.toString();
    }

    //set length of a rule to a size of 2
    public String reduceLength(){
        //Define some variable
        StringBuilder log_output = new StringBuilder();
        String first_var = help_method.getVariable(0, next_rule_content);
        String second_var ="";
        if(first_var.length()<next_rule_content.length()) {
            second_var = help_method.getVariable(first_var.length(), next_rule_content);
        }

        //Check if the first variables are the complete rule
        if((first_var.length()+second_var.length())<next_rule_content.length()){
            String new_var;
            int i = 0;

            //Add a new variable
            while(true){
                new_var = "R"+i;
                i++;
                if(!output_variable.contains(new_var)){
                    break;
                }
            }
            output_variable.add(new_var);
            log_output.append("Neue Variable ").append(new_var).append(" wurde hinzugefügt.\n");

            //Add the rule for the new variable and replace old rule
            output_rules.put(new_var, new ArrayList<>());
            String new_rule = next_rule_content.substring(first_var.length());
            int index = output_rules.get(next_rule_variable).indexOf(next_rule_content);
            output_rules.get(next_rule_variable).set(index,first_var+new_var);
            output_rules.get(new_var).add(new_rule);
            log_output.append("Es wurde bei ").append(new_var).append(" die Regel").append(new_rule).append(" hinzugefügt.\n");

        }


        setNextState();
        return log_output.toString();
    }

    //Create new grammars
    public String createGrammars(){
        //Define some variables
        StringBuilder log_output = new StringBuilder();
        String new_var;
        String new_rule1;
        String new_rule2="";
        String new_rule;
        String first_var = help_method.getVariable(0, next_rule_content);
        String second_var ="";
        if(first_var.length()<next_rule_content.length()) {
            second_var = help_method.getVariable(first_var.length(), next_rule_content);
        }

        //Get next_grammar in a correct state
        while(output_start_variable.contains(next_grammar) || next_grammar.equals("")){
            //Initial if not done
            if(next_grammar.equals("")) {
                next_grammar = output_variable.get(0);
            }
            //Skip next_grammar if its a start-variable
            while(output_start_variable.contains(next_grammar)){
                setNextState();
                if(next_rule==5 && !next_grammar.equals(output_variable.get(output_variable.size() - 1))){
                    next_rule =4;
                    next_grammar = output_variable.get(output_variable.indexOf(next_grammar)+1);
                    return log_output.toString();
                }

            }
        }
        //Skip the rules from startvariables
        if(output_start_variable.contains(next_rule_variable)){
            while(output_start_variable.contains(next_rule_variable)){
                setNextState();
            }
            return log_output.toString();
        }

        //Check if there already exists a Map for this rule
        if(!grammar_rule.containsKey(next_grammar)){
            grammar_rule.put(next_grammar, new HashMap<>());

        }

        //Check if the variable and the grammar are the same
        if(next_grammar.equals(next_rule_variable)){

            //Rule 1: A->a to S(A)->a
            if(second_var.equals("")){
                new_var = "S("+ next_grammar +")";
                new_rule1 = first_var;
            }

            //Rule 4: A->CD to C(A)->D
            else{
                new_var = first_var+"("+ next_grammar +")";
                new_rule1 = second_var;
            }
            new_rule=new_rule1+new_rule2;

            //Add the rule
            if(!grammar_rule.get(next_grammar).containsKey(new_var)) {
                grammar_rule.get(next_grammar).put(new_var, new ArrayList<>());
                log_output.append("Variable " +new_var+" wurde der Grammatik "+next_grammar+"hinzugefügt.\n");
            }
            grammar_rule.get(next_grammar).get(new_var).add(new_rule);
            log_output.append("Grammatik "+next_grammar+"wurde die Regel "+new_var+"->"+new_rule+"hinzugefügt.\n");
        }

        //Rule 2: B->a to S(A)->aB(A)
        if(second_var.equals("")){
            new_var = "S("+ next_grammar +")";
            new_rule1 = first_var;
        }
        //Rule 3: B->CD to C(A)->DB(A)
        else{
            new_var = first_var+"("+ next_grammar +")";
            new_rule1 = second_var;
        }
        new_rule2 = next_rule_variable+"("+ next_grammar +")";
        new_rule=new_rule1+new_rule2;

        //Add the rule
        if(!grammar_rule.get(next_grammar).containsKey(new_var)) {
            grammar_rule.get(next_grammar).put(new_var, new ArrayList<>());
            log_output.append("Variable " +new_var+" wurde der Grammatik "+next_grammar+"hinzugefügt.\n");
        }
        grammar_rule.get(next_grammar).get(new_var).add(new_rule);
        log_output.append("Grammatik "+next_grammar+"wurde die Regel "+new_var+"->"+new_rule+"hinzugefügt.\n");

        //Set state to 4 if not every Grammar get generated
        setNextState();
        if(next_rule==5 && !next_grammar.equals(output_variable.get(output_variable.size() - 1))){
            next_rule =4;
            next_grammar = output_variable.get(output_variable.indexOf(next_grammar)+1);
        }
        return log_output.toString();
    }

    public String replaceFirstVar(){
        //Define some Variables
        StringBuilder log_output = new StringBuilder();
        String first_var = help_method.getVariable(0, next_rule_content);
        String second_var ="";
        if(first_var.length()<next_rule_content.length()) {
            second_var = help_method.getVariable(first_var.length(), next_rule_content);
        }

        //Save state and get next state to avoid problems when adding sth to list where the state are based on
        String old_next_rule_variable = next_rule_variable;
        String old_next_rule_content = next_rule_content;
        setNextState();

        //Check if replacement is needed
        if(!second_var.equals("") && output_variable.contains(first_var) && output_variable.contains(second_var)){
            String startvar = "S("+first_var+")";
            List<String> replace_list = grammar_rule.get(first_var).get(startvar);
            int index_of_var  = output_rules.get(old_next_rule_variable).indexOf(old_next_rule_content);
            output_rules.get(old_next_rule_variable).remove(old_next_rule_content);
            //Replace very first var with every rule S(var) generated in Grammar
            for(String replace_rule:replace_list){
                output_rules.get(old_next_rule_variable).add(index_of_var, replace_rule+second_var);
                log_output.append("Die Regel "+old_next_rule_variable+"->"+old_next_rule_content+" durch "+replace_rule+second_var+"ersetzt.\n");
                index_of_var++;
            }
        }
        return log_output.toString();
    }
}
