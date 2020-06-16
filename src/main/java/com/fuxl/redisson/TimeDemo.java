package com.fuxl.redisson;

import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

public class TimeDemo {
    /**
     * **time Start**2020-06-15 11:40:06
     * **time End**2020-06-15 11:40:06
     * **timeTask**2020-06-15 11:40:09
     * @param args
     */
    public static void main(String[] args) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timer timer = new Timer();

        System.out.println("**time Start**"+ sf.format(System.currentTimeMillis()));
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("**timeTask**"+ sf.format(System.currentTimeMillis()));
                timer.cancel();
            }
        },3*1000);
        System.out.println("**time End**"+ sf.format(System.currentTimeMillis()));

    }
}
