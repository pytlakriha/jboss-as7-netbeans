/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.jbossas7.nodes.domain;

import org.netbeans.modules.jbossas7.nodes.*;
import java.util.Collection;
import java.util.Vector;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.netbeans.modules.jbossas7.AS7Domain;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Mutex;
import org.openide.util.WeakListeners;

/**
 *
 * @author kulikov
 */
public class Hk2ServerChildren extends Children.Keys<Node> implements Refreshable, ChangeListener {

    private AS7Domain domain;
    private String host;

    public Hk2ServerChildren(AS7Domain si, String hostName) {
        this.host = hostName;
        this.domain = si;
        domain.addChangeListener(WeakListeners.change(this, domain));
    }

    @Override
    protected Node[] createNodes(Node key) {
        return new Node[]{key};
    }

    @Override
    public void updateKeys() {
        Vector<Node> keys = new Vector<Node>();

        Collection<String> servers = domain.getServer(host);

        if (servers != null) {
            for (String server : servers) {
                keys.add(new Hk2ItemNode(domain, new Hk2InstanceChildren(domain, host, server), server));
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
