/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.jbossas7.nodes;

import java.util.Collection;
import java.util.List;
import java.util.Vector;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.netbeans.modules.jbossas7.AS7Instance;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Mutex;
import org.openide.util.WeakListeners;

/**
 *
 * @author kulikov
 */
public class Hk2ExtensionChildren extends Children.Keys<Node> implements Refreshable, ChangeListener {

    private AS7Instance serverInstance;

    public Hk2ExtensionChildren(AS7Instance si) {
        this.serverInstance = si;
        serverInstance.addChangeListener(WeakListeners.change(this, serverInstance));
    }

    @Override
    protected Node[] createNodes(Node key) {
        return new Node[]{key};
    }

    @Override
    public void updateKeys() {
        Vector<Node> keys = new Vector<Node>();

        Collection<String> apps = serverInstance.getExtensions();

        if (apps != null) {
            for (String name : apps) {
                keys.add(new Hk2ItemNode(serverInstance, name));
            }
        }
        setKeys(keys);
    }

    @Override
    protected void addNotify() {
        updateKeys();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        Mutex.EVENT.readAccess(new Runnable() {
            @Override
            public void run() {
                updateKeys();
            }
        });

    }
}
