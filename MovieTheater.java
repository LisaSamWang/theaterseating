package theaterseating;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.*;
import java.lang.Math.*;

public class MovieTheater {
    int rows = 10;
    int columns = 20;
    int numberOfSeats = rows * columns;
    LinkedHashMap<String, ArrayList<String>> allocations = new LinkedHashMap<>();
    int[][] seats = new int[10][20];
    int[] remainingSeats = new int[] { 20, 20, 20, 20, 20, 20, 20, 20, 20, 20 };
    int[] alternaterows = {4, 5}; //book in alternate rows to maximize customer satisfaction. This is expressed in indices
    boolean frontallocated = false;

    public MovieTheater() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                seats[i][j] = 0; //0 means unoccupied. 1 means skipped, 2 means occupied. for future optimizations.
            }
        }

    }

    public void bookSeat(String res) {
        String[] input = res.split(" ");
        String reservation = input[0];
        int count = Integer.parseInt(input[1]);
        int group = count;
        if(count > 0){
            if (numberOfSeats >= count) {
                allocations.put(reservation, new ArrayList<String>());
                if (group > 20) {
                    while (group > 20) {
                        allocate(reservation, 20);
                        group -= 20;
                    }
                }
                allocate(reservation, group);
            } else {
                throw new SeatingException("Insufficient seats in this theater!");
            }
        } else {
            throw new SeatingException("Invalid number of seats");
        }
    }

    private void allocate(String res, int seatsToBook) {
        while (seatsToBook > 20) {
            if (alternaterows[0] > 0 || alternaterows[1] > 0) {
                allocaterow(res); //allocates full row
            } else {
                allocateempty(res, 20); //allocates from any existent empty seat
            }
            seatsToBook -= 20;
        }
        if (alternaterows[0] > 0 || alternaterows[1] > 0) {
            allocateseats(res, seatsToBook); //allocate the seats normally, one row per group
        } else {
            allocateempty(res, seatsToBook);
        }
    }

    private void allocateseats(String res, int need) {
        int rowallocated = alternaterows[0];
        if (frontallocated) {
            rowallocated = alternaterows[1];
            frontallocated = false;
            if (rowallocated + 1 >= rows - 1) { //if already on last row
                alternaterows[1] = -1;
            } else {
                alternaterows[1] = rowallocated + 1;
            }
        } else {
            frontallocated = true;
            if (rowallocated - 1 < 0) {
                alternaterows[0] = -1;
            } else {
                alternaterows[0] = rowallocated - 1;
            }
        } //if row in front was allocated before, allocate the one after.
        remainingSeats[rowallocated] = remainingSeats[rowallocated] - need;
        ArrayList<String> curres = allocations.get(resnum);
        int i = columns/2 - 1; //as index
        int j = columns/2;
        boolean allocatedi = false;
        int lastallfront = i;
        int lastallback = j;
        while (need > 0) {
            if (allocatedi) {
                seats[rowallocated][j] = 1;
                curres.add(theaternotation(rowallocated, j));
                allocatedi = false;
                //lastallback = j;
                i--;
                need--;
            } else {
                seats[rowallocated][i] = 1;
                curres.add(theaternotation(rowallocated, i));
                allocatedi = true;
                //lastallfront = i;
                j++;
                need--;
            }
        }
        //for future optimizations
//        for (int b = lastallback; b <= min(columns - 1, lastallback + 3); b++) {
//            seats[rowallocated][b] = 0; //buffer the seat
//        }
//        for (int f = lastallfront - 1; f >= max(0, lastallfront - 3); f--) {
//            seats[rowallocated][f] = 0;
//        }

    }

    private void allocateempty(String resnum, int need) {
        ArrayList<String> curres = allocations.get(resnum);
        for (int i = 0; i < rows; i ++) {
            if (need <= 0) {
                break;
            }
            if (remainingSeats[i] > 0) {
                for (int j = 0; j < columns; j++) {
                    if (need <= 0) {
                        break;
                    }
                    if (seats[i][j] < 1) { //if not occupied
                        seats[i][j] = 1;
                        need--;
                        remainingSeats[i] = remainingSeats[i] - 1;
                        curres.add(theaternotation(i, j));
                    }
                }
            }
        }
    }

    private void allocaterow(String resnum) {
        int rowallocated = alternaterows[0];
        if (frontallocated) {
            rowallocated = alternaterows[1];
            frontallocated = false;
            if (rowallocated + 1 >= rows - 1) { //if already on last row
                alternaterows[1] = -1;
            } else {
                alternaterows[1] = rowallocated + 1;
            }
        } else {
            frontallocated = true;
            if (rowallocated - 1 < 0) {
                alternaterows[0] = -1;
            } else {
                alternaterows[0] = rowallocated - 1;
            }
        } //if row in front was allocated before, allocate the one after.
        remainingSeats[rowallocated] = 0;
        ArrayList<String> curres = allocations.get(resnum);
        for (int s = 0; s < columns; s++) {
            seats[rowallocated][s] = 1;
            curres.add(theaternotation(rowallocated, s));
        }
    }

    private String theaternotation(int i, int j) {
        return (char) (i + 65) + Integer.toString(j + 1);
    }
    public LinkedHashMap<String, ArrayList<String>> getResults() {
        return allocations;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }
}