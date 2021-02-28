import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hilfmethoden {
    //Replace a char at a index with a string
    public String replaceChar(int index, String word, String replaceString){
        String newWord = "";
        String before = "";
        String after= "";
        before = word.substring(0, index);
        if(index>=word.length()){
            System.out.println("index is to big");
            return null;
        }
        if(index!=word.length()){
            after = word.substring(index+1, word.length());
        }
        newWord =before.concat(replaceString).concat(after);
        return newWord;
    }

    //Get the first variable (does not work for variables the type of S(A), A(A)... (not needed in program yet)
    public String getVariable(int index, String word){
        String variable = String.valueOf(word.charAt(index));
        if(index+1>= word.length()){
            return variable;
        }

        while (true){
            index++;
            if(index == word.length()){
                return variable;
            }
            try {
                Integer.parseInt(String.valueOf(word.charAt(index)));
                variable= variable +(word.charAt(index));
            } catch (NumberFormatException nfe) {
                return variable;
            }
        }
    }

    //Convert Output to get rid of artifacts from Lists and Maps
    public String convertOutput(String input){
        input = input.replace("[","");
        input = input.replace("]","");
        input = input.replace("{","");
        input = input.replace("}","");
        return input;

    }
}
