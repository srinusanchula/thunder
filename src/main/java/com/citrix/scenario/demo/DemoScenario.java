package com.citrix.scenario.demo;

import com.citrix.core.Request;
import com.citrix.core.Scenario;
import com.citrix.data.Input;

//@Component
public class DemoScenario extends Scenario {
    private static final String NAME = "DEMO";

    private DemoScenario(Input input) {
        super(input);
    }

    public static Builder getBuilder() {
        return input -> new DemoScenario(input);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Request[] init() {
        return new Request[]{new HelloRequest("http://localhost:8080"), new DeviceRequest("http://localhost:8182")};
    }

}
