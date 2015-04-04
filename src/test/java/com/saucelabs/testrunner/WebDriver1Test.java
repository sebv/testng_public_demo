package com.saucelabs.testrunner;
import com.saucelabs.saucerest.SauceREST;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.*;
import org.testng.Assert;

import java.lang.reflect.Method;
import java.util.Map;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertEquals;

/**
 * Simple {@link RemoteWebDriver} test that demonstrates how to run your Selenium tests with <a href="http://saucelabs.com/ondemand">Sauce OnDemand</a>.
 *
 * @author Ross Rowe
 */
public class WebDriver1Test {

    private WebDriver driver;
    String jobId;
    private SauceREST sauceREST;

    /**
     * Creates a new {@link RemoteWebDriver} instance to be used to run WebDriver tests using Sauce.
     *
     * @param method the test method being executed
     * @throws Exception thrown if any errors occur in the creation of the WebDriver instance
     */
    @BeforeMethod
    public void setUp(Method method) throws Exception {
        Map<String, String> env = System.getenv();
        String username = env.get("SAUCE_USERNAME");
        String key = env.get("SAUCE_ACCESS_KEY");
        sauceREST = new SauceREST(username, key);
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName("firefox");
        capabilities.setCapability("version", "17");
        capabilities.setCapability("platform", "Windows 2003");
        capabilities.setCapability("name", method.getName());
        this.driver = new RemoteWebDriver(
                new URL("http://" + username + ":" + key + "@ondemand.saucelabs.com:80/wd/hub"),
                capabilities);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        this.jobId = ((RemoteWebDriver) driver).getSessionId().toString();
    }

    @Test
    public void webDriver1() throws Exception {
        driver.get("http://www.amazon.com/");
        Assert.assertEquals(driver.getTitle(), "Amazon.com: Online Shopping for Electronics, Apparel, Computers, Books, DVDs & more");
    }

    @AfterMethod
    public void tearDown() throws Exception {
        sauceREST.jobPassed(jobId);
        driver.quit();
    }
}
