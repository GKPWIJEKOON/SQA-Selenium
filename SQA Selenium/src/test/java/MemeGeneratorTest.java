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

public class MemeGeneratorTest {

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
    public void testMemeTextEntry() {
        System.out.println("\n========== STARTING: Meme Text Entry Test ==========");
        String projectPath = System.getProperty("user.dir");
        String filePath = projectPath + "\\src\\test\\resources\\image02.png";

        try {
            driver.get("https://www.pixelssuite.com/meme-generator");

            // Step 1: Upload the image
            WebElement fileInput = driver.findElement(By.cssSelector("input[type='file']"));
            js.executeScript(
                    "arguments[0].style.display='block';" +
                            "arguments[0].style.visibility='visible';" +
                            "arguments[0].style.opacity='1';",
                    fileInput
            );
            fileInput.sendKeys(filePath);
            System.out.println("File uploaded: " + filePath);

            // Step 2: Wait for the editor UI to appear after upload
            // Wait until at least one input or textarea is visible inside the editor
            wait.until(d -> {
                Object count = js.executeScript(
                        "var inputs = document.querySelectorAll('input:not([type=file]):not([type=checkbox]):not([type=range]), textarea');" +
                                "return inputs.length > 0;"
                );
                return Boolean.TRUE.equals(count);
            });
            System.out.println("Editor fields appeared!");

            // Step 3: Find Top text and Bottom text fields using JS
            // (handles cases where type attribute may be missing or fields are textareas)
            WebElement topText = (WebElement) js.executeScript(
                    "var inputs = document.querySelectorAll('input:not([type=file]):not([type=checkbox]):not([type=range]), textarea');" +
                            "return inputs.length > 0 ? inputs[0] : null;"
            );

            WebElement bottomText = (WebElement) js.executeScript(
                    "var inputs = document.querySelectorAll('input:not([type=file]):not([type=checkbox]):not([type=range]), textarea');" +
                            "return inputs.length > 1 ? inputs[1] : null;"
            );

            Assert.assertNotNull(topText, "Top text field not found");
            Assert.assertNotNull(bottomText, "Bottom text field not found");

            // Step 4: Type into the fields
            js.executeScript("arguments[0].focus();", topText);
            topText.clear();
            topText.sendKeys("SLIIT SQA ASSIGNMENT");
            System.out.println("Top text entered.");

            js.executeScript("arguments[0].focus();", bottomText);
            bottomText.clear();
            bottomText.sendKeys("AUTOMATION SUCCESSFUL");
            System.out.println("Bottom text entered.");

            // Step 5: Click Download Meme button
            WebElement downloadBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//button[contains(text(),'Download')]")
                    )
            );
            js.executeScript("arguments[0].scrollIntoView(true);", downloadBtn);
            Thread.sleep(500);
            js.executeScript("arguments[0].click();", downloadBtn);
            System.out.println("Download Meme clicked!");

            Thread.sleep(5000);
            System.out.println("========== PASSED: Meme Generator Test ==========");

        } catch (Exception e) {
            System.out.println("Error encountered: " + e.getMessage());
            Assert.fail("Meme text addition failed: " + e.getMessage());
        }
    }
}