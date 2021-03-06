import java.util.*;

public class Logic {
    Hilfmethoden helpmethod= new Hilfmethoden();
    List<String> input_terminal_list = new ArrayList<>();
    List<String> input_variable_list = new ArrayList<>();
    List<String> input_start_variable_list = new ArrayList<>();
    Map<String, List<String>> input_rules_map = new HashMap<>();

    List<String> output_terminal_list = new ArrayList<>();
    List<String> output_variable_list = new ArrayList<>();
    List<String> output_start_variable_list = new ArrayList<>();
    Map<String, List<String>> output_rules_map = new HashMap<>();

    Map<String, List<String>> chain_rule_map = new HashMap<>();
    List<String> chain_rule_list = new ArrayList<>();

    Map<String,String> startvariable_translate = new HashMap<>();
    Map<String,String> terminal_translate = new HashMap<>();

    Map<String, Map<String, List<String>>> grammar_rule= new HashMap<>();



    final int count_rule = 5;
    int next_rule = 0;
    String next_rule_variable = "";
    String next_rule_content = "";
    String next_grammer = "";

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
        if(!input_variable_list.contains(rule_key)){
            log_output.append("Bitte eine Regel für eine Variable definieren, die auch als Variable deklariert wurde.\n");
            rule_key_input_valid = false;
        }

        //Check if rule value is valid
        for(String value: rule_value){
            for (int i = 0; i<value.length();i++) {
                char ch = value.charAt(i);
                if (!input_terminal_list.contains(String.valueOf(ch))  && !input_variable_list.contains(String.valueOf(ch))) {
                    log_output.append("Das Zeichen ").append(ch).append(" muss als Variable oder Terminal definiert sein.\n");
                    rule_value_input_valid = false;
                }
            }
        }

