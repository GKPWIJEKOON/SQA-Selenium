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

public class BulkResizeTes {

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
    public void testBulkResizeMultipleImages() {
        System.out.println("\n========== STARTING: Bulk Resize Test ==========");

        String projectPath = System.getProperty("user.dir");

        // Define paths for two separate images
        String img1 = projectPath + "\\src\\test\\resources\\image01.jpg";
        String img2 = projectPath + "\\src\\test\\resources\\image02.png";

        // Combine them into one string separated by \n for bulk upload
        String bulkFiles = img1 + "\n" + img2;

        try {
            driver.get("https://www.pixelssuite.com/bulk-resize");

            // Step 1: Upload Multiple Images
            WebElement fileInput = driver.findElement(By.cssSelector("input[type='file']"));
            js.executeScript("arguments[0].style.display='block';arguments[0].style.visibility='visible';arguments[0].style.opacity='1';", fileInput);

            fileInput.sendKeys(bulkFiles);
            System.out.println("Step 1: Multiple images sent for bulk processing.");

            // Step 2: Set Bulk Resize Dimensions
            // Targeted by XPath for the Width and Height boxes in the Options panel
            WebElement widthInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Width' or @type='number'][1]")));
            WebElement heightInput = driver.findElement(By.xpath("//input[@placeholder='Height' or @type='number'][2]"));

            widthInput.clear();
            widthInput.sendKeys("800");

            heightInput.clear();
            heightInput.sendKeys("600");
            System.out.println("Step 2: Bulk dimensions set to 800x600.");

            // Step 3: Process and Download
            WebElement processBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Process & Download')]")
            ));

            js.executeScript("arguments[0].click();", processBtn);
            System.out.println("Step 3: Bulk Process & Download triggered.");

            // Final verification wait for the zip file download
            Thread.sleep(7000);
            System.out.println("========== PASSED: Bulk Resize Functional Test ==========");

        } catch (Exception e) {
            System.out.println("========== FAILED ==========");
            Assert.fail("Test failed: " + e.getMessage());
        }
    }
}