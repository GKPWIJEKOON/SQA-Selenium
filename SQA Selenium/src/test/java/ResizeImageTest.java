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

public class ResizeImageTest {

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
    public void testResizeImageAndDownload() {
        System.out.println("\n========== STARTING: Resize Image Test ==========");
        String projectPath = System.getProperty("user.dir");
        String filePath = projectPath + "\\src\\test\\resources\\image02.png";

        try {
            driver.get("https://www.pixelssuite.com/resize-image");

            // Step 1: Upload the image
            WebElement fileInput = driver.findElement(By.cssSelector("input[type='file']"));
            js.executeScript("arguments[0].style.display='block';arguments[0].style.visibility='visible';arguments[0].style.opacity='1';", fileInput);
            fileInput.sendKeys(filePath);
            System.out.println("Step 1: Image uploaded.");

            // Step 2: Change Width (W) and Height (H)
            // We wait for the input fields to appear after upload
            WebElement widthInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='number'][1]")));
            WebElement heightInput = driver.findElement(By.xpath("//input[@type='number'][2]"));

            // Clear existing values and set new ones (e.g., 1920x1080)
            widthInput.clear();
            widthInput.sendKeys("1920");

            heightInput.clear();
            heightInput.sendKeys("1080");
            System.out.println("Step 2: Width set to 1920, Height set to 1080.");

            // Step 3: Trigger Download
            WebElement downloadBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Download')]")
            ));

            js.executeScript("arguments[0].click();", downloadBtn);
            System.out.println("Step 3: Resized Image Download triggered.");

            Thread.sleep(5000);
            System.out.println("========== PASSED: Resize Image Functional Test ==========");

        } catch (Exception e) {
            System.out.println("========== FAILED ==========");
            Assert.fail("Test failed: " + e.getMessage());
        }
    }
}