        // rule will be added if the input was correct
        if(rule_key_input_valid && rule_value_input_valid){
            if(!input_rules_map.containsKey(rule_key)){
                input_rules_map.put(rule_key, new ArrayList<>());
            }
            for(String a: rule_value){
                if(!input_rules_map.get(rule_key).contains(a)) {
                    input_rules_map.get(rule_key).add(a);
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

    //Checks the input and add a terminal, variable and the startvariable to their lists
    //terminal, variable and start_variable are the Input from their Textarea from the gui
    public String commit_input(String terminal, String variable, String start_variable){

        //Definition for intern variables
        StringBuilder log_output= new StringBuilder();

        boolean terminal_valid = true;
        boolean variable_valid = true;
        boolean start_variable_valid = true;

        //Clear the list so only the current data will be saved
        input_terminal_list.clear();
        input_variable_list.clear();
        input_start_variable_list.clear();

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
                input_terminal_list.add(a.toLowerCase());
            }
            log_output.append("Die Terminale ").append(input_terminal_list.toString()).append(" wurden erfolgreich hinzugefügt.\n");
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
                input_variable_list.add(a.toUpperCase());
            }
            log_output.append("Die Variablen ").append(input_variable_list.toString()).append(" wurden erfolgreich hinzugefügt.\n");
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
                if(!input_variable_list.contains(a.toUpperCase())){
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
                input_start_variable_list.add(a.toUpperCase());
            }
            log_output.append("Die Startvariable ").append(input_start_variable_list.toString()).append(" wurde erfolgreich hinzugefügt.\n");
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
        if(input_start_variable_list.size()==0){
            input_correct = false;
            log_output.append("Bitte mindestens eine Startvariable einfügen.\n");
        }
        if(input_variable_list.size()==0){
            input_correct = false;
            log_output.append("Bitte mindestens eine Variable einfügen.\n");
        }
        if(input_terminal_list.size()==0){
            input_correct = false;
            log_output.append("Bitte mindestens eine Terminale einfügen.\n");
        }
        if(input_rules_map.keySet().size()==0) {
            input_correct = false;
            log_output.append("Bitte mindestens eome Regel einfügen.\n");
        }

        //Check if startvariable is in variable
        for (String a : input_start_variable_list) {
            if (!input_variable_list.contains(a.toUpperCase())) {
                input_correct = false;
                log_output.append("Startvaribale ").append(a).append(" ist nicht in den Nichtterminalen definiert.\n");
            }
        }

        //Check if for every rule variable exists a defined variable
        Set<String> keys = input_rules_map.keySet();
        for(String key: keys){
            if (!input_variable_list.contains(key)) {
                input_correct = false;
                log_output.append("Es wurde für die Regel die Variable ").append(key).append(" verwedendet welche nicht definiert ist.\n");
            }
        }

        //If: Check if for every rule variable exists a defined variable
        //Else: Check if all terminals and variable used in the rules are defined
        for(String variable: input_variable_list){
            if (!keys.contains(variable)) {
                input_correct = false;
                log_output.append("Es wurde eine Variabel ").append(variable).append(" definiert, für welche es keine Regel gibt\n");
            }
            else{
                for(String a: input_rules_map.get(variable)){
                    for (int i = 0; i<a.length();i++) {
                        String ch = String.valueOf(a.charAt(i));
                        if (!input_terminal_list.contains(ch)  && !input_variable_list.contains(ch)) {
                            log_output.append("Das Zeichen ").append(ch).append(" muss als Nicht-Terminal oder Terminal definiert sein\n");
                            input_correct = false;
                        }
                    }
                }
            }
        }

        //Makes a log_output on succes and set init values
        if(input_correct){
            output_rules_map = input_rules_map;
            output_variable_list = input_variable_list;
            output_terminal_list = input_terminal_list;
            output_start_variable_list = input_start_variable_list;
            next_rule_variable = (String) output_rules_map.keySet().toArray()[0];
            next_rule_content = output_rules_map.get(next_rule_variable).get(0);
            log_output.append("Eingabe stimmt.");
        }

        //send the log and input back to the gui
        return log_output.toString();
    }

    //Resets all global variables
    public void clear(){
        input_terminal_list.clear();
        input_variable_list.clear();
        input_start_variable_list.clear();
        input_rules_map.clear();

        output_rules_map.clear();
        output_start_variable_list.clear();
        output_terminal_list.clear();
        output_variable_list.clear();

        copy_output_variable_list.clear();
        startvariable_map.clear();
        startvariable_translate.clear();
        terminal_translate.clear();
        chain_rule_map.clear();
        chain_rule_list.clear();
        grammar_rule.clear();
        chainrule_state =0;
        next_rule = 0;
        next_rule_variable = "";
        next_rule_content = "";
        next_grammer = "";
    }

    public List<String> all_step(){
        List<String> returnList = new ArrayList<>();
        StringBuilder log_output = new StringBuilder();

        for(int i = next_rule; i<=count_rule; i++ ){
            log_output.append(rule_step().get(0));
        }

        returnList.add(log_output.toString().replace("[", "").replace("]", ""));
        returnList.add(getOutputString());
        return returnList;
    }
    public List<String> rule_step(){
        List<String> returnList = new ArrayList<>();
        StringBuilder log_output = new StringBuilder();
        switch(next_rule) {
            case 0:
                while(next_rule==0){
                   log_output.append(multi_step().get(0));
                }
                break;
            case 1:
                while(next_rule==1){
                    log_output.append(multi_step().get(0));
                }
                break;
            case 2:
                while(next_rule==2){
                    log_output.append(multi_step().get(0));
                }
                break;
            case 3:
                while(next_rule==3){
                    log_output.append(multi_step().get(0));
                }
                break;
            case 4:
                while(next_rule==4){
                    log_output.append(multi_step().get(0));
                }
                break;
            case 5:
                while(next_rule==5){
                    log_output.append(multi_step().get(0));
                }

        }
        returnList.add(log_output.toString().replace("[","").replace("]",""));
        returnList.add(getOutputString());
        return returnList;
    }
    public List<String> multi_step(){
        List<String> returnList = new ArrayList<>();
        StringBuilder log_output = new StringBuilder();
        if(next_rule!=1 && next_rule != 6) {
            int index = output_rules_map.get(next_rule_variable).indexOf(next_rule_content);
            for (int i = index; i < output_rules_map.get(next_rule_variable).size(); i++) {
                log_output.append(single_step().get(0));
            }
        }
        else if(next_rule==1){
                while(chainrule_state ==0){
                    log_output.append(single_step().get(0));
                }
                while(chainrule_state ==1){
                    log_output.append(single_step().get(0));
                }
        }
        else{
            log_output.append("Umformung ist fertig!\n");
        }
        returnList.add((log_output.toString().replace("[", "").replace("]", "")));
        returnList.add(getOutputString());
        return returnList;
    }
    public List<String> single_step(){
        List<String> returnList = new ArrayList<>();
        returnList.clear();
        String log_output = "";

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
                log_output = "Umformung ist fertig!\n";

        }

        log_output =log_output.replace("[","").replace("]","").replace("{","").replace("}","");
        returnList.add(log_output);
        returnList.add(getOutputString());
        return returnList;

    }

