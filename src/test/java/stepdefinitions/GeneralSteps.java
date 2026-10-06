package stepdefinitions;

import io.cucumber.java.en.*;
import utils.ConfigReader;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * GeneralSteps
 */
public class GeneralSteps {

    private WebDriver driver;

    @Given("A user goes to Ria Money Transfer website")
    public void aUserGoesToRiaMoneyTransferWebsite() {
        String url = ConfigReader.getProperty("ria.url");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(url);
    }

    @Given("The user types {string} into the amount field")
    public void theUserTypesIntoTheAmountField(String amount) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement amountField = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input#amount-from")));        
        amountField.click();
        amountField.sendKeys(Keys.HOME);
        amountField.sendKeys(Keys.chord(Keys.SHIFT, Keys.END));
        amountField.sendKeys(Keys.BACK_SPACE);
        wait.until(d -> {
            String currentValue = amountField.getDomProperty("value");
            return currentValue == null || currentValue.isEmpty();
        });
        amountField.sendKeys(amount);
    }

    @When("The user selects the country {string} in the search field with {string} currency")
    public void theUserSelectsTheCountryInTheFieldWithCurrency(String country, String currency) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement countryDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("img[alt='CO flag']")));
        countryDropdown.click();

        WebElement modalTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[text()='Select Destination']")));
        assertEquals("Select Destination", modalTitle.getText());

        WebElement searchInput = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[type='search']")));
        searchInput.sendKeys(country);

        WebElement countryButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[@role='option'][.//span[text()='" + country + "']]")
        ));
        countryButton.click();

        WebElement currencyOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='" + currency + "']")));
        currencyOption.click();
    }

    @Then("the conversion should be performed successfully")
    public void the_conversion_should_be_performed_successfully() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        WebElement startTransferBtn = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//a[contains(., 'Start your transfer')]")
        ));
        startTransferBtn.click();

        WebElement welcomeTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("div.oen-ui-card-header-title")
        ));
        assertEquals("Welcome", welcomeTitle.getText());

        WebElement registerLink = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//span[contains(@class, 'oen-ui-link-text') and (contains(., 'Register'))]")
        ));
        assertTrue(registerLink.isDisplayed());

        WebElement emailLabel = driver.findElement(
            By.xpath("//label[contains(@class, 'oen-ui-input-label') and contains(text(), 'Phone or email')]")
        );
        assertTrue(emailLabel.isDisplayed());

        WebElement emailInput = driver.findElement(By.cssSelector("input[analytics-name='login-email-input']"));
        assertTrue(emailInput.isDisplayed());

        WebElement passwordLabel = driver.findElement(
            By.xpath("//label[contains(@class, 'oen-ui-input-label') and contains(text(), 'Password')]")
        );
        assertTrue(passwordLabel.isDisplayed());

        WebElement passwordInput = driver.findElement(By.cssSelector("input[analytics-name='login-password']"));
        assertTrue(passwordInput.isDisplayed());

        try {
            WebElement allowCookiesBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("button[analytics-name='consent-manager-allow-all-cookies']")
        ));
        allowCookiesBtn.click();
        } catch (Exception ignored) {
        }

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", registerLink);
        
        boolean isCountrySelectionPage = wait.until(url -> driver.getCurrentUrl().toLowerCase().contains("registration"));
        assertTrue(isCountrySelectionPage, "Country Selection Page is loaded");
    }
}

   // TODO: implement pages and methods according to pages, move locator to pages
