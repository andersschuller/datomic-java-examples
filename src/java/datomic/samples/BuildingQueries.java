package datomic.samples;

import datomic.Connection;
import datomic.Database;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

import static datomic.Peer.query;
import static datomic.Util.list;
import static datomic.samples.Fns.scratchConnection;
import static datomic.samples.IO.transactAllFromResource;
import static datomic.samples.PrettyPrint.print;

public class BuildingQueries {

    public static void queryWithVariableBinding(Database db) {
        print(query("[:find [?e ...]" +
                    " :in $ ?name" +
                    " :where [?e :user/firstName ?name]]",
                    db, "Stewart"));
    }

    public static void queryWithCollectionBinding(Database db) {
        print(query("[:find [?e ...]" +
                    " :in $ [?name ...]" +
                    " :where [?e :user/firstName ?name]]",
                    db, list("Stewart", "Stuart")));
    }

    public static void queryWithMultipleCollectionBindings(Database db) {
        print(query("[:find [?e ...]" +
                    " :in $ [?name ...] [?attr ...]" +
                    " :where [?e ?attr ?name]]",
                    db, list("Stuart", "Stewart"), list(":user/firstName", ":user/lastName")));
    }

    public static void queryWithMapForm(Database db) {
        print(query("{:find [[?e ...]]" +
                    " :in [$ ?fname ?lname]" +
                    " :where [[?e :user/firstName ?fname]" +
                    "         [?e :user/lastName ?lname]]}",
                db, "Stuart", "Smalley"));
    }

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        Connection conn = scratchConnection();
        transactAllFromResource(conn, "datomic-java-examples/social-news.edn");
        transactAllFromResource(conn, "datomic-java-examples/stuarts.edn");
        Database db = conn.db();

        queryWithVariableBinding(db);
        queryWithCollectionBinding(db);
        queryWithMultipleCollectionBindings(db);
        queryWithMapForm(db);
    }
}
