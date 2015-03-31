import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Frank on 10/28/14.
 */
public class BackwardChaining {
    private String andOperator = "&";
    private String implicationOperator = "=>";
    private String variable = "x";
    public static boolean isFact = false;

    public String query;
    public int numberOfClauses;
    public List<String> knowledgeBase = new ArrayList<String>();

    public static void main(String[] args) {
        BackwardChaining bc = new BackwardChaining();
        String x = "A(b,c)";
        String y = "A(x,c)";
        String theta = "";
        String conclusion = bc.unify(x, y, theta);
        System.out.println(conclusion);

    }

    public BackwardChaining() {

    }

    public BackwardChaining(String query, int numberOfClauses, List<String> knowledgeBase) {
        this.query = query;
        this.numberOfClauses = numberOfClauses;
        this.knowledgeBase = knowledgeBase;

    }

    public boolean backwardChaining(List<Predicate> queries, List<String> knowledgeBase) {
        Predicate conclusion = new Predicate();
        //    conclusion = queries.get(0);

        boolean[] truthOfQueries = new boolean[queries.size()];


        for (int i = 0; i < truthOfQueries.length; i++) {
            truthOfQueries[i] = false;
        }

        boolean[] truthOfQueries2 = new boolean[queries.size()];

        for (int i = 0; i < truthOfQueries2.length; i++) {
            truthOfQueries2[i] = false;
        }

        int index = 0;

        for (Predicate q : queries) {

            conclusion = q;
            for (String a : knowledgeBase) {
                if (isAtomic(a) == false) {
                    Predicate conclusion2;
                    conclusion2 = parseGetConclusion(a);
                    if (conclusion.getPredicate().equals(conclusion2.getPredicate())) {
                        if (conclusion.getArgument1().equals(conclusion2.getArgument1())
                                || conclusion.getArgument1().equals(this.variable)
                                || conclusion2.getArgument1().equals(this.variable)) {
                            if (conclusion.getArgument2() == null) {
                                continue;
                            }
                            if (conclusion2.getArgument2() == null) {
                                continue;
                            }
                            if (conclusion.getArgument2().equals(conclusion2.getArgument2())
                                    || conclusion.getArgument2().equals(this.variable)
                                    || conclusion2.getArgument2().equals(this.variable)) {
                                //return true;
                                List<Predicate> conditions = parseGetPredicates(a);

                                for (int i = 0; i < conditions.size(); i++) {
                                    conditions.get(i).println();
                                }

                                return backwardChaining(conditions, knowledgeBase);
                                //return parseGetPredicates(a)
                            }
                        }
                    }
                } else {
                    Predicate conclusion2;
                    conclusion2 = parsePredicate(a);
                    if (conclusion.getPredicate().equals(conclusion2.getPredicate())) {
                        if (conclusion.getArgument2() != null && conclusion2.getArgument2() != null) {
                            if (conclusion.getArgument1().equals(conclusion2.getArgument1())
                                    || conclusion.getArgument1().equals(this.variable)
                                    || conclusion2.getArgument1().equals(this.variable)) {
                                if (conclusion.getArgument2().equals(conclusion2.getArgument2())
                                        || conclusion.getArgument2().equals(this.variable)
                                        || conclusion2.getArgument2().equals(this.variable)) {
                                    //return true;

                                    conclusion2.println();

                                    truthOfQueries[index] = true;
                                    break;
                                }
                            }
                        } else {
                            if (conclusion.getArgument1().equals(conclusion2.getArgument1())
                                    || conclusion.getArgument1().equals(this.variable)
                                    || conclusion2.getArgument1().equals(this.variable)) {

                                //return true;

                                conclusion2.println();

                                truthOfQueries[index] = true;
                                break;
                            }
                        }
                    }
                }
            }
            index++;
        }
        for (int i = 0; i < truthOfQueries.length; i++) {
            if (truthOfQueries[i] == false) {
                return false;
            }
        }
        return true;
    }

/*    public boolean fol_bc_ask(List<String> knowledgeBase, String query) {
        return fol_bc_or(knowledgeBase, query, theta);
    }
    public boolean fol_bc_or(List<String> knowledgeBase, String goal, List<String> theta) {
        for (String a: knowledgeBase) {
            if (!isAtomic(a)) {

            }
        }
    }
    public boolean fol_bc_and(List<String> knowledgeBase, String goals, List<String> theta) {
        if (theta == null) {
            return false;
        } else if (goals.length() == 0) {
            return
        }
    }*/

