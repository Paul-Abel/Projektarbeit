import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hilfmethoden {
    public static String replaceChar(int index, String word, String replaceString){
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

    public void generatMapListDynamic(){
        List<String> values = new ArrayList<String>();
        values.add("Key 1");
        values.add("Key 2");
        values.add("Key 3");

        Map<String, List<String>> rules = new HashMap<String, List<String>>();
        values.forEach(terminal ->{
            rules.put(terminal, new ArrayList<String>());
        });
        rules.get("Key 1").add("1.1");
        rules.get("Key 1").add("1.2");
        rules.get("Key 1").add("1.3");
        rules.get("Key 2").add("2.1");
        rules.get("Key 3").add("3.1");
        rules.get("Key 3").add("3.2");

        System.out.println(rules.get("Key 1"));
        System.out.println(rules.get("Key 2"));
        System.out.println(rules.get("Key 3"));
    }
}
