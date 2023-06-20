import org.apache.zookeeper.*;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class ZooKeeperApp implements Watcher {

    private static final String ZNODE_PATH = "/z";

    private final ZooKeeper zooKeeper;
    private final String externalApplicationName;

    private Process externalApplicationProcess = null;

    private ZooKeeperApp(String hostName, String externalApplicationName) throws IOException {
        this.externalApplicationName = externalApplicationName;
        this.zooKeeper = new ZooKeeper(hostName, 3000, this);
    }


    public void displayTreeStructure() throws KeeperException, InterruptedException {
        displayZNodeTree(ZNODE_PATH, "");
    }

    private void displayZNodeTree(String path, String indent) throws KeeperException, InterruptedException {
        System.out.println(indent + path);
        for (String child : zooKeeper.getChildren(path, false)) {
            String childPath = path.equals("/") ? path + child : path + "/" + child;
            displayZNodeTree(childPath, indent);
        }
    }

    public void runExternalApplication() {
        if (externalApplicationProcess == null) {
        try {
            ProcessBuilder pb = new ProcessBuilder();
            pb.command(externalApplicationName);
            externalApplicationProcess = pb.start();
            System.out.println("External application started.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error while starting external application: " + externalApplicationName);
        }
        }
    }

    public void stopExternalApplication() {
        if (externalApplicationProcess != null) {
            externalApplicationProcess.destroy();
            externalApplicationProcess = null;
            System.out.println("External application stopped.");
        }
        else {
            System.out.println("External application not running.");
        }
    }

    public void displayNumberOfChildren(int numChildren) {
        System.out.println("Number of children: " + numChildren);
    }

    @Override
    public void process(WatchedEvent event) {
        Event.EventType eventType = event.getType();
        String path = event.getPath();

        if (eventType == Event.EventType.NodeCreated && path.equals(ZNODE_PATH)) {
            handleNodeCreated();
        } else if (eventType == Event.EventType.NodeDeleted && path.equals(ZNODE_PATH)) {
            handleNodeDeleted();
        } else if (eventType == Event.EventType.NodeCreated && path.startsWith(ZNODE_PATH)) {
            try {
                handleChildNodeCreated(event);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (KeeperException e) {
                throw new RuntimeException(e);
            }
        } else if (eventType == Event.EventType.NodeChildrenChanged && path.equals(ZNODE_PATH)) {
            try {
                handleChildrenChanged(event);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (KeeperException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void handleNodeCreated() {
        System.out.println("Node created: " + ZNODE_PATH);
        this.runExternalApplication();
    }

    private void handleNodeDeleted() {
        System.out.println("Node deleted: " + ZNODE_PATH);
        this.stopExternalApplication();
    }

    private void handleChildNodeCreated(WatchedEvent event) throws InterruptedException, KeeperException {
        System.out.println("Node child created: " + event.getPath());
        int numChildren = this.countChildren(ZNODE_PATH);
        this.displayNumberOfChildren(numChildren);
    }

    private void handleChildrenChanged(WatchedEvent event) throws InterruptedException, KeeperException {
        System.out.println("Node child changed: " + event.getPath());
        int numChildren = this.countChildren(ZNODE_PATH);
        this.displayNumberOfChildren(numChildren);
    }

    private int countChildren(String path) throws KeeperException, InterruptedException {
        List<String> children = zooKeeper.getChildren(path, false);
        int count = children.size();

        for (String child : children) {
            String childPath = path + "/" + child;
            count += countChildren( childPath);
        }

        return count;
    }

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        if (args.length < 2) {
            System.out.println(" Usage: ZooKeeperApp <hostAddress> <zNodeName>");
            System.exit(1);
        }
        final String hostAddress = args[0];
        final String zNodeName = args[1];


        ZooKeeperApp app = new ZooKeeperApp(hostAddress, zNodeName);

        app.zooKeeper.addWatch(ZNODE_PATH, AddWatchMode.PERSISTENT_RECURSIVE);


        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("EXECUTOR READY");
        System.out.println("Write 'tree' to display the tree structure, 'size' to count the number of children");
        while (true) {
            try {
                String line = br.readLine();
                if (line.equals("tree")) {
                    app.displayTreeStructure();
                } else if (line.equals("size")) {
                    int numChildren = app.countChildren(ZNODE_PATH);
                    app.displayNumberOfChildren(numChildren);
                }
                else {
                    System.out.println("Unrecognized command.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
