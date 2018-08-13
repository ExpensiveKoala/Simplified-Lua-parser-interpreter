import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String fileName = "luaScript.lua";
        if (args.length > 0) {
            fileName = args[0];
        }
        File file = new File(fileName);
        ArrayList<String> program = new ArrayList<>();
        if (file.exists()) {
            try {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNext()) {
                    program.add(scanner.nextLine());
                }

                //Test Scanner
//                LuaScanner scannerTest = new LuaScanner(program);
//                while(scannerTest.hasNextToken()) {
//                    System.out.println(scannerTest.getNextToken());
//                }

                //Test Parser
                LuaScanner luaScanner = new LuaScanner(program);
                ParseTree tree = new LuaParser(luaScanner).parse();
                //System.out.println(tree);
                //Test Interpreter
                LuaInterpreter luaInterpreter = new LuaInterpreter(tree);
                luaInterpreter.run();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Could not find file: " + fileName);
        }
    }
}
