package config;

import java.io.*;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.LoaderOptions;

public abstract class YamlLoader<T> {

    private final Class<T> type;
    private final String path;

    public YamlLoader(Class<T> type, String path) {
        this.type = type;
        this.path = path;
    }

    public T load() {
        try (FileInputStream fis = new FileInputStream(path)) {

            LoaderOptions options = new LoaderOptions();
            Yaml yaml = new Yaml(new Constructor(type, options));

            T model = yaml.load(fis);
            return (model != null) ? model : loadDefault();

        } catch (Exception e) {
            System.out.println(e);
            return loadDefault();
        }
    }

    public abstract T loadDefault();
}

