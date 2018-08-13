import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class LuaInterpreter {
    private List<ParseTree> parseTrees;

    Object[] ids = new Object[26];

    public LuaInterpreter(ParseTree parseTree) {
        this.parseTrees = new ArrayList<>();
        parseTrees.add(parseTree);
    }

    public LuaInterpreter(List<ParseTree> parseTrees) {
        this.parseTrees = parseTrees;
    }

    public void run() {
        for (ParseTree tree : parseTrees) {
            execute(tree);
        }
    }

    private void execute(ParseTree tree) {
        if (tree.data == GrammarRule.PROGRAM) {
            executeProgram(tree);
        } else {
            System.err.println("Found: " + tree.data + " expected: " + GrammarRule.PROGRAM);
        }
    }

    private void executeProgram(ParseTree tree) {
        for (ParseTree t : tree.children) {
            if (t.data instanceof Token) {
                if (((Token) t.data).getToken() == TokenType.ID) {
                    assignId(t.data, ((Token) t.data).getText().charAt(0));
                }
            } else if (t.data == GrammarRule.BLOCK) {
                executeBlock(t);
            }
        }
    }

    private void executeBlock(ParseTree tree) {
        for (ParseTree t : tree.children) {
            if (t.data == GrammarRule.STATEMENT) {
                executeStatement(t);
            } else {
                System.err.println("Found: " + t.data + " expected: " + GrammarRule.STATEMENT);
            }
        }
    }

    private void executeStatement(ParseTree tree) {
        ParseTree t = tree.children.get(0);
        switch ((GrammarRule) t.data) {
            case IF_STATEMENT:
                executeIfStatement(t);
                break;
            case WHILE_STATEMENT:
                executeWhile(t);
                break;
            case REPEAT_STATEMENT:
                executeRepeat(t);
                break;
            case ASSIGNMENT_STATEMENT:
                executeAssignment(t);
                break;
            case PRINT_STATEMENT:
                executePrint(t);
                break;
        }
    }

    private void executeIfStatement(ParseTree tree) {
        if(executeBoolean(tree.children.get(1))) {
            executeBlock(tree.children.get(3));
        } else {
            executeBlock(tree.children.get(5));
        }
    }

    private void executeWhile(ParseTree tree) {
        while(executeBoolean(tree.children.get(1))) {
            executeBlock(tree.children.get(3));
        }
    }

    private void executeRepeat(ParseTree tree) {
        do {
            executeBlock(tree.children.get(1));
        } while(executeBoolean(tree.children.get(3)));
    }

    private void executeAssignment(ParseTree tree) {
        char id = ((Token)tree.children.get(0).data).getText().charAt(0);
        Object var = getId(id);
        if(var == null || var instanceof Integer) {
            assignId(executeArithmetic(tree.children.get(2)), id);
        } else {
            System.err.println("Variable " + id + " is already used as a function ID!");
        }
    }

    private void executePrint(ParseTree tree) {
        if (tree.children.get(2).data == GrammarRule.ARITHMETIC_EXPRESSION) {
            System.out.println(executeArithmetic(tree.children.get(2)));
        } else {
            System.err.println("Found: " + tree.children.get(2).data + " expected: " + GrammarRule.ARITHMETIC_EXPRESSION);
        }
    }

    private int executeArithmetic(ParseTree tree) {
        List<Token> prefix = flattenArithmetic(tree.children);
        Stack<Integer> stack = new Stack<>();
        for (int i = prefix.size() - 1; i >= 0; i--) {
            if (prefix.get(i).getToken() == TokenType.ID) {
                Object idValue = getId(prefix.get(i).getText().charAt(0));
                if(idValue instanceof Integer) {
                    stack.push((Integer) idValue);
                } else {
                    System.err.println("INVALID ID type");
                    System.exit(1);
                }
            } else if(prefix.get(i).getToken() == TokenType.LITERAL_INTEGER) {
                stack.push(Integer.parseInt(prefix.get(i).getText()));
            } else {
                int o1 = stack.pop();
                int o2 = stack.pop();
                switch (prefix.get(i).getToken()) {
                    case ADD_OPERATOR:
                        stack.push(o1 + o2);
                        break;
                    case SUB_OPERATOR:
                        stack.push(o1 - o2);
                        break;
                    case MUL_OPERATOR:
                        stack.push(o1 * o2);
                        break;
                    case DIV_OPERATOR:
                        stack.push(o1 / o2);
                        break;
                }
            }
        }

        return stack.peek();
    }

    private List<Token> flattenArithmetic(List<ParseTree> trees) {
        List<Token> value = new ArrayList<>();
        if (trees.get(0).data instanceof Token) {
            value.add(((Token) trees.get(0).data));
        } else if(trees.get(0).data == GrammarRule.ARITHMETIC_OP) {
            value.addAll(flattenArithmetic(trees.get(0).children));
        } else {
            System.err.println("Found: " + trees.get(0).data + " expected: Token");
        }
        for (int i = 1; i < trees.size(); i++) {
            if (trees.get(i).data instanceof Token) {
                value.add(((Token) trees.get(0).data));
            } else if (trees.get(i).data == GrammarRule.ARITHMETIC_EXPRESSION) {
                value.addAll(flattenArithmetic(trees.get(i).children));
            } else if(trees.get(i).data == GrammarRule.ARITHMETIC_OP) {
                value.addAll(flattenArithmetic(trees.get(i).children));
            }
        }
        return value;
    }

    private boolean executeBoolean(ParseTree tree) {
        List<Object> statement = flattenBoolean(tree.children);
        int v1 = (Integer)statement.get(1);
        int v2 = (Integer)statement.get(2);
        switch (((Token)((ParseTree)statement.get(0)).data).getToken()) {
            case LE_OPERATOR:
                return v1 <= v2;
            case LT_OPERATOR:
                return v1 < v2;
            case GE_OPERATOR:
                return v1 >= v2;
            case GT_OPERATOR:
                return v1 > v2;
            case EQ_OPERATOR:
                return v1 == v2;
            case NE_OPERATOR:
                return v1 != v2;
        }

        return false;
    }

    private List<Object> flattenBoolean(List<ParseTree> trees) {
        List<Object> value = new ArrayList<>();
        value.add(trees.get(0).children.get(0));
        value.add(executeArithmetic(trees.get(1)));
        value.add(executeArithmetic(trees.get(2)));
        return value;
    }

    private void assignId(Object value, char c) {
        c = Character.toLowerCase(c);
        ids[((int) c) - 97] = value;
    }

    private Object getId(char c) {
        c = Character.toLowerCase(c);
        return ids[((int) c) - 97];
    }
}
