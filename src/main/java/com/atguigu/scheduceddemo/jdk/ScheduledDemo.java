package com.atguigu.scheduceddemo.jdk;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

public class ScheduledDemo {

    public static void main(String[] args) {
//        System.out.println("初始时间："+System.currentTimeMillis());
//        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(3);
//        //scheduleAtFixedRate()：固定周期的执行定时任务；这种用的较多
//        //schedule():一次性的执行定时任务
//        executorService.scheduleAtFixedRate(()->{
//            System.out.println("excutors定时任务线程池执行定时任务"+System.currentTimeMillis());
//        }, 5,10, TimeUnit.SECONDS);

//        System.out.println("初始时间：" + System.currentTimeMillis());
//        new DelayTask().scheduleAtfixed(()->{
//            System.out.println("执行定时任务："+System.currentTimeMillis());
//        },5,5,TimeUnit.SECONDS);


        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("定时器执行定时任务:" + System.currentTimeMillis());
            }
        },5000,2000);
    }
}

//通过jdk提供的延时队列实现一个定时任务的演示
class DelayTask implements Delayed{

    private DelayQueue<DelayTask> delayQueue = new DelayQueue();

    private long time;

    //获取延时任务的剩余时间，如果返回值小于等于0，该元素将会出队。
    //延时队列会一直自动的调用该方法，直到出队
    @Override
    public long getDelay(TimeUnit unit) {
        return this.time - System.currentTimeMillis();
    }

    //任务排序
    @Override
    public int compareTo(Delayed o) {//和另一个延时队列对象进行比较
        return (int) (this.time - ((DelayTask)o).time);
    }

    //自定义一个一次性的定时任务
    public void schedule(Runnable runnable,int timeOut,TimeUnit unit){
        try {
            this.time = System.currentTimeMillis() + unit.toMillis(timeOut);
            delayQueue.put(this);//该方法将会发生阻塞，知道能够执行
            delayQueue.take();
            new Thread(runnable).start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    //自定义一个周期性的定时任务
    public void scheduleAtfixed(Runnable runnable,int timeOut,int period,TimeUnit unit){
        try {
            this.time = System.currentTimeMillis() + unit.toMillis(timeOut);
            int flag = 0;
            while (true){
                if (flag != 0 ){
                    this.time += unit.toMillis(period);
                }
                delayQueue.put(this);//该方法将会发生阻塞，知道能够执行
                delayQueue.take();
                new Thread(runnable).start();
                flag ++;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}