import java.util.*;

public class Logic {
    Hilfmethoden helpmethod= new Hilfmethoden();
    List<String> input_terminal_list = new ArrayList<String>();
    List<String> input_variable_list = new ArrayList<String>();
    List<String> input_start_variable_list = new ArrayList<String>();
    Map<String, List<String>> input_rules_map = new HashMap<String, List<String>>();

    List<String> output_terminal_list = new ArrayList<String>();
    List<String> output_variable_list = new ArrayList<String>();
    List<String> output_start_variable_list = new ArrayList<String>();
    Map<String, List<String>> output_rules_map = new HashMap<String, List<String>>();

    Map<String, List<String>> chain_rule_map = new HashMap<String, List<String>>();
    List<String> chain_rule_list = new ArrayList<String>();
    Dictionary startvariable_translate = new Hashtable();
    Dictionary terminal_translate = new Hashtable();


    final int count_rule = 3;
    int next_rule = 0;
    String next_rule_variable = "";
    String next_rule_content = "";

    int chainrule_state = 0;



    //Checks the input and add a rule to the input_rule_map
    //rule_input is a rule from the gui, which should be wrote in a certain way. (Variable:rule1,rule2,rule3...)
    public List confirm_rule(String rule_input ){

        //Definition for intern variables
        List<String> returnList = new ArrayList<String>();
        String log_output = "";
        boolean rule_key_input_valid = true;
        boolean rule_value_input_valid = true;

        //Split input to rule_key and rule_value
        rule_input = rule_input.replaceAll("[^a-zA-Z],:", "");
        String rule_key =rule_input.split(":")[0].trim().toUpperCase();
        String[] rule_value = rule_input.split(":")[1].trim().split(",");

        //Check if rule key is valid
        if(rule_key.length()!=1){
            log_output += "Bitte eine Regel eingeben.\n";
            rule_key_input_valid = false;
        }
        if(!input_variable_list.contains(rule_key)){
            log_output += "Bitte eine Regel für eine Variable definieren, die auch als Variable deklariert wurde.\n";
            rule_key_input_valid = false;
        }

        //Check if rule value is valid
        for(String value: rule_value){
            for (int i = 0; i<value.length();i++) {
                char ch = value.charAt(i);
                if (!input_terminal_list.contains(String.valueOf(ch))  && !input_variable_list.contains(String.valueOf(ch))) {
                    log_output += "Das Zeichen " + ch + " muss als Variable oder Terminal definiert sein.\n";
                    rule_value_input_valid = false;
                }
            }
        }

        // rule will be added if the input was correct
        if(rule_key_input_valid && rule_value_input_valid){
            if(!input_rules_map.containsKey(rule_key)){
                input_rules_map.put(rule_key, new ArrayList<String>());
            }
            for(String a: rule_value){
                if(!input_rules_map.get(rule_key).contains(a)) {
                    input_rules_map.get(rule_key).add(a);
                    log_output += "Für " + rule_key + " wurde die Regel " + a + " hizugefügt.\n";
                }
                else{
                    log_output += "Für " + rule_key + " existiert die Regel " + a + " bereits.\n";
                }
            }
        }

        //send the log and input back to the gui
        returnList.add(log_output);
        returnList.add(getInputString());
        return returnList;
    }

    //Checks the input and add a terminal, variable and the startvariable to their lists
    //terminal, variable and start_variable are the Input from their Textarea from the gui
    public List commit_input(String terminal, String variable, String start_variable){

        //Definition for intern variables
        List<String> returnList = new ArrayList<String>();
        String log_output= "";

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
            log_output +="Bitte bei den Terminalen etwas eingeben.\n";
            terminal_valid = false;
        }
        else {
            terminal = terminal.trim();
            terminal_array = terminal.split(",");

            for (String a : terminal_array) {
                if (a.length() != 1) {
                    terminal_valid = false;
                    log_output+= "Bitte bei Terminalen nur einzelne Buchstaben eingeben.\n";
                }
            }
        }

        //Add terminals if valid
        if (terminal_valid == true) {
            for (String a : terminal_array) {
                input_terminal_list.add(a.toLowerCase());
            }
            log_output+="Die Terminale " + input_terminal_list.toString() +" wurden erfolgreich hinzugefügt.\n";
        }

        //Check if the input for variables are correct
        String[] not_terminal_array = null;
        variable = variable.replaceAll("[^a-zA-Z],", "");
        if(variable.length()==0){
            log_output +="Bitte bei den Variablen etwas eingeben.\n";
            variable_valid = false;
        }
        else {
            variable = variable.trim();
            not_terminal_array = variable.split(",");
            for (String a : not_terminal_array) {
                if (a.length() != 1) {
                    variable_valid = false;
                    log_output+= "Bitte bei den Variablen nur einzelne Buchstaben eingeben.\n";
                }
            }
        }

