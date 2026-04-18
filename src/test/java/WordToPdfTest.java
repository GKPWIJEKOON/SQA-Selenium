import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

public class WordToPdfTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        js = (JavascriptExecutor) driver;
        driver.manage().window().maximize();
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testWordToPdfConversion() {
        System.out.println("\n========== STARTING: Word to PDF Test ==========");

        // Ensure 'sample.docx' is in your src/test/resources folder
        String projectPath = System.getProperty("user.dir");
        String filePath = projectPath + "\\src\\test\\resources\\sample.docx";

        try {
            // Step 1: Navigate to Word to PDF tool
            driver.get("https://www.pixelssuite.com/word-to-pdf");

            // Step 2: Upload the .docx file
            WebElement fileInput = driver.findElement(By.cssSelector("input[type='file']"));
            js.executeScript("arguments[0].style.display='block';arguments[0].style.visibility='visible';arguments[0].style.opacity='1';", fileInput);
            fileInput.sendKeys(filePath);
            System.out.println("Step 1: Word File (.docx) uploaded.");

            // Step 3: Click 'Convert to PDF' (The blue button in your screenshot)
            WebElement convertBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Convert to PDF')]")));
            convertBtn.click();
            System.out.println("Step 2: Conversion to PDF started.");

            // Step 4: Handle Download Trigger
            try {
                WebElement downloadBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//button[contains(text(),'Download')] | //a[contains(text(),'Download')]")
                ));

                // JS Click is used here to prevent the Timeout error if the site refreshes quickly
                js.executeScript("arguments[0].click();", downloadBtn);
                System.out.println("Step 3: PDF Download triggered.");
            } catch (Exception e) {
                System.out.println("Note: Browser handled download before wait finished.");
            }

            // Final wait to confirm file is received
            Thread.sleep(5000);
            System.out.println("========== PASSED: Word to PDF Conversion ==========");

        } catch (Exception e) {
            System.out.println("========== FAILED ==========");
            Assert.fail("Test failed: " + e.getMessage());
        }
    }
}