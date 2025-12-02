import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import config.user_config.storage_config.*;
import exception.connection.ConnectionTestException;

public class StorageConfigTest {
    @Test
    public void testConnectionTester() throws ConnectionTestException {
       System.out.println(ConfigService.testConnection());
    }  

    @Test
    public void testGetDestinationType() {
        System.out.println(ConfigService.getDestinationType());
    }

    // @Test
    // public void testTestLocalDestination() {
    //     System.out.println(ConfigService.getLocalDest());
    // }
}
