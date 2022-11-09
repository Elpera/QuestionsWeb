package flights.api_tests;

import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;

public class TestNG {

    WebDriver driver;

    @Test
    public void f() {
        driver = new ChromeDriver();
        String url = "https://www.google.com";
        driver.get(url);
        // Capturing the title and validating if expected is equal to actual
        String expectedTitle = "Google";
        String actualTitle = driver.getTitle();
        Assert.assertEquals(actualTitle, expectedTitle);
    }

    @BeforeMethod
    public void beforeMethod() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
		driver.get("http://localhost:3000/");
        System.out.println("Starting the browser session");
    }

    @AfterMethod
    public void afterMethod() {
        System.out.println("Closing the browser session");
        driver.quit();
    }

    public void login(String user, String pass) {
        WebElement username = driver.findElement(By.id("username"));
        username.sendKeys(user);
        WebElement password = driver.findElement(By.id("password"));
        password.sendKeys(pass);

        WebElement login = driver.findElement(By.id("login"));
        login.click();
    }

    public void selectFlightDetails(String originToSelect, int day) {
        WebElement origin = driver.findElement(By.id("origin"));
        origin.click();
        WebElement originOption = driver.findElement(By.id(originToSelect));
        originOption.click();
        WebElement destination = driver.findElement(By.id("destination"));
        destination.click();
        int var = 1;
        WebElement destinationSelection = driver.findElement(By.xpath(
                "/html[1]/body[1]/div[1]/div[1]/main[1]/div[1]/div[1]/div[2]/div[2]/select[1]/option[" + var + "]"));
        destinationSelection.click();
        WebElement selectDay = driver.findElement(By.xpath(
                "//body/div[@id='__next']/div[@id='app_wrapper']/main[1]/div[1]/div[1]/div[3]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[2]/button["
                        + day + "]"));
        selectDay.click();

        WebElement searchFlight = driver.findElement(By.id("search"));
        searchFlight.click();
    }
    // @Parameters({"calcs","result"})
    // @Test
    // public void runTestCalculator(@Optional("2+3") String text,@Optional("5")
    // String result){
    //
    // WebElement accept = driver.findElement(By.id("L2AGLb"));
    // WebElement searchBar =
    // driver.findElement(By.xpath("//body/div[1]/div[3]/form[1]/div[1]/div[1]/div[1]/div[1]/div[2]/input[1]"));
    // accept.click();
    // searchBar.sendKeys(text);
    // searchBar.submit();
    // WebElement calculatorResult = driver.findElement(By.id("cwos"));
    //
    // Assert.assertEquals(calculatorResult.getText(),result);
    // }

    @Parameters({ "login", "pass", "city", "day", "result" })
    @Test
    public void testSelectFlight(@Optional("pera") String login, @Optional("pass") String pass,
            @Optional("Sao Paulo") String city, @Optional("1") String day, @Optional("Available Flights") String result)
            throws InterruptedException {
        login(login, pass);
        Thread.sleep(1000);
        selectFlightDetails(city, Integer.parseInt(day) + 1);
        Thread.sleep(1000);

        WebElement available = driver.findElement(By.xpath("//div[contains(text(),'Available Flights')]"));
        Assert.assertEquals(available.getText(), result);
    }

    // @Parameters({"login","pass","city","day","result"})
    @Test
    public void testInsertingUsersInformation() throws InterruptedException {
        login("pera", "pass");
        Thread.sleep(1000);
        selectFlightDetails("Sao Paulo", Integer.parseInt("11") + 1);
        Thread.sleep(1000);
        WebElement flight = driver
                .findElement(By.xpath("//body/div[@id='__next']/div[@id='app_wrapper']/main[1]/div[1]/div[3]/a[1]"));
        flight.click();
        Thread.sleep(1000);
        WebElement name = driver.findElements(By.xpath("//input[@id='name']")).get(0);
        WebElement surname = driver.findElements(By.xpath("//input[@id='surname']")).get(0);
        WebElement nationality = driver.findElements(By.xpath("//input[@id='nationality']")).get(0);
        WebElement identification = driver.findElements(By.xpath("//input[@id='identification']")).get(0);
        WebElement age = driver.findElements(By.xpath("//select[@id='age']")).get(0);

        WebElement bags = driver.findElements(By.xpath("//select[@id='bags']")).get(0);

        name.sendKeys("Eric");
        surname.sendKeys("Damiani");
        nationality.sendKeys("Brazilian");
        identification.sendKeys("YEA123632342");
        age.click();
        WebElement agePicker = driver
                .findElements(By.xpath("//option[contains(text(),'Between 2 and 9 years (inclusive)')]")).get(0);
        agePicker.click();
        Thread.sleep(1000);
        bags.click();
        WebElement bagPicker = driver.findElements(By.xpath("//option[contains(text(),'Yes')]")).get(0);
        bagPicker.click();

        WebElement saveButton = driver.findElements(By.xpath("//button[@id='save']")).get(0);
        saveButton.click();

    }
}