        //add variables if valid
        if (variable_valid == true) {
            for (String a : not_terminal_array) {
                input_variable_list.add(a.toUpperCase());
            }
            log_output += "Die Variablen "+ input_variable_list.toString() +" wurden erfolgreich hinzugefügt.\n";
        }

        //Check if the input for start_variable are correct
        String[] start_var_array = null;
        start_variable = start_variable.replaceAll("[^a-zA-Z],", "");
        if(start_variable.length()==0){
            log_output +="Bitte bei der Startvariablen etwas eingeben.\n";
            start_variable_valid = false;
        }
        else {
            start_variable = start_variable.trim();
            start_var_array= start_variable.split(",");
            for (String a : start_var_array) {
                if(!input_variable_list.contains(a.toUpperCase())){
                    start_variable_valid = false;
                    log_output+= "Startvaribale " + a +" ist nicht in den Nichtterminalen definiert.\n";
                }
                if (a.length() != 1) {
                    start_variable_valid = false;
                    log_output+= "Bitte bei der Startvariablen nur einzelne Buchstaben eingeben.\n";
                }
            }
        }

        //add start_variables if valid
        if (start_variable_valid) {
            for (String a : start_var_array) {
                input_start_variable_list.add(a.toUpperCase());
            }
            log_output += "Die Startvariable "+input_start_variable_list.toString() +" wurde erfolgreich hinzugefügt.\n";
        }

