package recovery;

public class RecoveryServiceResolver {

    public static Recoveryable getService(RecoveryModel.Type type) throws Exception {
        switch (type) {
            case FULL:
                return new FullRecoveryService();
            case SELECTIVE:
                return new SelectiveRecoveryService();
            default:
                throw new UnsupportedOperationException("Unsupported Operation: " + type);
        }
    }
}

