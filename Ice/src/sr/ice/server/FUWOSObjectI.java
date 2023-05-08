package sr.ice.server;

import com.zeroc.Ice.Current;

public class FUWOSObjectI implements Demo.FUWOS {
    private final int servantId;

    public FUWOSObjectI(int id) {
        servantId = id;
        System.out.println("Created new FUWOS servant with id " + id);
    }

    public String concatWords(String first, String second, Current current) {
        System.out.println(servantId + "called concatWords " + first + ", " + second);
        return first + second;
    }
}