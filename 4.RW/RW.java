
import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RW {
    private final Lock readLock = new ReentrantLock();
    private final Semaphore writeLock = new Semaphore(1);
    private static int rc = 0;

    private void Reader(int id) {
        try {
            readLock.lock();
            if (++rc == 1) {
                writeLock.acquire();
            }
            readLock.unlock();

            System.out.println("Reader " + id + " is reading.");
            Thread.sleep(2500);
            System.out.println("Reader " + id + " finished reading.");

            readLock.lock();
            if (--rc == 0) {
                writeLock.release();
            }
            readLock.unlock();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    private void Writer(int id) {
        try {
            writeLock.acquire();

            System.out.println("Writer " + id + " is writing.");
            Thread.sleep(1000);
            System.out.println("Writer " + id + " finished writing.");

            writeLock.release();

        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        RW rw = new RW();
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the number of processes: ");
        int n = sc.nextInt();

        int[] seq = new int[n];
        System.out.println("Enter the sequence of processes (1 for Reader, 2 for Writer):");
        for (int i = 0; i < n; i++) {
            seq[i] = sc.nextInt();
        }

        for (int i = 0; i < n; i++) {
            int id = i + 1;
            if (seq[i] == 1) {
                new Thread(() -> rw.Reader(id)).start();
            } else {
                new Thread(() -> rw.Writer(id)).start();
            }
        }

        sc.close();
    }
}
