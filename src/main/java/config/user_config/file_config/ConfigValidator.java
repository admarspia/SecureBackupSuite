package config.user_config.file_config;

import java.util.Set;
import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

public class ConfigValidator {
    public static void validate(BackupFilesConfigModel m)
        throws  IllegalArgumentException
    {
         
            Set<String> sources = m.getSourcePaths();
            Set<String> includes = m.getIncludePatterns();
            Set<String> excludes = m.getExcludePatterns();

            if (sources.isEmpty())
                throw new IllegalArgumentException("Source path can't be empty.");

            for (String source : sources){
                if (source.isBlank() || !isValidPath(source))
                    throw new IllegalArgumentException("Invalid Path: " + source);
            }

            if (includes.isEmpty())
                throw new IllegalArgumentException("Include patterns can't be empty.");
            
            for (String include: includes){
                if (include.isBlank() || !isValidFormat(include))
                    throw new IllegalArgumentException("Invalid include pattern: " + include);
            }

            if (excludes.isEmpty())
                throw new IllegalArgumentException("Exclude patterns can't be empty.");

            for (String exclude : excludes){
                if (exclude.isBlank() || !isValidFormat(exclude))
                    throw new IllegalArgumentException("Exclude format can't be empty");
            }
    }

    private static boolean isValidFormat(String pattern)  {
        try {
            PathMatcher p = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
            return true;
        } catch (IllegalArgumentException ex){
            return false;
        }
    }

    private static boolean isValidPath(String path){
      Path p = Paths.get(path);
      return Files.exists(p);

    }

}
