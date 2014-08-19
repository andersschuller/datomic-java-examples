package datomic.samples;

import datomic.Connection;
import datomic.Database;
import datomic.Datom;
import datomic.Peer;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import static datomic.Peer.q;
import static datomic.samples.Fns.scratchConnection;
import static datomic.samples.IO.resource;
import static datomic.samples.IO.transactAll;

public class DatabaseFiltering {
    public static Object tempid() {
        return Peer.tempid("db.part/user");
    }

    public static final String storyQuery = "[:find (count ?e) :where [?e :story/url]]";

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        Connection conn = scratchConnection();
        URL url = resource("datomic-java-examples/social-news.edn");
        transactAll(conn, new InputStreamReader(url.openStream()));
        Database db = conn.db();

        System.out.print("\nTotal number of stories: ");
        System.out.println(q(storyQuery, db));

        System.out.print("\nTotal number of published stories: ");
        System.out.println(q(storyQuery, db.filter(new Database.Predicate<datomic.Datom>() {
            public boolean apply(Database db, Datom datom) {
                return db.entity(datom.tx()).get(":publish/at") != null;
            }
        })));
    }
}
