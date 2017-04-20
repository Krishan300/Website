package backend.luna.lehigh.edu;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.auth.AuthDescriptor;
import net.spy.memcached.auth.PlainCallbackHandler;

import java.io.IOException;

/**
 * MemcachedObj - class written to generate a memcached connection object for either file caching or our
 * new hash map-equivalent secret_key storage mechanism. Can be used statically.
 */
public class MemcachedObj {
    public static MemcachedClient getMemcachedConnection(String mc_username, String mc_password, String mc_server_list) {
        AuthDescriptor ad = new AuthDescriptor(new String[] { "PLAIN" },
                new PlainCallbackHandler(mc_username, mc_password));
        try {
            MemcachedClient mc = new MemcachedClient(new ConnectionFactoryBuilder()
                    .setProtocol(ConnectionFactoryBuilder.Protocol.BINARY)
                    .setAuthDescriptor(ad).build(), AddrUtil.getAddresses(mc_server_list));
            return mc;
        }
        catch (IOException ex) {
            System.err.println("Couldn't create a connection to MemCachier: \nIOException " + ex.getMessage());
        }
        return null;
    }
}
