import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import config.user_config.schedule_config.*;
import utils.*;
import java.time.*;

public class ScheduleConfigTest {
    private BackupScheduleConfigModel.Status s = BackupScheduleConfigModel.Status.SUCCESS;

    @Test
    void testScheduleConfig() {
        assertDoesNotThrow(() -> {
            ZonedDateTime t = ConfigService.getNextRuntime(s);
            System.out.println(t); 
        });
    }
}
