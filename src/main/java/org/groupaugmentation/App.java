package org.groupaugmentation;


import lombok.extern.slf4j.Slf4j;
import org.groupaugmentation.util.Parameters;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

@Slf4j
//TODO catch arithmetic exception for all the possible fails
public class App {


    private Parameters parameters;

    public App(String parameterFileName) {
        this.loadParameters(parameterFileName);
    }

    public static void main(String[] args) {
        String parameterFileName;
        if (args.length == 0) {
            log.warn("no parameters set --> using default.yml parameters");
            parameterFileName = "default";
        } else {
            parameterFileName = args[0];
        }

        App app = new App(parameterFileName);
        app.start();
    }

    public void start() {
        Simulation simulation = new Simulation(parameters);
        simulation.simulate();
    }


    /**
     * load parameters from resource folder
     */
    private void loadParameters(String parameterFileName) {
        final String extension = ".yml";
        final String folder = "parameters/";
        final String filePath = folder + parameterFileName + extension;
        log.info("reading parameters from " + filePath);

        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(filePath);
        this.parameters = yaml.loadAs(inputStream, Parameters.class);
        this.parameters.setParametersFileName(parameterFileName);
    }
}
