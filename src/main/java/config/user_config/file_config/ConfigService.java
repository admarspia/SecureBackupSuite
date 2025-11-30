package config.user_config.file_config;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import config.user_config.*;


public class ConfigService{
    private static BackupFilesConfigModel m;
    private static RootConfig root ;
    static {
        try {
            UserConfigLoader loader = new UserConfigLoader();
            root = loader.load();
            m = root.getFiles();

            ConfigValidator.validate(m);
        } catch (Exception ex) {
            System.err.println("Faild to initailize Loader: " + ex);
            m = null;
        }
    }

    public static List<Set<String>> getFiles() {
        List<Set<String>> files = new ArrayList<>();
        files.add(m.getSourcePaths());
        files.add(m.getIncludePatterns());
        files.add(m.getExcludePatterns());

        return files;
    }

}
