package recovery;

public class Recovery {
    public static void recover(RecoveryModel.Type type) {
        try {
            Recoveryable service = RecoveryServiceResolver.getService(type);
            RecoveryController controller = new RecoveryController(service);
            controller.runRecovery();

            System.out.println("Recovery daemon running...");
        } catch (Exception ex) {
            System.out.println("Error: " + ex);
        }
    }
}

