package net.fortytwo.smsn.webdav;

import org.apache.jackrabbit.core.TransientRepository;

import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

/**
 * Second hop example. Stores, retrieves, and removes example content.
 */
public class SecondHop {

    /**
     * The main entry point of the example application.
     *
     * @param args command line arguments (ignored)
     * @throws Exception if an error occurs
     */
    public static void main(String[] args) throws Exception {
        System.setProperty("org.apache.jackrabbit.repository.home", "/tmp/repo");

        Repository repository = new TransientRepository();
        Session session = repository.login(
                new SimpleCredentials("username", "password".toCharArray()));
        try {
            Node root = session.getRootNode();

            // Store content
            /*
            Node hello = root.addNode("hello");
            Node world = hello.addNode("world");
            world.setProperty("message", "Hello, World!");
            session.save();
            */

            // Retrieve content
            Node node = root.getNode("hello/world");
            System.out.println(node.getPath());
            System.out.println(node.getProperty("message").getString());

            // Remove content
            //root.getNode("hello").remove();
            session.save();
        } finally {
            session.logout();
        }
    }

}
