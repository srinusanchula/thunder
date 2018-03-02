package com.citrix.scenario.student;

import com.citrix.core.Request;
import com.citrix.core.Scenario;
import com.citrix.data.Input;

public class StudentScenario extends Scenario {
    private static final String NAME = "STUDENT";
    private static final String BASE_URL = "http://localhost:8080";

    private StudentScenario(Input input) {
        super(input);
    }

    public static Builder getBuilder() {
        return input -> new StudentScenario(input);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Request[] init() {
        return new Request[]{new Login(BASE_URL), new Courses(BASE_URL), new Logout(BASE_URL)};
    }

}
