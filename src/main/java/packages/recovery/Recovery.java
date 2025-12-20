package recovery;
import utils.compdecomp.FileUtils;
import java.nio.file.Path;

public class Recovery {
    public static void recover(RecoveryModel.Type type) {
        try {
            Recoveryable service = RecoveryServiceResolver.getService(type);
            RecoveryController controller = new RecoveryController(service);
            controller.runRecovery();

            System.out.println("Recovery daemon running...");
        } catch (Exception ex) {
            System.out.println("Error: " + ex);
            FileUtils.cleanup(Path.of("backup_workspace/temp/decrypted"));
            FileUtils.cleanup(Path.of("backup_workspace/temp/compressed"));
            FileUtils.cleanup(Path.of("backup_workspace/temp/recovery_download"));
        }
    }
}