    public boolean fol_bc_ask(List<String> knowledgeBase, String query) {
        List<String> result = fol_bc_or(knowledgeBase, query, "");

        System.out.println(result.size());
        for(String s: result)
        System.out.println(s);

        if (result.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public List<String> fol_bc_or(List<String> knowledgeBase, String goal, String theta) {
        List<String> matches = new ArrayList<String>();
        List<String> or_unifications = new ArrayList<String>();



        for (int i = 0; i < knowledgeBase.size(); i++) {
            if (matches(parseGetConclusionString(knowledgeBase.get(i)), goal)) {
                matches.add(knowledgeBase.get(i));
            }
        }
        for (int i = 0; i < matches.size(); ++i) {
            String line = matches.get(i);
            if (isCompoundStatement(line)) {
                isFact = false;
            } else {
                isFact = true;
            }
            String unifies = unify(parseGetConclusionString(line), goal,"");
            List<String> unifications = fol_bc_and(knowledgeBase, parseGetPremises(line), unifies);

            for (String s : unifications) {
                if(!or_unifications.contains(s))
                    or_unifications.add(s);
            }
        }

        return or_unifications;
    }

    public List<String> fol_bc_and(List<String> knowledgeBase, List<String> goals, String theta) {
        List<String> unifications = new ArrayList<String>();

        if (theta == null) {
            return unifications;
        } else if (goals == null || goals.size() == 0) {
            if(!unifications.contains(theta))
              unifications.add(theta);
            return unifications;
        } else {
            String first = goals.get(0);
            List<String> rest = new ArrayList<String>();
            for (String goal : goals)
                rest.add(goal);
            rest.remove(0);

            List<String> or_unifications = fol_bc_or(knowledgeBase, substitute(first, theta), theta);
            for (String or_unifier : or_unifications) {
                List<String> and_unifications = fol_bc_and(knowledgeBase, rest, or_unifier);
                for (String and_unifier : and_unifications)
                    if(!unifications.contains(and_unifier))
                        unifications.add(and_unifier);
            }

        }
        return unifications;
    }

    public String unify(String x, String y, String theta) {
        if (hasVariable(y) == 1 && !isFact) {
            return "";

        } else if (x.equals(y)) {
            return theta;
        } else if (x.equals(this.variable)) {
            return y;
        } else if (y.equals(this.variable)) {
            if(isFact)
                return x;
            else
                return "";

        } else if (isAtomic(x) && isAtomic(y)) {
            Predicate predicate1 = parseGetConclusion(x);
            Predicate predicate2 = parseGetConclusion(y);

            if (predicate1.getArgument2() == null) {
                predicate1.setArgument2("");
            }
            if (predicate2.getArgument2() == null) {
                predicate2.setArgument2("");
            }

            return unify(predicate1.getArgument2(), predicate2.getArgument2(), unify(predicate1.getArgument1(), predicate2.getArgument1(), theta));
        } else {
            return null;
        }
    }

/*    public boolean matches(String string1, String string2) {
        Predicate predicate1 = parsePredicate(string1);
        Predicate predicate2 = parsePredicate(string2);
        if (predicate1.getPredicate().equals(predicate2.getPredicate())) {
            return true;
        } else {
            return false;
        }
    }*/

    public boolean matches(String string1, String string2) {
        String stringA = string1.substring(0, string1.indexOf("("));
        String stringB = string2.substring(0, string2.indexOf("("));
        if (stringA.equals(stringB)) {
            return true;
        } else {
            return false;
        }
    }

/*
    public String unify_var(String var, String x, List<String> theta) {
        if (theta.contains(var)) {
            return unify(val, x, theta);
        } else if (theta.contains(x)) {
            return unify(var, val, theta);
        } else {
            theta.add(var);
            return var;
        }

    }

    public boolean backwardChaining(Predicate goal) {

    }*/

    public String substitute(String string, String theta) {
        if (theta.equals("")) {
            return string;
        }
        Predicate predicate = parsePredicate(string);
        if (predicate.getArgument1().equals(this.variable)) {
            predicate.setArgument1(theta);
        } else if (predicate.getArgument2() != null) {
            if (predicate.getArgument2().equals(this.variable)) {
                predicate.setArgument2(theta);
            }

        } else {
            return string;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(predicate.getPredicate());
        builder.append('(');
        builder.append(predicate.getArgument1());
        if (predicate.getArgument2() != null) {
            if (!predicate.getArgument2().equals("")) {
                builder.append(',');
                builder.append(predicate.getArgument2());
            }
        }
        builder.append(')');
        return builder.toString();
    }

    public int hasVariable(String input) {
        if (isAtomic(input)) {
            Predicate predicate = parsePredicate(input);
            if (predicate.getArgument1() == this.variable) {
                return 1;
            }
            if (predicate.getArgument2() == this.variable) {
                return 1;
            }
            return 0;
        }
        return -1;
    }

    public List<Predicate> parseGetPredicates(String line) {
        List<Predicate> predicates = new ArrayList<Predicate>();
        List<String> string1 = new ArrayList<String>();
        string1 = parseImplication(line);
        String premises = string1.get(0);
        //String conclusion = string1.get(1);

        List<String> string2 = parseAnd(premises);
        for (String s : string2) {
            predicates.add(parsePredicate(s));
        }
        return predicates;
    }

    public List<String> parseGetPremises(String line) {
        if (!line.contains(this.implicationOperator)) {
            return null;
        } else {
            List<String> strings = parseImplication(line);
            String leftHandSide = strings.get(0);
            List<String> premises = parseAnd(leftHandSide);
            return premises;
        }
    }

    public Predicate parseGetConclusion(String line) {
        List<String> string1 = parseImplication(line);
        String conclusion;
        if (!line.contains(this.implicationOperator)) {
            conclusion = string1.get(0);
        } else {
            conclusion = string1.get(1);
        }
        Predicate q = parsePredicate(conclusion);
        return q;
    }

    public String parseGetConclusionString(String line) {
        List<String> strings = parseImplication(line);
        if (line.contains(this.implicationOperator)) {
            return strings.get(1);
        } else {
            return strings.get(0);
        }

    }

    public boolean isAtomic(String line) {
        if (line.contains(this.andOperator)) {
            return false;
        } else if (line.contains(this.implicationOperator)) {
            return false;
        } else {

            if (line.contains("(")) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean isCompoundStatement(String input) {
        if (input.contains(this.implicationOperator)) {
            return true;
        } else {
            return false;
        }
    }

    public List<String> parseImplication(String string) {

        Scanner sc = new Scanner(string).useDelimiter(this.implicationOperator);
        List<String> parts = new ArrayList<String>();

        if (isAtomic(string)) {
            parts.add(string);
            return parts;
        }

        parts.add(sc.next());
        parts.add(sc.next());

        return parts;
    }

/*    public List<Predicate> parseAnd(String line) {
        Scanner sc = new Scanner(line).useDelimiter(this.andOperator);
        List<Predicate> predicates = new ArrayList<Predicate>();
        while (sc.hasNext()) {
            Predicate predicate = new Predicate();
            predicate.setPredicate(sc.next());
            predicates.add(predicate);
        }
        return predicates;
    }*/


    public List<String> parseAnd(String string) {
        Scanner sc = new Scanner(string).useDelimiter(this.andOperator);
        List<String> clauses = new ArrayList<String>();
        while (sc.hasNext()) {
            clauses.add(sc.next());
        }
        return clauses;
    }


    public Predicate parsePredicate(String string) {
        Predicate predicate = new Predicate();
        String argument1 = string.substring(string.indexOf("(") + 1, string.lastIndexOf(")"));
        //    System.out.println(argument1);
        Scanner sc = new Scanner(argument1).useDelimiter(",");
        List<String> arguments = new ArrayList<String>();
        while (sc.hasNext()) {
            arguments.add(sc.next().trim());
        }
        String predicateName = string.substring(0, string.indexOf("("));
        //    System.out.println(arguments);
        predicate.setPredicate(predicateName);
        predicate.setArgument1(arguments.get(0));

        if (arguments.size() == 2) {
            predicate.setArgument2(arguments.get(1));

        }

        return predicate;
    }


}

