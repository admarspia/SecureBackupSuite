package recovery;

public class RecoveryServiceResolver {

    public static Recoveryable getService(RecoveryModel.Type type) throws Exception {
        switch (type) {
            case FULL:
                return new FullRecoveryService();
            case SELECTIVE:
                return new SelectiveRecoveryService("test.java", "20251211232834");
            default:
                throw new UnsupportedOperationException("Unsupported Operation: " + type);
        }
    }
}

