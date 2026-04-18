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

public class PdfEditorTest {

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
    public void testPdfUploadAndDownloadOnly() {
        System.out.println("\n========== STARTING: PDF Editor Upload/Download Only ==========");
        String projectPath = System.getProperty("user.dir");
        String filePath = projectPath + "\\src\\test\\resources\\sample.pdf";

        try {
            driver.get("https://www.pixelssuite.com/pdf-editor");

            // Step 1: Upload
            WebElement fileInput = driver.findElement(By.cssSelector("input[type='file']"));
            js.executeScript("arguments[0].style.display='block';arguments[0].style.visibility='visible';arguments[0].style.opacity='1';", fileInput);
            fileInput.sendKeys(filePath);
            System.out.println("Step 1: PDF uploaded.");

            // Step 2: Immediate Download
            // We wait for the Download button to appear in the DOM
            WebElement downloadBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//button[contains(.,'Download')] | //a[contains(.,'Download')]")
            ));

            // Use JS Click to trigger the download even if the UI is still loading
            js.executeScript("arguments[0].click();", downloadBtn);
            System.out.println("Step 2: Download triggered.");

            Thread.sleep(5000);
            System.out.println("========== PASSED: PDF Editor Fast Test ==========");

        } catch (Exception e) {
            System.out.println("========== FAILED ==========");
            Assert.fail("Test failed: " + e.getMessage());
        }
    }
}