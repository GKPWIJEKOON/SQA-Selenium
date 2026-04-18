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

public class CompressImageTest {

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
    public void testImageCompressionAndDownload() {
        System.out.println("\n========== STARTING: Compress Image Test ==========");

        String projectPath = System.getProperty("user.dir");
        String filePath = projectPath + "\\src\\test\\resources\\image01.jpg";

        try {
            driver.get("https://www.pixelssuite.com/compress-image");

            // Step 1: Upload the image
            WebElement fileInput = driver.findElement(By.cssSelector("input[type='file']"));
            js.executeScript("arguments[0].style.display='block';arguments[0].style.visibility='visible';arguments[0].style.opacity='1';", fileInput);
            fileInput.sendKeys(filePath);
            System.out.println("Step 1: Image uploaded for compression.");

            // Step 2: Adjust Compression Quality Slider
            // We target the range input (slider)
            WebElement qualitySlider = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='range']")));

            // Set quality to 80% and trigger the 'input' event so the site updates
            js.executeScript("arguments[0].value = '80'; arguments[0].dispatchEvent(new Event('input'));", qualitySlider);
            System.out.println("Step 2: Compression quality adjusted to 80%.");

            // Step 3: Trigger Download
            // We use the blue Download button shown in your screenshot
            WebElement downloadBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Download')]")
            ));

            js.executeScript("arguments[0].click();", downloadBtn);
            System.out.println("Step 3: Compressed Image Download triggered.");

            Thread.sleep(5000);
            System.out.println("========== PASSED: Compress Image Functional Test ==========");

        } catch (Exception e) {
            System.out.println("========== FAILED ==========");
            Assert.fail("Test failed: " + e.getMessage());
        }
    }
}