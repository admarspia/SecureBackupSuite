package recovery;

public class RecoveryModel {
    public enum Type {FULL, SELECTIVE}
    private Type type;

    public void setType(Type type) { this.type = type; }
    public Type getType() { return type; }
}

