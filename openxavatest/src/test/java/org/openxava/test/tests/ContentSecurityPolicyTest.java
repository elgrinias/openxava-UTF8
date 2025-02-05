package org.openxava.test.tests;

import org.openqa.selenium.*;

/**
 * To test CSP related issues with Selenium.
 *
 * @author Javier Paniza
 */

public class ContentSecurityPolicyTest extends WebDriverTestBase {
	
	public void testContentSecurityPolicyExceptions() throws Exception {
		WebDriver driver = createWebDriver();
		driver.get("http://localhost:8080/openxavatest/m/SellerJSP?detail=1"); 
		wait(driver);
		
		waitImageLoaded(driver);
		WebElement img = driver.findElement(By.id("oximage"));
		assertEquals(new Dimension(500, 290), img.getSize()); // So, the original image is displayed
		
		WebElement greeting = driver.findElement(By.id("greeting"));
		assertEquals("Hello from openxava.org", greeting.getText());
		
		WebElement hidden = driver.findElement(By.id("thehidden"));
		assertFalse(hidden.isDisplayed());
		
		WebElement iframe = driver.findElement(By.id("bye"));
		driver.switchTo().frame(iframe);
		WebElement frameBody = driver.findElement(By.tagName("body"));
		assertEquals("Bye from openxava.org", frameBody.getText());
				
		driver.quit();
	}
	
	private void waitImageLoaded(WebDriver driver) throws Exception {
		Thread.sleep(500); 
		WebElement img = driver.findElement(By.id("oximage"));
		Dimension zero = new Dimension(0, 0);
		for (int c=0; c<10 && img.getSize().equals(zero); c++) {
			Thread.sleep(500);
		}
		
	}

}
