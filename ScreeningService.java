package com.tpb.botaml;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.tpb.botaml.util.BotUtil;

public class ScreeningService {

	public static final Logger logger = Logger
			.getLogger(ScreeningService.class);

	private static WebDriver driver = null;

	public static void main(String[] args) {
		doProcess("INDIVIDUAL");
	}

	public static void doProcess(String flowId) {
		
		if (driver == null) {
			System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
			driver = new ChromeDriver();
			driver.manage().window().maximize();
		}

		// Process
		JSONObject flow = BotUtil.getFlowById(flowId);

		// Check require login
		String requiredLogin = BotUtil.getValue(flow, "requiredLogin");

		if ("YES".equals(requiredLogin)) {
			if (!checkAlreadyLogin()) {
				doLogin();
			}
		}
		//
		JSONObject jsonObject = null;
		String id = null;
		String value = null;
		String findBy = null;
		String action = null;
		WebElement webElement = null;
		String waitElement =null;
		String waitElementID = null;
		String waitElementFrame =null;
		WebDriverWait wait = null;

		// Start check AML
		int noOfSteps = Integer.parseInt(BotUtil.getValue(flow, "noOfSteps"));
		JSONObject step = null;
		for (int i = 1; i <= noOfSteps; i++) {

			// Loop each step
			step = BotUtil.getStep(flow, String.valueOf(i));
			
			JSONArray arr = BotUtil.getStepElements(step);
		

			Iterator<Object> iterator = arr.iterator();

		//WebDriverWait wait = new WebDriverWait(driver, 30);

			for (int j = 1; j <= arr.size(); j++) {
				jsonObject = BotUtil.getStepElementByOrder(step,
						String.valueOf(j));
				id = (String) jsonObject.get("id");
				value = (String) jsonObject.get("value");
				findBy = (String) jsonObject.get("findBy");
				waitElement = (String) jsonObject.get("waitElement");
				waitElementID = (String) jsonObject.get("waitElementID");
				waitElementFrame = (String) jsonObject.get("waitElementFrame");
				action = (String) jsonObject.get("action");
				
	
				if ("ID".equals(findBy)) {
					webElement = driver.findElement(By.id(id));
				}
				if ("SENDTEXT".equals(action)) {
					webElement.sendKeys("Bill gate");
					new WebDriverWait(driver, 30);
				}
				if ("CLICK".equals(action)) {
					webElement.click();
					new WebDriverWait(driver, 30);
					if ("YES".equals(waitElement)) {
						wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(waitElementID)));
						driver.switchTo().frame(driver.findElement(By.xpath(waitElementFrame)));
				}
			}
		}
	}
	}

	public static boolean checkAlreadyLogin() {
		return false;
	}

	public static void doLogin() {

		JSONObject flowLogin = BotUtil.getFlowById("LOGIN");
		String url = (String) flowLogin.get("mainUrl");
		JSONObject stepLogin = BotUtil.getStep(flowLogin, "1");
		JSONArray arr = BotUtil.getStepElements(stepLogin);
		
		driver.get(url);

		Iterator<Object> iterator = arr.iterator();

		JSONObject jsonObject = null;
		String id = null;
		String value = null;
		String findBy = null;
		String action = null;
		String waitElement =null;
		WebElement webElement = null;
		String waitElementID = null;
		String waitElementFrame =null;
		//WebDriverWait wait = new WebDriverWait(driver, 30);
		WebDriverWait wait = null;

		for (int i = 1; i <= arr.size(); i++) {
			jsonObject = BotUtil.getStepElementByOrder(stepLogin,
					String.valueOf(i));
			id = (String) jsonObject.get("id");
			value = (String) jsonObject.get("value");
			findBy = (String) jsonObject.get("findBy");
			waitElement = (String) jsonObject.get("waitElement");
			waitElementID = (String) jsonObject.get("waitElementID");
			waitElementFrame = (String) jsonObject.get("waitElementFrame");
			action = (String) jsonObject.get("action");
			

			if ("ID".equals(findBy)) {
				webElement = driver.findElement(By.id(id));
			}
			if ("SENDTEXT".equals(action)) {
				webElement.sendKeys(value);
				new WebDriverWait(driver, 30);
			}
			if ("CLICK".equals(action)) {
				webElement.click();
				new WebDriverWait(driver, 30);
				if ("YES".equals(waitElement)) {
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(waitElementID)));
					driver.switchTo().frame(driver.findElement(By.xpath(waitElementFrame)));
			}
		}

		// WebElement uname = driver.findElement(By.id("Username"));
		// uname.sendKeys(user);
		// new WebDriverWait(driver, 30);
		// WebElement pass = driver.findElement(By.id("Password"));
		// pass.sendKeys(password);
		// new WebDriverWait(driver, 30);
		// driver.findElement(By.id("SignIn")).click();

		}
	/*
	public static void processStep(JSONObject step){
		
		String id = null;
		String value = null;
		String findBy = null;
		String action = null;
		WebElement webElement = null;
		for (int i = 1; i <= arr.size(); i++) {
			JSONObject jsonObject = BotUtil.getStepElementByOrder(step,
					String.valueOf(i));
			
			id = (String) jsonObject.get("id");
			value = (String) jsonObject.get("value");
			findBy = (String) jsonObject.get("findBy");
			action = (String) jsonObject.get("action");
	
			if ("ID".equals(findBy)) {
				webElement = driver.findElement(By.xpath(id));
			}
			if ("SENDTEXT".equals(action)) {
				webElement.sendKeys("Bill GATE");
				new WebDriverWait(driver, 30);
			}
			if ("CLICK".equals(action)) {
				webElement.click();
				new WebDriverWait(driver, 30);
			}

		}
	
*/

	}
}
