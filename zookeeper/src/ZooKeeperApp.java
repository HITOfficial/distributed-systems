import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ZooKeeperApp implements Watcher {

    private static final String ZNODE_PATH = "/z";

    private final ZooKeeper zooKeeper;
    private final String zNodeName;
    private final DataMonitor dataMonitor;

    private ZooKeeperApp(String hostName, String zNodeName) throws IOException {
        this.zNodeName = zNodeName;
        this.zooKeeper = new ZooKeeper(hostName, 3000, this);
        this.dataMonitor = new DataMonitor(zooKeeper, zNodeName, this);
    }

    @Override
    public void process(WatchedEvent event) {
        dataMonitor.process(event);
    }

    public void displayTreeStructure() throws KeeperException, InterruptedException {
        displayZNodeTree(ZNODE_PATH, "");
    }

    private void displayZNodeTree(String path, String indent) throws KeeperException, InterruptedException {
        System.out.println(indent + path);
        for (String child : zooKeeper.getChildren(path, false)) {
            String childPath = path.equals("/") ? path + child : path + "/" + child;
            displayZNodeTree(childPath, indent + "  ");
        }
    }

    public void runExternalApplication() {
        try {
            Runtime.getRuntime().exec(zNodeName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopExternalApplication() {
        try {
            Runtime.getRuntime().exec("pkill -f " + zNodeName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayNumberOfChildren(int numChildren) {
        System.out.println("Number of children: " + numChildren);
    }

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        final String hostAddress = "localhost:2181";
        final String zNodeName = "kcalc";


        ZooKeeperApp app = new ZooKeeperApp(hostAddress, zNodeName);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("EXECUTOR READY");
        System.out.println("Write 'tree' to display the tree structure.");
        while (true) {
            try {
                String line = br.readLine();
                if (line.equals("tree")) {
                    app.displayTreeStructure();
                } else {
                    System.out.println("Unrecognized command.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
