package recovery;

public class RecoveryController<T extends Recoveryable> {

    private final T recoveryService;

    public RecoveryController(T recoveryService) {
        this.recoveryService = recoveryService;
    }

    public void runRecovery() throws Exception {
        try {
            recoveryService.recover();
        } catch (Exception ex) {
            throw ex;
        }
    }
}