    //returns the string for the input-textarea
    public String getInputString(){
        StringBuilder rule = new StringBuilder();
        for(String a: input_variable_list){
            if(input_rules_map.containsKey(a)){
                rule.append("\t").append(a).append(": ").append(input_rules_map.get(a).toString()).append("\n");
            }
        }
        return "Terminal:\t"+ input_terminal_list.toString()+"\n"+
                "Variable:\t" + input_variable_list.toString() + "\n" +
                "Startvariable:\t" + input_start_variable_list.toString() + "\n"+
                "Regeln" + rule;
    }

    //returns the string for the output-textarea
    public String getOutputString(){
        StringBuilder rule = new StringBuilder();
        for(String a: output_variable_list){
            if(output_rules_map.containsKey(a)){
                rule.append("\t").append(a).append(": ").append(output_rules_map.get(a).toString()).append("\n");
            }
        }
        String next_rule_output = "";
        switch (next_rule){
            case 0:
                next_rule_output = "Startvariable auf rechter Seite entfernen.";
                break;
            case 1:
                next_rule_output = "Entferne Kettenregel.";
                break;
            case 2:
                next_rule_output = "Ersetze Terminale durch eine Variable";
                break;
            case 3:
                next_rule_output = "Erstelle nur Regeln der Länge 2";
                break;
            case 4:
                next_rule_output = "Erstelle neue Grammatiken für "+next_grammer;
                break;
            case 5:
                next_rule_output = "Ersetzte die erste Variable";


        }
        StringBuilder output = new StringBuilder("Nächster Schritt:" + next_rule_output + "\n" +
                "Nächste Regel:" + next_rule_variable + ": " + next_rule_content + "\n\n" +
                "Terminal:\t" + output_terminal_list.toString() + "\n" +
                "Variable:\t" + output_variable_list.toString() + "\n" +
                "Startvariable:\t" + output_start_variable_list.toString() + "\n" +
                "Regeln" + rule + "\n");
        if(next_rule==4){
            output.append("Neue Regeln:\n");
            for(String a: output_variable_list){
                if(grammar_rule.containsKey(a)){
                    output.append(a).append(": ").append(grammar_rule.get(a)).append("\n");
                }
            }
        }
        if(next_rule==5){
            output.append("Startvariablen:\n");
            for(String a: output_variable_list){
                if(startvariable_map.containsKey("S("+a+")")){
                    output.append("S("+a+")").append(": ").append(startvariable_map.get("S("+a+")")).append("\n");
                }
            }
        }
        return output.toString();
    }

    //set the next state
    public void setNextState(){
        int key_index = 0;
        int value_index =output_rules_map.get(next_rule_variable).indexOf(next_rule_content);


        if(value_index+1 <output_rules_map.get(next_rule_variable).size()){
            next_rule_content = output_rules_map.get(next_rule_variable).get(value_index+1);
        }
        else {
            for(int i = 0; i < output_variable_list.size(); i++) {
                if(output_variable_list.get(i).equals(next_rule_variable)) {
                    key_index = i;
                    break;
                }
            }
            if(key_index+1<output_variable_list.size()){
                next_rule_variable =output_variable_list.get(key_index+1);
            }
            else{
                next_rule =next_rule+1;
                next_rule_variable =output_variable_list.get(0);
            }
            next_rule_content = output_rules_map.get(next_rule_variable).get(0);
        }

    }