        //send the log and input back to the gui
        returnList.add(log_output);
        returnList.add(getInputString());
        return returnList;
    }

    //Makes a final input check before the input will be used
    public List check_input(){

        //Definition for intern variables
        List<String> returnList = new ArrayList<String>();
        String log_output = "";
        boolean input_correct = true;

        //Check if input is empty
        if(input_start_variable_list.size()==0){
            input_correct = false;
            log_output += "Bitte mindestens eine Startvariable einfügen.\n";
        }
        if(input_variable_list.size()==0){
            input_correct = false;
            log_output += "Bitte mindestens eine Variable einfügen.\n";
        }
        if(input_terminal_list.size()==0){
            input_correct = false;
            log_output += "Bitte mindestens eine Terminale einfügen.\n";
        }
        if(input_rules_map.keySet().size()==0) {
            input_correct = false;
            log_output += "Bitte mindestens eome Regel einfügen.\n";
        }

        //Check if startvariable is in variable
        for (String a : input_start_variable_list) {
            if (!input_variable_list.contains(a.toUpperCase())) {
                input_correct = false;
                log_output += "Startvaribale " + a + " ist nicht in den Nichtterminalen definiert.\n";
            }
        }

        //Check if for every rule variable exists a defined variable
        Set<String> keys = input_rules_map.keySet();
        for(String key: keys){
            if (!input_variable_list.contains(key)) {
                input_correct = false;
                log_output += "Es wurde für die Regel die Variable "+ key +" verwedendet welche nicht definiert ist.\n";
            }
        }

        //If: Check if for every rule variable exists a defined variable
        //Else: Check if all terminals and variable used in the rules are defined
        for(String variable: input_variable_list){
            if (!keys.contains(variable)) {
                input_correct = false;
                log_output += "Es wurde eine Variabel " + variable +" definiert, für welche es keine Regel gibt\n";
            }
            else{
                for(String a: input_rules_map.get(variable)){
                    for (int i = 0; i<a.length();i++) {
                        char ch = a.charAt(i);
                        if (!input_terminal_list.contains(String.valueOf(ch))  && !input_variable_list.contains(String.valueOf(ch))) {
                            log_output += "Das Zeichen " + ch + " muss als Nicht-Terminal oder Terminal definiert sein\n";
                            input_correct = false;
                        }
                        if (input_start_variable_list.contains(ch)) {
                            log_output += "Das Startsymbol " + ch + " darf nicht auf der rechten Seite der Regel stehen\n";
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
            log_output += "Eingabe stimmt.";
        }

        //send the log and input back to the gui
        returnList.add(log_output);
        returnList.add(getInputString());
        if(input_correct){
            returnList.add(getOutputString());
        }
        return returnList;
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

        Enumeration keys = startvariable_translate.keys();
        while(keys.hasMoreElements()){
            startvariable_translate.remove(keys.nextElement());
        }
        Enumeration keys1 = startvariable_translate.keys();
        while(keys1.hasMoreElements()){
            startvariable_translate.remove(keys1.nextElement());
        }
        chain_rule_map.clear();
        chain_rule_list.clear();
        chainrule_state=0;
        next_rule = 0;
        next_rule_variable = "";
        next_rule_content = "";
    }

    public List all_step(){
        List<String> returnList = new ArrayList<String>();
        String log_output = "";

        for(int i = next_rule; i<count_rule; i++ ){
            log_output+=rule_step().get(0);
        }

        log_output.replace("[","").replace("]","");
        returnList.add(log_output);
        returnList.add(getOutputString());
        return returnList;
    }
    public List rule_step(){
        List<String> returnList = new ArrayList<String>();
        String log_output = "";

        switch(next_rule) {
            case 0:
                while(next_rule==0){
                    multi_step();
                }
                break;
            case 1:
                while(next_rule==1){
                    multi_step();
                }
                break;
            case 2:
                while(next_rule==2){
                    multi_step();
                }
                break;
        }

        log_output = log_output.replace("[","").replace("]","");
        returnList.add(log_output);
        returnList.add(getOutputString());
        return returnList;
    }
    public List multi_step(){
        List<String> returnList = new ArrayList<String>();
        String log_output = "";
        String actual_terminal = next_rule_variable;
        switch(next_rule) {
            case 0:
                for (String rule : output_rules_map.get(next_rule_variable)) {
                    if(actual_terminal!= next_rule_variable){
                        break;
                    }
                    log_output+= single_step().get(0);
                }
                break;
            case 1:
                while(chainrule_state==0){
                    log_output+= single_step().get(0);
                }
                while(chainrule_state==1){
                    log_output+= single_step().get(0);
                }

                break;
            case 2:
                for(int i=0; i<output_rules_map.get(next_rule_variable).size();i++){
                    log_output+= single_step().get(0);
                }
                break;
        }

        log_output.replace("[","").replace("]","");
        returnList.add(log_output);
        returnList.add(getOutputString());
        return returnList;
    }
    public List single_step(){
        List<String> returnList = new ArrayList<String>();
        String log_output = "";

        switch(next_rule) {
            case 0:
                log_output+=replaceStartvariable();
                break;
            case 1:
                log_output+=deleteChainRule()+ chain_rule_map.toString()+"\n";
                break;
            case 2:
                log_output+=replaceTerminal()+ terminal_translate.toString()+"\n";
                break;
        }

        log_output.replace("[","").replace("]","");
        returnList.add(log_output);
        returnList.add(getOutputString());
        return returnList;

    }

    //returns the string for the input-textarea
    public String getInputString(){
        String rule = "";
        for(String a: input_variable_list){
            if(input_rules_map.containsKey(a)){
                rule += "\t" + a +": " + input_rules_map.get(a).toString() +"\n";
            }
        }
        String value =

                "Terminal:\t"+ input_terminal_list.toString()+"\n"+
                        "Variable:\t" + input_variable_list.toString() + "\n" +
                        "Startvariable:\t" + input_start_variable_list.toString() + "\n"+
                        "Regeln" + rule;
        return value;
    }

    //returns the string for the output-textarea
    public String getOutputString(){
        String rule = "";
        for(String a: output_variable_list){
            if(output_rules_map.containsKey(a)){
                rule += "\t" + a +": " + output_rules_map.get(a).toString() +"\n";
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


        }

        String value =
                "Nächster Schritt:"+ next_rule_output +"\n"+
                        "Nächste Regel:" + next_rule_variable + ": "+next_rule_content+"\n\n"+
                        "Terminal:\t"+ output_terminal_list.toString()+"\n"+
                        "Variable:\t" + output_variable_list.toString() + "\n" +
                        "Startvariable:\t" + output_start_variable_list.toString() + "\n"+
                        "Regeln" + rule;
        return value;
    }

    //set the next state
    public void setNextState(){
        Object[] a = output_rules_map.keySet().toArray();
        String[] keys = new String[a.length];
        System.arraycopy(a,0,keys,0,a.length);
        int key_index = 0;
        int value_index =output_rules_map.get(next_rule_variable).indexOf(next_rule_content);
        int b=output_rules_map.get(next_rule_variable).size();

        if(value_index+1 <output_rules_map.get(next_rule_variable).size()){
            next_rule_content = output_rules_map.get(next_rule_variable).get(value_index+1);
        }
        else {
            for(int i = 0; i < keys.length; i++) {
                if(keys[i] == next_rule_variable) {
                    key_index = i;
                    break;
                }
            }
            if(key_index+1< keys.length){
                next_rule_variable =keys[key_index+1];
            }
            else{
                next_rule =next_rule+1;
                next_rule_variable =keys[0];
            }
            next_rule_content = output_rules_map.get(next_rule_variable).get(0);
        }

    }

    //replace every startvariable on the right side of a rule
    public List replaceStartvariable(){

        //Define some inter variables
        List<String> returnList = new ArrayList<String>();
        boolean add_new_variable = true;
        String log_output = "";
        String to_replaced = "";
        String replaced = null;

        //Check for each letter if its contain a startvariable
        for(int i=0; i<next_rule_content.length(); i++) {
            to_replaced = String.valueOf(next_rule_content.charAt(i));
            if(output_start_variable_list.contains(to_replaced)){

                //Check if already a replace variable exits
                Enumeration keys = startvariable_translate.keys();
                while (keys.hasMoreElements()){
                    String t =(String) keys.nextElement();
                    if(t.equals(to_replaced)){
                        add_new_variable = false;
                    }
                }

                //Define new terminal and add it to the lists
                if(add_new_variable) {
                    output_rules_map.put("S" + startvariable_translate.size(), output_rules_map.get(to_replaced));
                    output_variable_list.add("S" + startvariable_translate.size());
                    log_output += "Es wurde die Hiflsvariable S" + startvariable_translate.size() + " erstellt und alle Regeln von " + to_replaced + " dieser hinzugefügt.\n";
                    startvariable_translate.put(to_replaced, "S" + startvariable_translate.size());
                }

                //Replace the startvariable
                replaced = helpmethod.replaceChar(i,next_rule_content, (String) startvariable_translate.get(to_replaced));
                int replaced_index = output_rules_map.get(next_rule_variable).indexOf(next_rule_content);
                output_rules_map.get(next_rule_variable).set(replaced_index,replaced);
                log_output += "Die Startvariable "+to_replaced+" wurde durch die Variable " + (String) startvariable_translate.get(to_replaced) + " ersetzt.\n";
                next_rule_content = replaced;
            }
        }
        setNextState();
        returnList.add(log_output);
        return returnList;
    }

    //delete every chainrule
    public List deleteChainRule(){
        //Define some inter variables
        List<String> returnList = new ArrayList<String>();
        String log_output = "";

        //chainrule_state = 0: look for a single variable
        if (chainrule_state == 0) {
            String old_next_rule_terminal = next_rule_variable;

            //Check for each rule for a single Variable
            while (old_next_rule_terminal == next_rule_variable &&next_rule==1) {
                if (output_variable_list.contains(next_rule_content)) {
                    if (!chain_rule_map.containsKey(next_rule_variable)) {
                        chain_rule_map.put(next_rule_variable, new ArrayList<String>());
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
                                log_output += "Es wurde " + add_rule+ " bei " +next_rule_variable +" hinzugefügt.\n";
                            }
                        }
                    }
                }

            }
            for(String delete_var:output_variable_list){
                if(output_rules_map.get(next_rule_variable).contains(delete_var)){
                    output_rules_map.get(next_rule_variable).remove(delete_var);
                }
            }
            String old_next_var = next_rule_variable;
            while(old_next_var ==next_rule_variable&&next_rule==1){
                setNextState();
            }

        }
        if(next_rule==2&&chainrule_state==0){
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
                log_output+="kettenregel aufgelöst, Chainrulestate=" +chainrule_state ;
            }
        }
        if(next_rule==2&&chainrule_state==1){chainrule_state=2;}


        returnList.add(log_output);
        return returnList;
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
    public List replaceTerminal(){
        //Define some inter variables
        List<String> returnList = new ArrayList<String>();
        String log_output = "";
        String word_to_check = next_rule_content;
        boolean add_new_variable = true;
        int count_char_replace = 0;
        for(int i=0; i<word_to_check.length();i++) {
            String char_to_check =String.valueOf(word_to_check.charAt(i));
            if (output_terminal_list.contains(char_to_check)&&word_to_check.length()>1) {

                //Check if already a replace variable exits
                Enumeration keys = terminal_translate.keys();
                while (keys.hasMoreElements()){
                    String t =(String) keys.nextElement();
                    if(t.equals(char_to_check)){
                        add_new_variable = false;
                    }
                }

                //Define new terminal and add it to the lists
                if(add_new_variable) {
                    List<String> rule_to_add = new ArrayList<>();
                    rule_to_add.add(char_to_check);
                    output_rules_map.put("T" + terminal_translate.size(), rule_to_add);
                    output_variable_list.add("T" + terminal_translate.size());
                    log_output += "Es wurde die Hiflsvariable T" + terminal_translate.size() + " erstellt und diese ersetzt " +char_to_check+".\n";
                    terminal_translate.put(char_to_check, "T" + terminal_translate.size());
                }

                String replace_Char = (String) terminal_translate.get(char_to_check);
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
        returnList.add(log_output);
        return returnList;
    }
}
