package cn.com.satum.server;

import java.util.concurrent.ExecutorService;  
import java.util.concurrent.Executors;  
import java.util.concurrent.ThreadFactory;  
import java.util.concurrent.TimeUnit;  
  
public class DaemonThreadFactory implements ThreadFactory{  
    @Override  
    public Thread newThread(Runnable runnable){  
        Thread t=new Thread(runnable);  
        t.setDaemon(true);  
        return t;  
    }  
}  