    //replace every startvariable on the right side of a rule
    public String replaceStartvariable(){

        //Define some inter variables
        boolean add_new_variable = true;
        StringBuilder log_output = new StringBuilder();
        String to_replaced;
        String replaced;

        //Check for each letter if its contain a startvariable
        for(int i=0; i<next_rule_content.length(); i++) {
            to_replaced = String.valueOf(next_rule_content.charAt(i));
            if(output_start_variable_list.contains(to_replaced)){

                //Check if already a replace variable exits
                for(String keys: startvariable_translate.keySet()){
                    if(keys.equals(to_replaced)){
                        add_new_variable = false;
                        break;
                    }
                }

                //Define new terminal and add it to the lists
                if(add_new_variable) {
                    output_rules_map.put("S" + startvariable_translate.size(), output_rules_map.get(to_replaced));
                    output_variable_list.add("S" + startvariable_translate.size());
                    log_output.append("Es wurde die Hiflsvariable S").append(startvariable_translate.size()).append(" erstellt und alle Regeln von ").append(to_replaced).append(" dieser hinzugefügt.\n");
                    startvariable_translate.put(to_replaced, "S" + startvariable_translate.size());
                }

                //Replace the startvariable
                replaced = helpmethod.replaceChar(i,next_rule_content, startvariable_translate.get(to_replaced));
                int replaced_index = output_rules_map.get(next_rule_variable).indexOf(next_rule_content);
                output_rules_map.get(next_rule_variable).set(replaced_index,replaced);
                log_output.append("Die Startvariable ").append(to_replaced).append(" wurde durch die Variable ").append(startvariable_translate.get(to_replaced)).append(" ersetzt.\n");
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

            //Check for each rule for a single Variable
            while (old_next_rule_terminal.equals(next_rule_variable) &&next_rule==1) {
                if (output_variable_list.contains(next_rule_content)) {
                    if (!chain_rule_map.containsKey(next_rule_variable)) {
                        chain_rule_map.put(next_rule_variable, new ArrayList<>());
                    }
                    chain_rule_map.get(next_rule_variable).add(next_rule_content);
                }
                setNextState();
            }
        }

        if(chainrule_state ==1){
            if(chain_rule_map.containsKey(next_rule_variable)){

                //bekommt Liste mit den einzusetzeden Variablen
                List<String> replace_list = chain_rule_map.get(next_rule_variable);

                //für jeden Eintrag der einzusetzenden Variablen
                for(String replace_variable: replace_list){

                    //Liste mit Regel pro einzusetzende Variable
                    List<String> add_rule_list = output_rules_map.get(replace_variable);
                    //list with the rule to add
                    for(String add_rule :add_rule_list){
                        //Es darf kein einzelnes Terminal sein
                        if(!output_variable_list.contains(add_rule)){
                            //Es darf die Regel darf nicht doppelt vorkommen
                            if(!output_rules_map.get(next_rule_variable).contains(add_rule)){
                                output_rules_map.get(next_rule_variable).add(add_rule);
                                log_output.append("Es wurde ").append(add_rule).append(" bei ").append(next_rule_variable).append(" hinzugefügt.\n");
                            }
                        }
                    }
                }

            }
            for(String delete_var:output_variable_list){
                output_rules_map.get(next_rule_variable).remove(delete_var);
            }
            String old_next_var = next_rule_variable;
            while(old_next_var.equals(next_rule_variable) &&next_rule==1){
                setNextState();
            }

        }
        if(next_rule==2&& chainrule_state ==0){
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
                log_output.append("kettenregel aufgelöst, Chainrulestate=").append(chainrule_state);
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

    //replace
    public String replaceTerminal(){
        //Define some inter variables
        StringBuilder log_output = new StringBuilder();
        String word_to_check = next_rule_content;
        boolean add_new_variable = true;
        int count_char_replace = 0;
        for(int i=0; i<word_to_check.length();i++) {
            String char_to_check =String.valueOf(word_to_check.charAt(i));
            if (output_terminal_list.contains(char_to_check)&&word_to_check.length()>1) {

                //Check if already a replace variable exits
                for(String keys: startvariable_translate.keySet()){
                    if(keys.equals(char_to_check)){
                        add_new_variable = false;
                        break;
                    }
                }

                //Define new terminal and add it to the lists
                if(add_new_variable) {
                    List<String> rule_to_add = new ArrayList<>();
                    startvariable_translate.put(char_to_check,"T" + terminal_translate.size());
                    rule_to_add.add(char_to_check);
                    output_rules_map.put("T" + terminal_translate.size(), rule_to_add);
                    output_variable_list.add("T" + terminal_translate.size());
                    log_output.append("Es wurde die Hiflsvariable T").append(terminal_translate.size()).append(" erstellt und diese ersetzt ").append(char_to_check).append(".\n");
                    terminal_translate.put(char_to_check, "T" + terminal_translate.size());
                }

                String replace_Char = terminal_translate.get(char_to_check);
                String replace = helpmethod.replaceChar(i+count_char_replace, next_rule_content, replace_Char);
                int j = output_rules_map.get(next_rule_variable).indexOf(next_rule_content);
                output_rules_map.get(next_rule_variable).set(j,replace);
                output_rules_map.put(replace_Char,new ArrayList<>());
                output_rules_map.get(replace_Char).add(char_to_check);
                next_rule_content=replace;
                count_char_replace++;
            }
        }

        setNextState();
        return log_output.toString();
    }

    //set length of a rule to a size of 2
    public String reduceLength(){
        StringBuilder log_output = new StringBuilder();

        String first_var = helpmethod.getVariable(0, next_rule_content);
        String second_var ="";
        if(first_var.length()<next_rule_content.length()) {
            second_var = helpmethod.getVariable(first_var.length(), next_rule_content);
        }

        if((first_var.length()+second_var.length())<next_rule_content.length()){
            String new_var;
            int i = 0;
            while(true){
                new_var = "R"+i;
                i++;
                if(!output_variable_list.contains(new_var)){
                    break;
                }
            }
            output_variable_list.add(new_var);
            log_output.append("Neue Variable ").append(new_var).append(" wurde hinzugefügt.\n");
            output_rules_map.put(new_var, new ArrayList<>());
            String new_rule = next_rule_content.substring(first_var.length());
            int index = output_rules_map.get(next_rule_variable).indexOf(next_rule_content);
            output_rules_map.get(next_rule_variable).set(index,first_var+new_var);
            output_rules_map.get(new_var).add(new_rule);
            log_output.append("Die Regel "+new_var+"->"+new_rule+" wurde hinzugefügt.\n");

        }


        setNextState();
        return log_output.toString();
    }
    List<String> copy_output_variable_list = new ArrayList<>();
    Map<String, List<String>> startvariable_map = new HashMap<>();

    //Create new grammars
    public String createGrammars(){
        StringBuilder log_output = new StringBuilder();
        //copy_output_variable_list = output_variable_list;

        while(output_start_variable_list.contains(next_grammer) || next_grammer.equals("")){
            if(next_grammer.equals("")) {
                next_grammer = output_variable_list.get(0);
            }
            while(output_start_variable_list.contains(next_grammer)){
                setNextState();
                if(next_rule==5 && !next_grammer.equals(output_variable_list.get(output_variable_list.size() - 1))){
                    next_rule =4;
                    next_grammer = output_variable_list.get(output_variable_list.indexOf(next_grammer)+1);
                    return log_output.toString();
                }

            }
        }
        if(output_start_variable_list.contains(next_rule_variable)){
            while(output_start_variable_list.contains(next_rule_variable)){
                setNextState();
            }
            return log_output.toString();
        }

        String first_var = helpmethod.getVariable(0, next_rule_content);
        String second_var ="";
        if(first_var.length()<next_rule_content.length()) {
            second_var = helpmethod.getVariable(first_var.length(), next_rule_content);
        }

        if(!grammar_rule.containsKey(next_grammer)){
            grammar_rule.put(next_grammer, new HashMap<>());

        }
        String new_var;
        String new_rule1;
        String new_rule2="";
        String new_rule;
        if(next_grammer.equals(next_rule_variable)){
            //Regel Nummer 1
            if(second_var.equals("")){
                new_var = "S("+next_grammer+")";
                new_rule1 = first_var;
                log_output.append("Regel 1:");
            }
            //Regel Nummer 4
            else{
                new_var = first_var+"("+next_grammer+")";
                new_rule1 = second_var;
                log_output.append("Regel 4:");
            }
            //Add the variable
            new_rule=new_rule1+new_rule2;
            if(!grammar_rule.get(next_grammer).containsKey(new_var)) {
                grammar_rule.get(next_grammer).put(new_var, new ArrayList<>());
            }
            //Add the rule
            grammar_rule.get(next_grammer).get(new_var).add(new_rule);
            log_output.append(" Aus "+next_rule_variable+"->"+next_rule_content+" wird " +new_var+"-> "+new_rule+"\n");


        }

        //Regel Nummer 2
        if(second_var.equals("")){
            new_var = "S("+next_grammer+")";
            new_rule1 = first_var;
            log_output.append("Regel 2:");
            //Add the rule
            if(!grammar_rule.get(next_grammer).containsKey(new_var)) {
                grammar_rule.get(next_grammer).put(new_var, new ArrayList<>());
            }
        }
        //Regel Nummer 3
        else{
            new_var = first_var+"("+next_grammer+")";
            new_rule1 = second_var;
            log_output.append("Regel 3:");
            //Add the variable
            if(!copy_output_variable_list.contains(new_var)){
                copy_output_variable_list.add(new_var);
            }
        }
        new_rule2 = next_rule_variable+"("+next_grammer+")";
        //Add the variable
        if(!copy_output_variable_list.contains(new_rule2)){
            copy_output_variable_list.add(new_rule2);
        }
        new_rule=new_rule1+new_rule2;

        //Add the rule
        if(!grammar_rule.get(next_grammer).containsKey(new_var)) {
            grammar_rule.get(next_grammer).put(new_var, new ArrayList<>());
        }
        grammar_rule.get(next_grammer).get(new_var).add(new_rule);
        log_output.append(" Aus "+next_rule_variable+"->"+next_rule_content+" wird " +new_var+"-> "+new_rule+"\n");

        setNextState();
        if(next_rule==5 && !next_grammer.equals(output_variable_list.get(output_variable_list.size() - 1))){
            next_rule =4;
            next_grammer = output_variable_list.get(output_variable_list.indexOf(next_grammer)+1);
        }
        if(next_rule==5 && next_grammer.equals(output_variable_list.get(output_variable_list.size() - 1))){
            //output_variable_list = copy_output_variable_list;
            log_output.append("Es werden nun alle aus Regel 2 und 3 entstanden Regeln der Grammatik hinzugefügt.\n");
            for(int i=0;i<output_variable_list.size();i++){
                String grammar = output_variable_list.get(i);
                if(grammar_rule.containsKey(grammar)){
                    Map<String, List<String>> grammer_map =  grammar_rule.get(grammar);
                    for(Object key :grammer_map.keySet()){
                        if(!(key.toString().charAt(0)=='S')){
                            for(int j=0; j<grammer_map.get(key.toString()).size();j++){
                                if(!output_rules_map.containsKey(key.toString())){
                                    output_rules_map.put(key.toString(),new ArrayList<>());
                                }
                                if(!output_variable_list.contains(key.toString())){
                                    output_variable_list.add(key.toString());
                                }
                                output_rules_map.get(key.toString()).add(grammer_map.get(key.toString()).get(j));
                            }
                        }

                    }
                    for(Object key :grammer_map.keySet()){
                        if((key.toString().charAt(0)=='S')){
                            startvariable_map.put(key.toString(),grammar_rule.get(grammar).get(key.toString()));
                        }

                    }

                }
            }
        }
        return log_output.toString();
    }

    public String replaceFirstVar(){
        StringBuilder log_output = new StringBuilder();

        String first_var = helpmethod.getVariable(0, next_rule_content);
        String second_var ="";
        if(first_var.length()<next_rule_content.length()) {
            second_var = next_rule_content.substring(first_var.length());
        }
        String old_next_rule_variable = next_rule_variable;
        String old_next_rule_content = next_rule_content;
        setNextState();

        if(!second_var.equals("") && output_variable_list.contains(first_var)){
            String startvar = "S("+first_var+")";
            List<String> replace_list = grammar_rule.get(first_var).get(startvar);
            int index_of_var  = output_rules_map.get(old_next_rule_variable).indexOf(old_next_rule_content);
            output_rules_map.get(old_next_rule_variable).remove(old_next_rule_content);
            log_output.append("Für die Regel " +old_next_rule_variable+"->"+old_next_rule_content+" wird "+first_var+" durch "+replace_list.toString()+" ersetzt.\n");
            for(String replace_rule:replace_list){
                output_rules_map.get(old_next_rule_variable).add(index_of_var, replace_rule+second_var);
                index_of_var++;
            }
        }
        if(second_var.equals("") && output_variable_list.contains(first_var)){
            String startvar = "S("+first_var+")";
            List<String> replace_list = grammar_rule.get(first_var).get(startvar);
            int index_of_var  = output_rules_map.get(old_next_rule_variable).indexOf(old_next_rule_content);
            output_rules_map.get(old_next_rule_variable).remove(old_next_rule_content);
            log_output.append("Für die Regel " +old_next_rule_variable+"->"+old_next_rule_content+" wird "+first_var+" durch "+replace_list.toString()+" ersetzt.\n");
            for(String replace_rule:replace_list){
                output_rules_map.get(old_next_rule_variable).add(index_of_var, replace_rule);
                index_of_var++;
            }
        }

        return log_output.toString();
    }
}
