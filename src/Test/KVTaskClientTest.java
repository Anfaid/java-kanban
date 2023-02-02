package Test;
import Server.KVServer;
import Server.KVTaskClient;
import org.junit.jupiter.api.*;
import Server.KVServer;
import Server.KVTaskClient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
public class KVTaskClientTest {

    private KVTaskClient taskClient;
    private static KVServer kvServer;

    KVTaskClientTest() {
    }

    @BeforeEach
    void BeforeEach() throws IOException, InterruptedException, URISyntaxException {
        URI uri =new URI("http://localhost:8078");
        this.taskClient = new KVTaskClient(uri );
    }

    @BeforeAll
    static void BeforeAll() {
        try {
            kvServer = new KVServer();
            kvServer.start();
        } catch (IOException var1) {
            kvServer = null;
        }

    }

    @AfterAll
    static void AfterAll() {
        if (kvServer != null) {
            kvServer.stop();
        }

    }

    @Test
    void shouldPutAndLoadServer() throws IOException, InterruptedException {
        try {
            String str = this.getClass().getSimpleName();
            String key = str + Instant.now().getEpochSecond();
            Assertions.assertThrows(IOException.class, () -> {
                this.taskClient.load((String) null);
            });
            Assertions.assertThrows(IOException.class, () -> {
                this.taskClient.load(key);
            });
            this.taskClient.put(key, key);
            Assertions.assertEquals(key, this.taskClient.load(key));
            this.taskClient.put(key, key + key);
            Assertions.assertEquals(key + key, this.taskClient.load(key));
        } catch (Throwable e) {
            throw e;
        }
    }
}
