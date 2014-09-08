package datomic.samples;

import clojure.java.api.Clojure;
import clojure.lang.IFn;
import datomic.Connection;
import datomic.Database;

import java.util.List;

public class QueryExplain {
    public static void main(String... args) {
        IFn require = Clojure.var("clojure.core", "require");
        require.invoke(Clojure.read("datomic-q-explain.core"));

        String query = "[:find ?attr ?type ?card" +
                " :where " +
                " [_ :db.install/attribute ?a] " +
                " [?a :db/valueType ?t] " +
                " [?a :db/cardinality ?c]" +
                " [?a :db/ident ?attr]" +
                " [?t :db/ident ?type]" +
                " [?c :db/ident ?card]]";

        Connection conn = Fns.scratchConnection();
        Database db = conn.db();

        IFn qExplain = Clojure.var("datomic-q-explain.core", "q-explain");
        Object result = qExplain.invoke(Clojure.read(query), db);

        if (result instanceof List) {
            List<?> resultList = (List<?>) result;
            for (Object item : resultList) {
                System.out.println(item);
            }
        }

        IFn shutdownAgents = Clojure.var("clojure.core", "shutdown-agents");
        shutdownAgents.invoke();
    }
}
