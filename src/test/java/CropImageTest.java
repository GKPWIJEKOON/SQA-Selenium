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

public class CropImageTest {

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
    public void testCropPngAndDownload() {
        System.out.println("\n========== STARTING: Crop PNG Test ==========");

        String projectPath = System.getProperty("user.dir");
        String filePath = projectPath + "\\src\\test\\resources\\image02.png";

        try {
            driver.get("https://www.pixelssuite.com/crop-png");

            // Step 1: Upload the PNG
            WebElement fileInput = driver.findElement(By.cssSelector("input[type='file']"));
            js.executeScript("arguments[0].style.display='block';arguments[0].style.visibility='visible';arguments[0].style.opacity='1';", fileInput);
            fileInput.sendKeys(filePath);
            System.out.println("Step 1: PNG uploaded for cropping.");

            // Step 2: Adjust X, Y, Width, and Height
            // We wait for the coordinate inputs to appear after the preview loads
            WebElement xInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='X' or @type='number'][1]")));
            WebElement yInput = driver.findElement(By.xpath("//input[@placeholder='Y' or @type='number'][2]"));
            WebElement widthInput = driver.findElement(By.xpath("//input[@placeholder='Width' or @type='number'][3]"));
            WebElement heightInput = driver.findElement(By.xpath("//input[@placeholder='Height' or @type='number'][4]"));

            // Setting a 500x500 crop from the top-left (X=50, Y=50)
            xInput.clear();
            xInput.sendKeys("50");

            yInput.clear();
            yInput.sendKeys("50");

            widthInput.clear();
            widthInput.sendKeys("500");

            heightInput.clear();
            heightInput.sendKeys("500");
            System.out.println("Step 2: Crop coordinates set (50, 50) and size set to 500x500.");

            // Step 3: Trigger Download
            WebElement downloadBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Download')]")
            ));

            js.executeScript("arguments[0].click();", downloadBtn);
            System.out.println("Step 3: Cropped PNG Download triggered.");

            Thread.sleep(5000);
            System.out.println("========== PASSED: Crop PNG Functional Test ==========");

        } catch (Exception e) {
            System.out.println("========== FAILED ==========");
            Assert.fail("Test failed: " + e.getMessage());
        }
    }
}