package com.yuu;

import java.util.*;

// 注意类名必须为 Main, 不要有任何 package xxx 信息
public class test {
    public static void main(String[] args) {
        List<Integer> i = new ArrayList<>();
        new Thread(() -> new LockTest(i).show()).start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        i.add(1);
        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            new LockTest(i).show();
        }).start();
        new Thread(() -> {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            new LockTestTest(i).help();
        }).start();
    }


    public static int check(int aim, int[] arr) {

        int[] tab = new int[aim + 1];
        for (int i = 0; i < tab.length; i++) {
            tab[i] = aim + 1;
        }

        tab[0] = 0;
        for (int i = 0; i < aim; i++) {
            for (int coin:
                    arr) {
                if (i - coin < 0)
                    continue;
                tab[i] = Math.min(tab[i], tab[i - coin] + 1);
            }
        }

        return (tab[aim] == aim + 1) ? -1 : tab[aim];


    }
}

class LockTest extends Thread {
    Object list;
    public LockTest(Object list) {
        this.list = list;
    }

    public void show() {
        synchronized (list) {
            List<Integer> list = (List<Integer>) this.list;
            if (list.size() == 0) {
                try {
                    System.out.println("我死了！");
                    this.list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("我活了！");
        }
    }
}

class LockTestTest extends Thread {
    Object list;
    public LockTestTest(Object list) {
        this.list = list;
    }

    public void help() {
        synchronized (list) {
            System.out.println("通知你了");
            list.notify();
        }
    }
}