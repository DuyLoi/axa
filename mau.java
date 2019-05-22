package com.tpb.botaml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.ProfilesIni;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;


@SuppressWarnings("unused")
public class TestFinal {
	private static String url;
	private static String user;
	private static String userid;
	private static String password;
	private static String passwordid;

	private static String submit;
	private static String click;
	private static String xpath;

	private static String keyword;
	private static String typeGuest;
	private static String status;

	private static WebDriver driver = null;
	
	public static FileWriteLog filelog;
	private static String folderpdfexport;

	@SuppressWarnings({ "unchecked" })
	public static void main(String[] args) throws InterruptedException {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String folderpdfexport = df.format(date);
	       JSONParser jsonParser = new JSONParser();
	        try (FileReader reader = new FileReader("config.json"))
	        {
	        
	        	Logger logger=Logger.getLogger("TestFinal");
	        	PropertyConfigurator.configure("./res/log4j.properties");
  	
	            //Read JSON file
	            Object obj = jsonParser.parse(reader);
	            JSONArray BotAMLList = (JSONArray) obj;

	        	   //========================================================== LOGIN ========================================================
			BotAMLList.forEach(emp -> parseBotAML((JSONObject) emp, "login"));
			
			System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
			driver = new ChromeDriver();
			driver.manage().window().maximize();
			WebDriverWait wait = new WebDriverWait(driver, 30);
			
			AutoLogin login = new AutoLogin(driver);
			login.Start(url, user, By.id("Username"), password,
					By.id("Password"), By.id("SignIn"));
			
			if (login.waitForElementPresent(driver,By.xpath("//div[@id=\"dijit__WidgetsInTemplateMixin_2\"]"),30) == null) {
				System.out.println("Log in thất bại");
				//logger.error("Log in thất bại");
				login.Start(url, user, By.id("Username"), password,By.id("Password"), By.id("SignIn"));
				
				//logger.info("Tim kiem the login");
			} else {
				FileWriteLog.createLogFileAML("Log in","","Login thành công");
				//System.out.println("Login thành công");
				 //logger.info("Login thành công");
				 
				//================================================ END LOGIN ==========================================================
				//================================================= find iframe =======================================================
				new WebDriverWait(driver, 30);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("accelus_components_application_IframeView_0")));
				driver.switchTo().frame(driver.findElement(By.xpath("//*[@id=\"accelus_components_application_IframeView_0\"]/iframe")));
				//================================================= end find iframe =======================================================
				//read file data
				File f = new File("inputdata.txt");
				FileReader fr = new FileReader(f);
				BufferedReader br = new BufferedReader(fr);
				String line;
				while ((line = br.readLine()) != null) {
					String str_General_information = line.trim();
					String[] information = str_General_information.split("\\|");
					String str_Guest_Name = information[0].trim();
					String str_Type_Guest_Name = information[1].trim();
					keyword = str_Guest_Name;
					typeGuest = str_Type_Guest_Name;
					System.out.println("Tên khách hàng :" + str_Guest_Name);
					System.out.println("Loại khách hàng:" + str_Type_Guest_Name);
		       		//============================================ SelectT Entity Type=====================================================
					//Select individual//Select organisation//Select vessel//Select unspecified
					switch (typeGuest) {
					case "individual":
						BotAMLList.forEach(emp -> parseBotAML((JSONObject) emp, "individual"));
						
						WebElement individual = driver.findElement(By.xpath(xpath));
						JavascriptExecutor executor = (JavascriptExecutor) driver;
						executor.executeScript("arguments[0].click();",individual);
						break;
					case "organisation":
						BotAMLList.forEach(emp -> parseBotAML((JSONObject) emp, "organisation"));
						
						WebElement organisation = driver.findElement(By.xpath(xpath));
						JavascriptExecutor executor1 = (JavascriptExecutor) driver;
						executor1.executeScript("arguments[0].click();",organisation);
						break;
					case "vessel":
						BotAMLList.forEach(emp -> parseBotAML((JSONObject) emp, "vessel"));
						
						WebElement vessel = driver.findElement(By.xpath(xpath));
						JavascriptExecutor executor2 = (JavascriptExecutor) driver;
						executor2.executeScript("arguments[0].click();", vessel);
						break;

					case "unspecified":
						BotAMLList.forEach(emp -> parseBotAML((JSONObject) emp, "unspecified"));
						WebElement unspecified = driver.findElement(By.xpath("xpath"));
						JavascriptExecutor executor3 = (JavascriptExecutor) driver;
						executor3.executeScript("arguments[0].click();",unspecified);
						break;

					}
					//======================================== End SelectT Entity Type=====================================================
					//=============================================== SEARCH KEY WORD =====================================================
					new WebDriverWait(driver, 30);
					wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[starts-with(@id, 'indium_view_form_ValidationTextBox')]")));
					new WebDriverWait(driver, 30);
					WebElement name = driver.findElement(By.xpath("//input[starts-with(@id, 'indium_view_form_ValidationTextBox')]"));
					name.sendKeys(keyword);
					name.sendKeys(Keys.ENTER);
					//=============================================== END SEARCH KEY WORD =================================================
					
					//================================ Export ShowButton / Case Report / Export ===========================================
					// Export ShowButton
					BotAMLList.forEach(emp -> parseBotAML((JSONObject) emp, "exportshow"));
					new WebDriverWait(driver, 30);
					wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(xpath)));
					WebElement exportShowButton =  driver.findElement(By.cssSelector(xpath));
					exportShowButton.click();
					// Case Report
					BotAMLList.forEach(emp -> parseBotAML((JSONObject) emp, "pdfCaseReport"));
					WebElement pdfCaseReport = driver.findElement(By.cssSelector(xpath));
					pdfCaseReport.click();
					// Export
					BotAMLList.forEach(emp -> parseBotAML((JSONObject) emp, "export"));
					new WebDriverWait(driver, 30);
					WebElement export = driver.findElement(By.xpath(xpath));
					export.click();

					
					new WebDriverWait(driver, 30);
					FileWriteLog.createLogFileAML(keyword,typeGuest,status);
					//================================ END Export ShowButton / Case Report / Export =======================================
					
