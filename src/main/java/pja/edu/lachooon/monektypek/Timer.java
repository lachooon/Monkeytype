package pja.edu.lachooon.monektypek;

public class Timer extends Thread {
    private int time;
    private int currTime = 0;

    Timer(int time) {
        this.time = time;
    }

    public void run() {
        System.out.println("Odpalam dziwke");

        while (this.time >= 0) {
            try {
                if (currTime % 1000 == 0) {
                    System.out.println("Mam skurwysyna");
                    time--;
                }

                currTime += 10;
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int getCurrTime() {
        return currTime;
    }

    public int getTime() {
        return time;
    }
}
