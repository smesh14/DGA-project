package com.contactbook;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;


@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/test/resources/features"},
        plugin = {"pretty"}, dryRun = true,
        glue = {"com/contactbook/cucumberglue"})
public class CucumberTestRunnerIT {
}
