package bzu.edu.hotelManagmentAPI.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationRunnerConfig {

    @Autowired
    private SampleDataLoader sampleDataLoader;

    @Bean
    CommandLineRunner run() {
        return args -> {
            sampleDataLoader.loadData();
        };
    }
}
