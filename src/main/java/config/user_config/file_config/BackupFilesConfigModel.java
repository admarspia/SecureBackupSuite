package config.user_config.file_config;

import java.util.Set;
import java.util.TreeSet;

public class BackupFilesConfigModel {

    private Set<String> sourcePaths = new TreeSet<>();
    private Set<String> includePatterns = new TreeSet<>();
    private Set<String> excludePatterns = new TreeSet<>();
    private boolean followSymlinks = false;  
    private final int maxFollowDepth = 10;

    public void setSourcePaths(Set<String> sourcePaths) {
        this.sourcePaths.clear();
        this.sourcePaths.addAll(sourcePaths);
    }

    public void setIncludePatterns(Set<String> includePatterns) {
        this.includePatterns.clear();
        this.includePatterns.addAll(includePatterns);
    }

    public void setExcludePatterns(Set<String> excludePatterns) {
        this.excludePatterns.clear();
        this.excludePatterns.addAll(excludePatterns);
    }

    public void setFollowSymlinks(boolean followSymlinks) {
        this.followSymlinks = followSymlinks;
    }
    

    public Set<String> getSourcePaths() {
        return this.sourcePaths;
    }

    public Set<String> getIncludePatterns() {
        return this.includePatterns;
    }

    public Set<String> getExcludePatterns() {
        return this.excludePatterns;
    }

    public boolean getFollowSymlinks() {
        return this.followSymlinks;
    }

    public int getMaxFollowLink() {
        return this.maxFollowDepth;
    }

    @Override
    public String toString() {
        return "BackupFilesConfigModel {" +
                "\n  sourcePaths=" + sourcePaths +
                ",\n  includePatterns=" + includePatterns +
                ",\n  excludePatterns=" + excludePatterns +
                ",\n  followSymlinks=" + followSymlinks +
                "\n}";
    }
}

