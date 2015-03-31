/**
 * Created by Frank on 11/11/14.
 */
public class Predicate {
    public String predicate;
    public String argument1;
    public String argument2;

    public boolean print() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.predicate);
        builder.append('(');
        builder.append(this.argument1);
        if (this.argument1 != null) {
            builder.append(',');
            builder.append(this.argument2);
        }
        builder.append(')');
        System.out.print(builder);
        return true;
    }

    public boolean println() {
        print();
        System.out.println();
        return true;
    }

    public String getPredicate() {
        return predicate;
    }

    public void setPredicate(String predicate) {
        this.predicate = predicate;
    }

    public String getArgument1() {
        return argument1;
    }

    public void setArgument1(String argument1) {
        this.argument1 = argument1;
    }

    public String getArgument2() {
        return argument2;
    }

    public void setArgument2(String argument2) {
        this.argument2 = argument2;
    }
}
