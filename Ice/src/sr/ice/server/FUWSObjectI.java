package sr.ice.server;

import com.zeroc.Ice.Current;

public class FUWSObjectI implements Demo.FUWS {
    private final int servantId;
    private int counter = 0;

    public FUWSObjectI(int id) {
        servantId = id;
        System.out.println("Created new FUWOS servant with id " + id);
    }

    public int incrementAndGet( Current current) {
        System.out.println(servantId + "called incrementAndGet" + counter);
        return ++counter;
    }
}