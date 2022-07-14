package test;


import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class Task1 {
	WebDriver driver;
	File file;
	FileInputStream fis;
	XSSFWorkbook wb;
	XSSFSheet sheet;
	XSSFRow red;
	XSSFCell celija;
	
	
	@BeforeClass
	public void beforeClass() throws InterruptedException,  IOException {
		System.setProperty("webdriver.chrome.driver",
				"driver-lib\\chromedriver.exe");
		this.driver = new ChromeDriver();
		
		file = new File("data/TestPlan.xlsx");
		fis = new FileInputStream(file);
		wb = new XSSFWorkbook(fis);
		
	}
	
	@BeforeMethod
	public void beforeTests() throws InterruptedException, IOException {
		sheet = wb.getSheet("log in");
		red = sheet.getRow(3);
		celija = red.getCell(1);
		//String webSiteUrl = celija.getStringCellValue();
		String webSiteUrl = this.citanjeTekstaIzFajla("log in", 4, 2);
		driver.navigate().to(webSiteUrl);
		driver.manage().window().maximize();
		Thread.sleep(1000);
		
		red = sheet.getRow(4);
		celija = red.getCell(1);
		
		String username = celija.getStringCellValue();
		
		red = sheet.getRow(5);
		celija = red.getCell(1);
		String password = celija.getStringCellValue();
		
		driver.findElement(By.xpath("//*[@id=\"user-name\"]")).sendKeys(username);
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys(password);
		Thread.sleep(2000);
		driver.findElement(By.xpath("//*[@id=\"login-button\"]")).click();
	}
	
	
	@Test
	public void addProductToCart() throws InterruptedException {
		sheet = wb.getSheet("add product to cart ");
		red = sheet.getRow(5);
		celija = red.getCell(1);
		String expectedProductName = celija.getStringCellValue();
		red = sheet.getRow(7 );
		celija = red.getCell(1);
		int expectedProductQuantity = (int) celija.getNumericCellValue();
		String expectedProductQuantityText = Integer.toString(expectedProductQuantity);
		//String expectedProductName = this.citanjeTekstaIzFajla("add product to cartTest", 6, 2);
		//int expectedProductQuantity = this.citanjeBrojevaIzFajla("add product to cartTest", 8, 2);
		
		
		
		this.addBackPackToCart();
		this.clickOnCartPicture();
		
		
		
		String textForAssert = driver.findElement(By.xpath("//*[@id=\"item_4_title_link\"]/div")).getText();
		String quantityForAssert = driver.findElement(By.xpath("//*[@id=\"cart_contents_container\"]/div/div[1]/div[3]/div[1]")).getText();
		System.out.println("Vrednost stringa sa UI je " + textForAssert);
		
		assertEquals(textForAssert, expectedProductName);
		assertEquals(quantityForAssert, expectedProductQuantity);
	}
	
	@Test
	public void addTwoProductsToCart() throws InterruptedException {
		Thread.sleep(1000);
		
		this.addBackPackToCart();
	
		driver.findElement(By.xpath("//*[@id=\"add-to-cart-sauce-labs-bike-light\"]")).click();
		Thread.sleep(2000);
		this.clickOnCartPicture();
		
		String textForAssertFirstProduct = driver.findElement(By.xpath("//*[@id=\"item_4_title_link\"]/div")).getText();
		String quantityForAssert = driver.findElement(By.xpath("//*[@id=\"cart_contents_container\"]/div/div[1]/div[3]/div[1]")).getText();
		String textForAssertSecondProduct = driver.findElement(By.xpath("//*[@id=\"item_0_title_link\"]/div")).getText();
		
		assertEquals(textForAssertFirstProduct, "Sauce Labs Backpack");
		assertEquals(textForAssertSecondProduct, "Sauce Labs Bike Light");
		assertEquals(quantityForAssert, "1");
	}
	
	@Test
	public void creatingAnOrderForOneProduct() throws InterruptedException {
		this.addProductToCart();
		driver.findElement(By.xpath("//*[@id=\"checkout\"]")).click();
		this.popunjavanjeLicnihPodataka("Pera", "Peric", "21000");
		driver.findElement(By.xpath("//*[@id=\"continue\"]")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"finish\"]")).click();
		Thread.sleep(1000);
		
		String expectedText = "THANK YOU FOR YOUR ORDER";
		String actualText = driver.findElement(By.xpath("//*[@id=\"checkout_complete_container\"]/h2")).getText();
		assertEquals(actualText, expectedText);
		Thread.sleep(1000);
		
		this.proveraNastalePorudzbine();
		
	}
	
	@Test
	public void creatingAnOrderForTwoProducts() throws InterruptedException {
		this.addTwoProductsToCart();
		driver.findElement(By.xpath("//*[@id=\"checkout\"]")).click();
		this.popunjavanjeLicnihPodataka("Pera", "Peric", "21000");
		driver.findElement(By.xpath("//*[@id=\"continue\"]")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"finish\"]")).click();
		Thread.sleep(1000);
		this.proveraNastalePorudzbine();
	}
	
	
	public void addBackPackToCart() throws InterruptedException {
		Thread.sleep(1000);
		
		driver.findElement(By.xpath("//*[@id=\"add-to-cart-sauce-labs-backpack\"]")).click();
		Thread.sleep(2000);
	}
	
	public void clickOnCartPicture() throws InterruptedException {
		driver.findElement(By.xpath("//*[@id=\"shopping_cart_container\"]/a")).click();
		Thread.sleep(2000);
	}
	
	public void popunjavanjeLicnihPodataka(String ime, String prezime, String zipCode) throws InterruptedException {
		driver.findElement(By.xpath("//*[@id=\"first-name\"]")).sendKeys(ime);
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"last-name\"]")).sendKeys(prezime);
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"postal-code\"]")).sendKeys(zipCode);
		Thread.sleep(1000);
	}
	
	public void proveraNastalePorudzbine() throws InterruptedException {
		String expectedText = "THANK YOU FOR YOUR ORDER";
		String actualText = driver.findElement(By.xpath("//*[@id=\"checkout_complete_container\"]/h2")).getText();
		assertEquals(actualText, expectedText);
		Thread.sleep(1000);
	}
	
	@AfterMethod
	public void nakonTesta() throws InterruptedException {
		driver.findElement(By.xpath("//*[@id=\"react-burger-menu-btn\"]")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//*[@id=\"reset_sidebar_link\"]")).click();
		Thread.sleep(2000);
	}
	
	@AfterClass
	public void nakonSvega() {
		driver.close();
	}
	
	public String citanjeTekstaIzFajla(String imeSheeta, int brojReda, int brojKolone) {
		sheet = wb.getSheet(imeSheeta);
		red = sheet.getRow(brojReda - 1);
		celija = red.getCell(brojKolone - 1);
		
		return celija.getStringCellValue();
	}
	
	public int citanjeBrojevaIzFajla(String imeSheeta, int brojReda, int brojKolone) {
		sheet = wb.getSheet(imeSheeta);
		red = sheet.getRow(brojReda - 1);
		celija = red.getCell(brojKolone - 1);
		
		return (int) celija.getNumericCellValue();
	}
	
}
