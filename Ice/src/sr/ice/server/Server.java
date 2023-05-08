package sr.ice.server;

import com.zeroc.Ice.Util;
import com.zeroc.Ice.Object;
import com.zeroc.Ice.Current;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.ServantLocator;

class RUWSServantLocator implements ServantLocator {
	private int assignedId = 1;

	public ServantLocator.LocateResult locate(Current current) {
		RUWSObjectI ruwsObject = new RUWSObjectI(assignedId++);
		return new ServantLocator.LocateResult(ruwsObject, null);
	}

	public void finished(Current current, Object servant, java.lang.Object cookie) {}
	public void deactivate(String category) {}
}

public class Server {
	private void serve(String[] args) {
		try(com.zeroc.Ice.Communicator communicator = Util.initialize(args)) {
			ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("DemoAdapter", "tcp -h 127.0.0.2 -p 10000 -z : udp -h 127.0.0.2 -p 10000 -z");

			Object fuwsObject = new FUWSObjectI(1);
			adapter.add(fuwsObject, Util.stringToIdentity("singleton"));

			RUWSServantLocator ruwsServantLocator = new RUWSServantLocator();
			adapter.addServantLocator(ruwsServantLocator, "ruws");

			FUWOSObjectI fuwosObject = new FUWOSObjectI(1);
			adapter.addDefaultServant(fuwosObject, "fuwos");

			adapter.activate();
			System.out.println("Processing loop...");
			communicator.waitForShutdown();
		}
	}

	public static void main(String[] args) {
		Server app = new Server();
		app.serve(args);
	}
}