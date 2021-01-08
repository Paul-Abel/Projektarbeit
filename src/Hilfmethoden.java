import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hilfmethoden {
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
}