					//===================================find frame + Back to Screening===================================
					//find frame
					Thread.sleep(2000);
					driver.get("https://app.accelus.com/#accelus/fsp/%7B%22location%22%3A%22%3Flocale%3Den-GB%23fsp%2Fcase%2F0a3687c5-6aa5-1d04-9ab0-ec39000d18b4%2Fview%2Fworldcheck%2F%257B%2522from%2522%253A%2522screening%2522%257D%22%7D");
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("accelus_components_application_IframeView_0")));
					driver.switchTo().frame(driver.findElement(By.xpath("//*[@id=\"accelus_components_application_IframeView_0\"]/iframe")));
					//Back to Screening click
					WebElement Screening = driver.findElement(By.linkText("< Back to Screening"));
					JavascriptExecutor executor = (JavascriptExecutor) driver;
					executor.executeScript("arguments[0].click();", Screening);
					/////////////
					 Date date1 = new Date();
				        DateFormat df1 = new SimpleDateFormat("yyyyMMdd_HHmmss");
				        String dateString = df1.format(date1);
				        
					Thread.sleep(1000);
			        File srcFile = new File("C:/Users/thht-dev04/Downloads/CaseDossierReport.pdf");
			        
			        String filec= "D:/LoiPD/pdf/"+folderpdfexport+"/"+dateString+"_"+typeGuest+"_"+keyword+".pdf";

			        System.out.println(filec);
			        // File đích (Destination file).
			        File destFile = new File(filec);
			 
			        if (srcFile.exists()) {
			        	// Tạo thư mục cha của file đích.
			            destFile.getParentFile().mkdirs();
			     
			            boolean renamed = srcFile.renameTo(destFile);
			     
			            System.out.println("Renamed: " + renamed);
			        }																			
					//===================================end find frame + Back to Screening===================================
				}//end while

				//fr.close();
				//br.close();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	        }
	
//=========================================================

	 private static void parseBotAML(JSONObject config,String step){
		 JSONObject BotAML = (JSONObject) config.get(step);
		 try {
			switch (step) {
			//========================================= LOGIN
			case "login":
				url = (String) BotAML.get("url");
				System.out.println(url);

				JSONObject userconfig = (JSONObject) BotAML.get("user");
				userid = (String) userconfig.get("id");
				System.out.println(userid);
				user = (String) userconfig.get("value");
				System.out.println(user);
				
				JSONObject passwordconfig = (JSONObject) BotAML.get("password");
				passwordid = (String) passwordconfig.get("id");
				System.out.println(passwordid);	
				password = (String) passwordconfig.get("value");
				System.out.println(password);
				
				JSONObject actionconfig = (JSONObject) BotAML.get("action");
				click  = (String) actionconfig.get("id");
				System.out.println(click);
				break;
				//==================================== END LOGIN
			case "individual":
				xpath = (String) BotAML.get("xpath");
				System.out.println(xpath);
				break;
				//====================================
			case "organisation":
				xpath = (String) BotAML.get("xpath");
				System.out.println(xpath);
				break;
				//====================================
			case "vessel":
				xpath = (String) BotAML.get("xpath");
				System.out.println(xpath);
				break;
				//====================================
			case "unspecified":
				xpath = (String) BotAML.get("xpath");
				System.out.println(xpath);
				break;
				//====================================
			case "filldata":
				xpath = (String) BotAML.get("xpath");
				System.out.println(xpath);
				break;
				//====================================
			case "exportshow":
				xpath = (String) BotAML.get("xpath");
				System.out.println(xpath);
				break;
				//====================================
			case "pdfCaseReport":
				xpath = (String) BotAML.get("xpath");
				System.out.println(xpath);
				break;
				//====================================
			case "export":
				xpath = (String) BotAML.get("xpath");
				System.out.println(xpath);
				break;
			default:
				break;
			}
			 
		} catch (Exception e) {}
	 }
}
