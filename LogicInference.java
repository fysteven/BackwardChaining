import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Frank on 10/28/14.
 */
public class LogicInference {
    public String query;
    public int numberOfClauses;
    public List<String> knowledgeBase = new ArrayList<String>();


    public static void main(String[] args) throws IOException {
        LogicInference li = new LogicInference();
        li.read();
        System.out.println(li.query);
        System.out.println(li.numberOfClauses);
        for (int i = 0; i < li.numberOfClauses; i++) {
            System.out.println(li.knowledgeBase.get(i));
        }
        System.out.println();
        BackwardChaining bc = new BackwardChaining(li.query, li.numberOfClauses, li.knowledgeBase);

        //List<Predicate> queries = bc.parseGetPredicates(bc.query);
        boolean finalConclusion;
        //finalConclusion = bc.backwardChaining(queries, li.knowledgeBase);
        finalConclusion = bc.fol_bc_ask(li.knowledgeBase, li.query);
        String finalConclusionString;
        if (finalConclusion == false) {
            finalConclusionString = "FALSE";
        } else {
            finalConclusionString = "TRUE";
        }
        System.out.println();
        System.out.println(finalConclusionString);
        StringBuilder builder = new StringBuilder(finalConclusionString);
        li.writeOutputFile(builder);

    }

    public boolean writeOutputFile(StringBuilder builder) throws IOException {
        //System.out.print(builder.toString());
        String outputFile = "output.txt";
        FileWriter fileWriter = new FileWriter(outputFile);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(builder.toString());
        bufferedWriter.close();
        return true;
    }

    public boolean read(String input) throws IOException {
        if (input == null) {
            input = "input.txt";
        }
        FileReader fileReader = new FileReader(input);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        this.readInputFile(bufferedReader);
        return true;
    }

    public boolean readInputFile(BufferedReader reader) throws IOException {
        Scanner sc = new Scanner(reader).useDelimiter("\\s*\n\\s*");

        this.query = sc.next();

        this.numberOfClauses = sc.nextInt();


        for (int i = 0; i < this.numberOfClauses; i++) {
            this.knowledgeBase.add(sc.next());

        }

        return true;
    }

    public boolean write(String output) throws IOException {
        writeOutputFile(new StringBuilder(output));
        return true;
    }

    public boolean write(StringBuilder builder) throws IOException {
        writeOutputFile(builder);
        return true;
    }

    public boolean read() throws IOException {
        this.read("input.txt");
        return true;
    }
}
