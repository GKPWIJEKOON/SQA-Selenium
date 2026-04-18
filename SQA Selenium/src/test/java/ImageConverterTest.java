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
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Duration;

public class ImageConverterTest {

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

    @DataProvider(name = "converterData")
    public Object[][] converterData() {
        String projectPath = System.getProperty("user.dir");
        return new Object[][] {
                {
                        "https://www.pixelssuite.com/convert-to-png",
                        projectPath + "\\src\\test\\resources\\image01.jpg",
                        "Convert to PNG"
                },
                {
                        "https://www.pixelssuite.com/convert-to-jpg",
                        projectPath + "\\src\\test\\resources\\image02.png",
                        "Convert to JPG"
                },
                {
                        "https://www.pixelssuite.com/convert-to-webp",
                        projectPath + "\\src\\test\\resources\\image03.WEBP",
                        "Convert to WEBP"
                }
        };
    }

    @Test(dataProvider = "converterData")
    public void testImageConverter(String url, String filePath, String testName) throws InterruptedException {
        System.out.println("\n========== STARTING: " + testName + " ==========");
        System.out.println("URL      : " + url);
        System.out.println("File     : " + filePath);

        try {
            // Step 1: Open the converter page
            driver.get(url);

            // Step 2: Unhide the file input and send the file
            WebElement fileInput = driver.findElement(By.cssSelector("input[type='file']"));
            js.executeScript(
                    "arguments[0].style.display='block';" +
                            "arguments[0].style.visibility='visible';" +
                            "arguments[0].style.opacity='1';",
                    fileInput
            );
            fileInput.sendKeys(filePath);
            System.out.println("File sent: " + filePath);

            // Step 3: Wait for and click the Download button
            WebElement downloadButton = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//button[contains(text(),'Download')] | //a[contains(text(),'Download')]")
                    )
            );
            System.out.println("Download button is visible!");
            downloadButton.click();
            System.out.println("Download button clicked!");

            // Step 4: Wait for download to trigger
            Thread.sleep(3000);

            System.out.println("========== PASSED: " + testName + " ==========");

        } catch (Exception e) {
            System.out.println("========== FAILED: " + testName + " ==========");
            System.out.println("Reason: " + e.getMessage());
            Assert.fail("Test failed for [" + testName + "]: " + e.getMessage());
        }
    }
}