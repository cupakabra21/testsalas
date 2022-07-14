package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TaskLogIn {
	
	WebDriver driver;
	File file;
	FileInputStream fis;
	XSSFWorkbook wb;
	XSSFSheet sheet;
	XSSFRow red;
	XSSFCell celija;
	
	@BeforeClass
	public void beforeClass() throws IOException {
		System.setProperty("webdriver.chrome.driver",
				"driver-lib\\chromedriver.exe");
		this.driver = new ChromeDriver();
		
		file = new File("data/TestPlan.xlsx");
		fis = new FileInputStream(file);
		wb = new XSSFWorkbook(fis);
		
	}
	
	@Test
	public void logInValidCredentials() throws InterruptedException {
		String webSiteUrl = this.citanjeTekstaIzFajla("Log in", 4, 2);
		driver.navigate().to(webSiteUrl);
		driver.manage().window().maximize();
		Thread.sleep(1000);
		String username = this.citanjeTekstaIzFajla("log in", 5, 2);
		String password = this.citanjeTekstaIzFajla("log in", 6, 2);
		this.insertUsername(username);
		this.insertPassword(password);
		this.clickLogIn();
	}
	
	@Test
	public void lognWithoutCredentials() throws InterruptedException {
		String webSiteUrl = this.citanjeTekstaIzFajla("Log in", 4, 2);
		driver.navigate().to(webSiteUrl);
		driver.manage().window().maximize();
		Thread.sleep(1000);
		String username = "";
		String password = "";
		this.insertUsername(username);
		this.insertPassword(password);
		this.clickLogIn();
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
	
	public void insertUsername(String username) throws InterruptedException {
		driver.findElement(By.xpath("//*[@id=\"user-name\"]")).sendKeys(username);
		Thread.sleep(1000);
	}

	public void insertPassword(String password) throws InterruptedException {
		driver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys(password);
		Thread.sleep(2000);
	}

	public void clickLogIn() throws InterruptedException {
		driver.findElement(By.xpath("//*[@id=\"login-button\"]")).click();
		Thread.sleep(2000);
	}
	
	@AfterClass
	public void nakonSvega() {
		driver.close();
	}

}
