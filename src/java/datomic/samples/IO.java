package datomic.samples;

import datomic.Connection;
import datomic.Util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class IO {
    public static void transactAll(Connection conn, Reader reader) throws ExecutionException, InterruptedException {
        List<List> txes = Util.readAll(reader);
        for (java.util.Iterator<List> it = txes.iterator(); it.hasNext(); ) {
            List tx =  it.next();
            conn.transact(tx).get();
        }
    }

    public static URL resource(String name) {
        return Thread.currentThread().getContextClassLoader().getResource(name);
    }

    public static void transactAllFromResource(Connection conn, String resource) throws IOException, ExecutionException, InterruptedException {
        URL url = resource(resource);
        transactAll(conn, new InputStreamReader(url.openStream()));
    }

}
