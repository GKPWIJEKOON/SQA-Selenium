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

public class FlipImageTest {

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
    public void testFlipImageHorizontalAndVertical() {
        System.out.println("\n========== STARTING: Flip Image Test ==========");

        String projectPath = System.getProperty("user.dir");
        String filePath = projectPath + "\\src\\test\\resources\\image02.png";

        try {
            driver.get("https://www.pixelssuite.com/flip-image");

            // Step 1: Upload the image
            WebElement fileInput = driver.findElement(By.cssSelector("input[type='file']"));
            js.executeScript("arguments[0].style.display='block';arguments[0].style.visibility='visible';arguments[0].style.opacity='1';", fileInput);
            fileInput.sendKeys(filePath);
            System.out.println("Step 1: Image uploaded for flipping.");

            // Step 2: Tick both Horizontal and Vertical checkboxes
            // We wait for the checkboxes to be visible after the preview renders
            WebElement flipHorizontal = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@type='checkbox'][following-sibling::text()[contains(.,'Horizontal')]] | //label[contains(.,'Horizontal')]/input")
            ));
            WebElement flipVertical = driver.findElement(
                    By.xpath("//input[@type='checkbox'][following-sibling::text()[contains(.,'Vertical')]] | //label[contains(.,'Vertical')]/input")
            );

            // Use JavaScript to click to ensure it bypasses any custom styling overlays
            if (!flipHorizontal.isSelected()) {
                js.executeScript("arguments[0].click();", flipHorizontal);
            }
            if (!flipVertical.isSelected()) {
                js.executeScript("arguments[0].click();", flipVertical);
            }
            System.out.println("Step 2: Both Horizontal and Vertical flip options ticked.");

            // Step 3: Trigger Download
            WebElement downloadBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Download')]")
            ));

            js.executeScript("arguments[0].click();", downloadBtn);
            System.out.println("Step 3: Flipped Image Download triggered.");

            Thread.sleep(5000);
            System.out.println("========== PASSED: Flip Image Functional Test ==========");

        } catch (Exception e) {
            System.out.println("========== FAILED ==========");
            Assert.fail("Test failed: " + e.getMessage());
        }
    }
}