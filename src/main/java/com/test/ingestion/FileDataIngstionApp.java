package com.test.ingestion;

import com.test.ingestion.config.SpoolerTemplateConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collection;

@SpringBootApplication(scanBasePackages = {"com.test.ingestion"} , exclude = {DataSourceAutoConfiguration.class})
@EnableConfigurationProperties({SpoolerTemplateConfig.class})
public class FileDataIngstionApp {

    private static final Logger LOG = LoggerFactory.getLogger(FileDataIngstionApp.class);
    /**
     * The context.
     */
    private static ConfigurableApplicationContext context;
    private final Environment env;

    public FileDataIngstionApp(final Environment env) {
        this.env = env;
    }



    /**
     * Gets the context.
     *
     * @return the context
     */
    public final static ConfigurableApplicationContext getContext() {
        return context;
    }

    /**
     * Sets the context.
     *
     * @param context the new context
     */
    public final void setContext(final ConfigurableApplicationContext context) {
        FileDataIngstionApp.context = context;
    }


    /**
     * The main method.
     *
     * @param args the arguments
     *             the cloud foundry environment exception
     */
    public static void main(final String[] args) throws Exception {
        try {
            final SpringApplication app = new SpringApplication(FileDataIngstionApp.class);
            app.run(args);
            //final Environment env = app.run(args).getEnvironment();
        } catch (Exception e) {
            LOG.error("Exception while starting app as: ", e);
        }
    }

    @PostConstruct
    public void initApplication() {
        final Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());

    }


}
