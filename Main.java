package theaterseating;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Collections;

import static theaterseating.SeatingException.*;

/** Movie Theater Seating.
 *  @author Lisa Sam Wang
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 2.
     *  ARGS[0] is the name of the input file.
     *  Otherwise, input comes from the standard
     *  input.  ARGS[1] is optional; when present, it names an output
     *  file for the assignments.  Otherwise, output goes to a newly formed file called output.txt.
     *  Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (SeatingException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length == 1) {
            _input = getInput(args[0]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 1) {
            _output = getOutput(args[1]);
        } else {
            File outputfile = new File("output.txt");
            outputfile.createNewFile();
            _output = getOutput("output.txt");
        }
    }

    /** Return a Scanner reading from the file named INNAME. */
    private Scanner getInput(String inname) {
        try {
            return new Scanner(new File(inname));
        } catch (IOException excp) {
            System.out.println("Could not open file");
            excp.printStackTrace();
            System.exit(1);
        }
    }

    /** Return a PrintStream writing to the file named OUTNAME. */
    private PrintStream getOutput(String outname) {
        try {
            return new PrintStream(new File(outname));
        } catch (IOException excp) {
            System.out.println("Could not open file");
            excp.printStackTrace();
            System.exit(1);
        }
    }

    /** Set up the seating assignment based on _input, sending the
     *  results to _output. */
    private void process() {
        String nextl = _input.nextLine();
        while (_input.hasNext()) {
            String request = nextl;
            if (request.contains("*") || request.contains("!") || request.contains("@") || request.contains("%")) {
                throw new SeatingException("Not a valid request");
            }
            MovieTheater mytheater = new MovieTheater();
            while (!(nextl.contains("*"))) { //while valid input
                if (nextl.isEmpty()) { //skip empty lines
                    nextl = _input.nextLine();
                }
                mytheater.bookSeat(nextl);
                nextl = _input.nextLine();
            }
        }

    }

    private void printalloc(LinkedHashMap<String, ArrayList<String>> res) {
        Iterator<Entry<String, ArrayList<String>>> itr = res.entrySet()
                .iterator();
        while (itr.hasNext()) {
            Entry<String, ArrayList<String>> pairs = itr.next();
            Collections.sort(pairs.getValue(), comparator);
            String str = pairs.getKey() + " " + pairs.getValue();
            _output.println(str);
        }

    }

    public String printpath(String outputpath) {
        if (outputpath.isEmpty()) {
            return System.getProperty("user.dir") + File.separator + "output.txt";
        }
        return outputpath;
    }

    private int comparator(String a, String b) {
        if (a.charAt(0) == b.charAt(0)) {
            if (Integer.parseInt(a.charAt(1)) < Integer.parseInt(b.charAt(1))) {
                return -1;
            }
            return 1;
        }
    }

    /** Source of input messages. */
    private Scanner _input;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

}