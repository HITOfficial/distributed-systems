package sr.ice.server;

import com.zeroc.Ice.Current;

public class RUWSObjectI implements Demo.RUWS {
    private final int servantId;

    public RUWSObjectI(int id) {
        servantId = id;
        System.out.println("Created new RUWS servant with id " + id);
    }

    public void saveALotOfData(String data, Current current) {
        System.out.println(servantId + "saved a lot of data " + data );
    }
}