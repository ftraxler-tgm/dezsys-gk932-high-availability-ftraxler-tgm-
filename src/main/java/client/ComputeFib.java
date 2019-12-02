package client;

import compute.Compute;

import java.math.BigInteger;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.lang.Long;


public class ComputeFib {



    public static void main(String args[]) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
        	// Hier ist was ganz wichtiges
            String name ="Loadbalancer";
            Registry registry = LocateRegistry.getRegistry(args[0]);
            Compute comp = (Compute) registry.lookup(name);
            Fibonacci task = new Fibonacci(Integer.parseInt(args[1]));
            BigInteger fibonacci = comp.executeTask(task);
            System.out.println(fibonacci);
        } catch (Exception e) {
            System.err.println("ComputeFibonacci exception:");
            e.printStackTrace();
        }
    }

}
