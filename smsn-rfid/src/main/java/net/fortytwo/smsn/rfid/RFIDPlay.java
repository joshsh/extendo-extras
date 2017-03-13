package net.fortytwo.smsn.rfid;

import net.fortytwo.smsn.brain.TreeViews;
import net.fortytwo.smsn.brain.model.Filter;
import net.fortytwo.smsn.brain.model.TopicGraph;
import net.fortytwo.smsn.brain.model.entities.Atom;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Logger;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class RFIDPlay {
    private static final Logger logger = Logger.getLogger(RFIDPlay.class.getName());

    public RFIDPlay(final String graphUri) throws Exception {

        // TODO: restore remote graph
        //RexsterGraph g = new RexsterGraph(graphUri);
        TopicGraph graph = null; //new TopicGraph(g);

        RFIDListener l = new RFIDListener();

        RFIDReader reader = new RFIDReader(l.firstReader(), 4, System.out);

        //String readerName = reader.doReaderCommand("get ReaderName");
        //logger.info("reader name: " + readerName);

        Filter f = new Filter();

        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            reader.clear();

            for (int i = 0; i < 10; i++) {
            reader.readTags();
            }

            //System.out.println("[tag list]");
            for (String tagId : reader.getTagIds()) {
                String v = "RFID: " + tagId;
                Collection<Atom> atoms = graph.getAtomsByTitleQuery(v, Filter.noFilter());
                if (0 == atoms.size()) {
                    System.out.println("         * " + v);
                } else {
                    if (atoms.size() > 1) {
                        logger.warning("multiple atoms with value: " + v);
                    }

                    for (Atom a : atoms) {
                        System.out.println("* :" + a.getId() + ": " + a.getTitle());
                        for (Atom n : getParents(a, f)) {
                            System.out.println("* :" + n.getId() + ": " + n.getTitle());
                        }
                        System.out.println("");
                    }
                }
            }

            System.out.println("");

            String command = r.readLine();
            if (command.toLowerCase().equals("quit")) {
                break;
            }
        }
    }

    private static Collection<Atom> getParents(final Atom a,
                                               final Filter f) {
        Collection<Atom> n = new LinkedList<Atom>();

        //for (Atom at : NoteQueries.FORWARD_ADJACENCY.getLinked(a, f)) {
        //    n.add(at);
        //}

        for (Atom at : TreeViews.backwardViewStyle.getLinked(a, f)) {
            n.add(at);
        }

        return n;
    }

    public static void main(final String args[]) {
        try {
            new RFIDPlay("http://localhost:8182/graphs/joshkb");
        } catch (Throwable e) {
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }
}
