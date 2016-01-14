import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * Runs a number of algorithms that try to fit files onto disks.
 */
public class Bins {
    public static final String DATA_FILE = "example.txt";

    /**
     * Reads list of integer data from the given input.
     *
     * @param input tied to an input source that contains space separated numbers
     * @return list of the numbers in the order they were read
     */
    public List<Integer> readData(Scanner input) {
        List<Integer> results = new ArrayList<Integer>();
        while (input.hasNext()) {
            results.add(input.nextInt());
        }
        return results;
    }

    public List<Integer> getData() {
        InputStream bStream = Bins.class.getClassLoader().getResourceAsStream(DATA_FILE);
        Scanner input = new Scanner(bStream);
        List<Integer> data = readData(input);
        return data;
    }

    /**
     * Get the total size of all the files.
     *
     * @param data a list containing the sizes of each file
     * @return the total size of all the files.
     */
    public int getTotal(List<Integer> data) {
        int total = 0;
        for (Integer size : data) {
            total += size;
        }
        return total;
    }

    public void worstFit(PriorityQueue<Disk> pq, List<Integer> data) {
        int diskId = 1;
        for (Integer size : data) {
            Disk d = pq.peek();
            if (d.freeSpace() >= size) {
                pq.poll();
                d.add(size);
                pq.add(d);
            } else {
                Disk d2 = new Disk(diskId);
                diskId++;
                d2.add(size);
                pq.add(d2);
            }
        }
    }

    /**
     * Do the prints.
     *
     * @param type is the type of bin packing to use (worst-fit or worst-fit decreasing)
     * @param pq   is the Priority Queue containing the Disks.
     */
    public void prints(String type, PriorityQueue<Disk> pq) {
        System.out.println(type);
        System.out.println("number of pq used: " + pq.size());
        while (!pq.isEmpty()) {
            System.out.println(pq.poll());
        }
        System.out.println();
    }

    /**
     * The main program.
     */
    public static void main(String args[]) {
        Bins b = new Bins();
        List<Integer> data = b.getData();
        int total = b.getTotal(data);

        PriorityQueue<Disk> pq = new PriorityQueue<Disk>();
        pq.add(new Disk(0));

        b.worstFit(pq, data);
        System.out.println("total size = " + total / 1000000.0 + "GB");
        b.prints("worst-fit method", pq);

        Collections.sort(data, Collections.reverseOrder());
        pq.add(new Disk(0));
        b.worstFit(pq, data);
        b.prints("worst-fit decreasing method", pq);
    }
}
