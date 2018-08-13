import java.util.ArrayList;
import java.util.List;

public class LuaParser {
    LuaScanner scanner;

    public LuaParser(LuaScanner scanner) {
        this.scanner = scanner;
    }

    public ParseTree parse() {
        ParseTree tree = match(GrammarRule.PROGRAM);
        if (tree == null) {
            System.err.println("No main program found!");
        }
        return tree;
    }

    private ParseTree match(GrammarRule rule) {
        List<ParseTree> result = matchGrammarRule(rule);
        return result == null ? null : new ParseTree(rule, result);
    }

    private List<ParseTree> matchGrammarRule(GrammarRule rule) {
        List<ParseTree> result = new ArrayList<>();
        if (rule.isSpecialRule()) {
            //If the rule is a BLOCK (Repeated Rule) or an OR (Multiple possible rules)
            List<ParseTree> trees = matchSpecialRule(rule);
            if (trees != null) {
                result.addAll(trees);
            }
        } else {
            //If the rule is a normal rule with only 1 variation
            for (int i = 0; i < rule.getRules()[0].length; i++) {
                ParseTree tree = matchRule(rule.getRules()[0][i]);
                if (tree == null) {
                    break;
                }
                result.add(tree);
            }
        }

        return result.isEmpty() ? null : result;
    }

    private List<ParseTree> matchGrammarRule(Object[] rule) {
        List<ParseTree> result = new ArrayList<>();
        for (int i = 0; i < rule.length; i++) {
            ParseTree tree = matchRule(rule[i]);
            if (tree == null) {
                break;
            }
            result.add(tree);
        }
        return result.isEmpty() ? null : result;
    }

    private ParseTree matchRule(Object rule) {
        if (rule instanceof GrammarRule) {
            return match((GrammarRule) rule);
        } else if (rule instanceof TokenType) {
            if (rule == scanner.getLookAhead().getToken()) {
                return new ParseTree(scanner.getNextToken());
            }
        }
        return null;
    }

    private List<ParseTree> matchSpecialRule(GrammarRule rule) {
        if (rule == GrammarRule.BLOCK) {
            //Repeated rule
            List<ParseTree> result = new ArrayList<>();

            while (true) {
                List<ParseTree> branches = matchGrammarRule((GrammarRule) rule.getRules()[0][0]);
                if (branches == null) {
                    break;
                } else {
                    result.add(new ParseTree(GrammarRule.STATEMENT, branches));
                }
            }

            return result.isEmpty() ? null : result;

        } else {
            //Or rule
            List<Object[]> rules = new ArrayList<>();
            List<ParseTree> result = new ArrayList<>();

            for (int i = 0; i < rule.getRules().length; i++) {
                rules.add(rule.getRules()[i]);
            }

            for (Object[] gRule : rules) {
                List<ParseTree> branches = matchGrammarRule(gRule);
                if (branches != null) {
                    result.addAll(branches);
                    break;
                }
            }

            return result.isEmpty() ? null : result;
        }
    }
}
