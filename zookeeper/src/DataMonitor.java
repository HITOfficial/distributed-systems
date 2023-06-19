import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

public class DataMonitor implements Watcher, AsyncCallback.StatCallback {

    private ZooKeeper zooKeeper;
    private String znodePath;
    private ZooKeeperApp zooKeeperApp;

    public DataMonitor(ZooKeeper zooKeeper, String znodePath, ZooKeeperApp zooKeeperApp) {
        this.zooKeeper = zooKeeper;
        this.znodePath = znodePath;
        this.zooKeeperApp = zooKeeperApp;
        zooKeeper.exists(znodePath, this, this, null);
    }

    public void process(WatchedEvent event) {
        if (event.getType() == Event.EventType.NodeCreated && event.getPath().equals(znodePath)) {
            // Launch the external graphical application
            zooKeeperApp.runExternalApplication();
        } else if (event.getType() == Event.EventType.NodeDeleted && event.getPath().equals(znodePath)) {
            // Stop the external graphical application
            zooKeeperApp.stopExternalApplication();
        } else if (event.getType() == Event.EventType.NodeChildrenChanged && event.getPath().equals(znodePath)) {
            zooKeeper.exists(znodePath, this, this, null); // Reset the watcher on the znode
            try {
                // Display the current number of children
                int numChildren = zooKeeper.getChildren(znodePath, false).size();
                zooKeeperApp.displayNumberOfChildren(numChildren);
            } catch (KeeperException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void processResult(int rc, String path, Object ctx, Stat stat) {
        if (stat != null) {
            zooKeeper.exists(znodePath, this, this, null); // Reset the watcher on the znode
        }
    }
}
