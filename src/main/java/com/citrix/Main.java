package com.citrix;

import com.citrix.config.ThunderProperties;
import com.citrix.core.InstantStressTester;
import com.citrix.core.LoopStressTester;
import com.citrix.core.StressTester;
import com.citrix.data.InputReader;
import com.citrix.scenario.student.StudentScenario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ThunderProperties.class)
public class Main implements CommandLineRunner {

    private final ThunderProperties thunderProperties;

    @Autowired
    public Main(ThunderProperties thunderProperties) {
        this.thunderProperties = thunderProperties;
    }

    public static void main(String[] args) {
        try {
            SpringApplication.run(Main.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run(String... var1) throws Exception {
        System.out.println("\nScale simulation has started....");
        System.out.println("Number of processors: " + Runtime.getRuntime().availableProcessors());

        System.out.println("Preparing the test...");
        StressTester stressTester = new LoopStressTester(
                thunderProperties,
                InputReader.read(),
                StudentScenario.getBuilder());

        stressTester.writeMetricsTo(System.out);

        stressTester.stress();

        System.out.println("Scale simulation has ended....");
    }
}

