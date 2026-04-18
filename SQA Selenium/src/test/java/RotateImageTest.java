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

public class RotateImageTest {

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
    public void testRotateImageWithFlips() {
        System.out.println("\n========== STARTING: Rotate Image Test ==========");
        String projectPath = System.getProperty("user.dir");
        String filePath = projectPath + "\\src\\test\\resources\\image02.png";

        try {
            // Step 1: Open the rotate image page
            driver.get("https://www.pixelssuite.com/rotate-image");

            // Step 2: Upload the image
            WebElement fileInput = driver.findElement(By.cssSelector("input[type='file']"));
            js.executeScript(
                    "arguments[0].style.display='block';" +
                            "arguments[0].style.visibility='visible';" +
                            "arguments[0].style.opacity='1';",
                    fileInput
            );
            fileInput.sendKeys(filePath);
            System.out.println("File uploaded: " + filePath);

            // Step 3: Wait for the Rotate panel to fully load
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//button[contains(text(),'+90')]")
            ));
            System.out.println("Rotate panel loaded!");

            // Step 4: Tick Flip H checkbox
            WebElement flipH = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@type='checkbox'][following-sibling::*[contains(text(),'Flip H')] " +
                            "or preceding-sibling::*[contains(text(),'Flip H')]]" +
                            " | //label[contains(text(),'Flip H')]//input" +
                            " | //input[@type='checkbox'][1]")
            ));

            if (!flipH.isSelected()) {
                js.executeScript("arguments[0].click();", flipH);
            }
            System.out.println("Flip H checked: " + flipH.isSelected());

            // Step 5: Tick Flip V checkbox
            WebElement flipV = (WebElement) js.executeScript(
                    "var checkboxes = document.querySelectorAll('input[type=checkbox]');" +
                            "return checkboxes.length > 1 ? checkboxes[1] : null;"
            );

            Assert.assertNotNull(flipV, "Flip V checkbox not found");
            if (!flipV.isSelected()) {
                js.executeScript("arguments[0].click();", flipV);
            }
            System.out.println("Flip V checked: " + flipV.isSelected());

            // Step 6: Verify both checkboxes are ticked
            Assert.assertTrue(flipH.isSelected(), "Flip H should be checked!");
            Assert.assertTrue(flipV.isSelected(), "Flip V should be checked!");
            System.out.println("Both Flip H and Flip V are ticked!");

            // Step 7: Click the Download Rotated button
            WebElement downloadBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//button[contains(text(),'Download')]")
                    )
            );
            js.executeScript("arguments[0].scrollIntoView(true);", downloadBtn);
            Thread.sleep(500);
            js.executeScript("arguments[0].click();", downloadBtn);
            System.out.println("Download Rotated button clicked!");

            // Step 8: Wait for download to complete
            Thread.sleep(3000);
            System.out.println("========== PASSED: Rotate Image Test ==========");

        } catch (Exception e) {
            System.out.println("Error encountered: " + e.getMessage());
            Assert.fail("Rotate image test failed: " + e.getMessage());
        }
    }
}