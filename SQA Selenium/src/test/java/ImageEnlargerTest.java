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

public class ImageEnlargerTest {

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
    public void testImageEnlargerScaleAndDownload() {
        System.out.println("\n========== STARTING: Image Enlarger Test ==========");

        String projectPath = System.getProperty("user.dir");
        String filePath = projectPath + "\\src\\test\\resources\\image01.jpg";

        try {
            driver.get("https://www.pixelssuite.com/image-enlarger");

            // Step 1: Upload the image
            WebElement fileInput = driver.findElement(By.cssSelector("input[type='file']"));
            js.executeScript("arguments[0].style.display='block';arguments[0].style.visibility='visible';arguments[0].style.opacity='1';", fileInput);
            fileInput.sendKeys(filePath);
            System.out.println("Step 1: Image uploaded for enlargement.");

            // Step 2: Adjust the Scale slider
            // We wait for the slider input to appear after the preview renders
            WebElement scaleSlider = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='range']")));

            // We set the value to '400' (4x enlargement) and trigger the 'input' event 
            // so the website's code notices the change
            js.executeScript("arguments[0].value = '400'; arguments[0].dispatchEvent(new Event('input'));", scaleSlider);
            System.out.println("Step 2: Scale adjusted to 400%.");

            // Step 3: Trigger Download
            WebElement downloadBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Download')]")
            ));

            js.executeScript("arguments[0].click();", downloadBtn);
            System.out.println("Step 4: Enlarged Image Download triggered.");

            Thread.sleep(5000);
            System.out.println("========== PASSED: Image Enlarger Functional Test ==========");

        } catch (Exception e) {
            System.out.println("========== FAILED ==========");
            Assert.fail("Test failed: " + e.getMessage());
        }
    }
}