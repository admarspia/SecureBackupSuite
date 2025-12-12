package backup;

public class BackupModel {
    public enum Type {FULL, INCREMENTAL, PREDICTIVE };
    private Type type;

    public void setType(Type type) { this.type = type; }
    public Type getType() { return type; }
}
