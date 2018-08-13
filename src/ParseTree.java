import java.util.ArrayList;
import java.util.List;

public class ParseTree {
    public Object data;
    public List<ParseTree> children;

    public ParseTree(Object data, List<ParseTree> children) {
        this.children = children;
        this.data = data;
    }

    public ParseTree(Object data) {
        this(data, new ArrayList<>());
    }

    @Override
    public String toString() {
        return toString(0);
    }

    public String toString(int numTabs) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < numTabs; i++) {
            stringBuilder.append("\t");
        }
        stringBuilder.append(data);
        for (ParseTree tree : children) {
            stringBuilder.append("\n");
            stringBuilder.append(tree.toString(numTabs + 1));
        }
        return stringBuilder.toString();
    }
}
