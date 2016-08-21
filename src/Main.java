import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Created by theo on 21/08/16.
 */
public class Main {
    private final String testProg = "-,+[                         Read first character and start outer character reading loop\n" +
            "    -[                       Skip forward if character is 0\n" +
            "        >>++++[>++++++++<-]  Set up divisor (32) for division loop\n" +
            "                               (MEMORY LAYOUT: dividend copy remainder divisor quotient zero zero)\n" +
            "        <+<-[                Set up dividend (x minus 1) and enter division loop\n" +
            "            >+>+>-[>>>]      Increase copy and remainder / reduce divisor / Normal case: skip forward\n" +
            "            <[[>+<-]>>+>]    Special case: move remainder back to divisor and increase quotient\n" +
            "            <<<<<-           Decrement dividend\n" +
            "        ]                    End division loop\n" +
            "    ]>>>[-]+                 End skip loop; zero former divisor and reuse space for a flag\n" +
            "    >--[-[<->+++[-]]]<[         Zero that flag unless quotient was 2 or 3; zero quotient; check flag\n" +
            "        ++++++++++++<[       If flag then set up divisor (13) for second division loop\n" +
            "                               (MEMORY LAYOUT: zero copy dividend divisor remainder quotient zero zero)\n" +
            "            >-[>+>>]         Reduce divisor; Normal case: increase remainder\n" +
            "            >[+[<+>-]>+>>]   Special case: increase remainder / move it back to divisor / increase quotient\n" +
            "            <<<<<-           Decrease dividend\n" +
            "        ]                    End division loop\n" +
            "        >>[<+>-]             Add remainder back to divisor to get a useful 13\n" +
            "        >[                   Skip forward if quotient was 0\n" +
            "            -[               Decrement quotient and skip forward if quotient was 1\n" +
            "                -<<[-]>>     Zero quotient and divisor if quotient was 2\n" +
            "            ]<<[<<->>-]>>    Zero divisor and subtract 13 from copy if quotient was 1\n" +
            "        ]<<[<<+>>-]          Zero divisor and add 13 to copy if quotient was 0\n" +
            "    ]                        End outer skip loop (jump to here if ((character minus 1)/32) was not 2 or 3)\n" +
            "    <[-]                     Clear remainder from first division if second division was skipped\n" +
            "    <.[-]                    Output ROT13ed character from copy and clear it\n" +
            "    <-,+                     Read next character\n" +
            "]                            End character readin";
    private final int[] mem;
    private int pos = 0;
    private int pointer = 0;
    private final ArrayList<Loop> loops = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);



    public Main(int memSize) {
        mem = new int[memSize];
        findLoopPositions();

        while(pos < testProg.length()) {
            switch(testProg.charAt(pos)) {
                case '>':
                    pointer++;
                    break;
                case '<':
                    pointer--;
                    break;
                case '+':
                    mem[pointer]++;
                    break;
                case '-':
                    mem[pointer]--;
                    break;
                case '.':
                    System.out.println((char) mem[pointer]);
                    break;
                case ',':
                    System.out.print("Input: ");
                    final String in = scanner.next();
                    if(in.length() > 0) System.out.println("Only the first character will be used");
                    mem[pointer] = in.charAt(0);
                    break;
                case '[':
                    if(mem[pointer] == 0) {
                        //Jump to matching ]
                        pos = closingPos(pos);
                    }
                    break;
                case ']':
                    if(mem[pointer] != 0) {
                        pos = openingPos(pos);
                    }
                    break;
            }
            pos++;

        }

    }

    public int closingPos(int left) {
        for(Loop l : loops) {
            if(l.left == left) return l.right;
        }
        return -1;
    }

    public int openingPos(int right) {
        for(Loop l : loops) {
            if(l.right == right) return l.left;
        }
        return -1;
    }

    public void findLoopPositions() {
        final Stack<Integer> openings = new Stack<>();
        for(int i = 0; i < testProg.length(); i++) {
            if(testProg.charAt(i) == '[') {
                openings.push(i);
            } else if(testProg.charAt(i) == ']') {
                loops.add(new Loop(openings.pop(), i));
            }
        }
        System.out.println(loops.toString());
    }


    private static class Loop {
        int left;
        int right;

        Loop(int l, int r) {
            left = l;
            right = r;
        }

        @Override
        public String toString() {
            return left + " " + right;
        }
    }



    public static void main(String[] args) {
        new Main(2000);
    }




}
