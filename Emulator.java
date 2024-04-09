import java.io.*;
import java.util.*;

public class Emulator {
    String[] memory = new String[256];
    int AC = 0;
    int PC = 0;
    int F = 0;

    void START() {
    }

    void LOAD(int x) {
        AC = x;
    }

    void LOADM(int x) {
        AC = Integer.parseInt(memory[x]);
    }

    void STORE(int x) {
        memory[x] = String.valueOf(AC);
    }

    void CMPM(int x) {
        int value = Integer.parseInt(memory[x]);
        if (AC > value)
            F = 1;
        else if (AC < value)
            F = -1;
        else
            F = 0;
    }

    void CJMP(int x) {
        if (F > 0)
            PC = x - 1;
    }

    void JMP(int x) {
        PC = x - 1;
    }

    void ADD(int x) {
        AC += x;
    }

    void ADDM(int x) {
        AC += Integer.parseInt(memory[x]);
    }

    void SUB(int x) {
        AC -= x;
    }

    void SUBM(int x) {
        AC -= Integer.parseInt(memory[x]);
    }

    void MUL(int x) {
        AC *= x;
    }

    void MULM(int x) {
        AC *= Integer.parseInt(memory[x]);
    }

    void DISP() {
        System.out.println(AC);
    }

    void HALT() {
    }

    void execute(String instruction) {
        String[] parts = instruction.split(" ");
        String instr = parts[0].toUpperCase();
        int arg = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;

        switch (instr) {
            case "START":
                START();
                break;
            case "LOAD":
                LOAD(arg);
                break;
            case "LOADM":
                LOADM(arg);
                break;
            case "STORE":
                STORE(arg);
                break;
            case "CMPM":
                CMPM(arg);
                break;
            case "CJMP":
                CJMP(arg);
                break;
            case "JMP":
                JMP(arg);
                break;
            case "ADD":
                ADD(arg);
                break;
            case "ADDM":
                ADDM(arg);
                break;
            case "SUB":
                SUB(arg);
                break;
            case "SUBM":
                SUBM(arg);
                break;
            case "MUL":
                MUL(arg);
                break;
            case "MULM":
                MULM(arg);
                break;
            case "DISP":
                DISP();
                break;
            case "HALT":
                HALT();
                break;
            default:
                System.err.println("Unknown instruction: " + instr);
                break;
        }
    }

    void loadProgram(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        int i = 0;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("%")) {
                line.substring(1).trim(); // print the content of the line
            } else {
                String[] parts = line.split(" ", 2); // split the line into two parts
                if (isNumeric(parts[0])) {
                    memory[i++] = parts[1]; // if the first part is a number, store the second part in memory
                } else {
                    memory[i++] = line; // if the first part is not a number, store the whole line in memory
                }
            }
        }
        reader.close();
    }

    boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void main(String[] args) throws IOException {

        Emulator emulator = new Emulator();
        if (args.length < 1) {
            System.err.println("Please provide a program file to run.");
            return;
        }
        emulator.loadProgram(args[0]);
        while (emulator.PC < emulator.memory.length && emulator.memory[emulator.PC] != null) {
            String instruction = emulator.memory[emulator.PC];
            if ("HALT".equals(instruction))
                break;
            emulator.execute(instruction);
            if (emulator.AC < 0 || emulator.AC >= 256) {
                throw new RuntimeException("Error: OUR CPU CANNOT HANDLE THAT MUCH!!!!");
            }
            emulator.PC++;
        }
    }
}
