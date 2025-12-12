package recovery;

public class Recovery {
    public static void recover() {
        try {
            Recoveryable service = RecoveryServiceResolver.getService(RecoveryModel.Type.SELECTIVE);
            RecoveryController controller = new RecoveryController(service);
            controller.runRecovery();

            System.out.println("Recovery daemon running...");
        } catch (Exception ex) {
            System.out.println("Error: " + ex);
        }
    }
}

