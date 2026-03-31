package JCDSeleniumTraining;

import org.jspecify.annotations.NonNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class ReadFromFlatFile {
    WebDriver driver;

    // Launch Chrome
    public void LaunchBrowser() {

        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("https://www.calculator.net/calorie-calculator.html");
    }

    public void fillInFields(@NonNull ArrayList<String> fileValues) throws InterruptedException {
        // @NonNull ArrayList<String> fileValues){
        driver.findElement(By.xpath("//input[@onclick='clearForm(document.calform);']")).click();
        Thread.sleep(500);
        driver.findElement(By.id("cage")).sendKeys(fileValues.get(0));
        Thread.sleep(500);
        if(fileValues.get(1).equals("m")) {
            driver.findElement(By.xpath("//label[@for='csex1']/span[@class='rbmark']")).click();
        } else {
            driver.findElement(By.xpath("//label[@for='csex2']/span[@class='rbmark']")).click();
        }
        Thread.sleep(500);
        driver.findElement(By.id("cheightfeet")).sendKeys(fileValues.get(2));
        Thread.sleep(500);
        driver.findElement(By.id("cheightinch")).sendKeys(fileValues.get(3));
        Thread.sleep(500);
        driver.findElement(By.id("cpound")).sendKeys(fileValues.get(4));
        //driver.findElement(By.id("cactivity")).click();
        Thread.sleep(500);
        //driver.findElement(By.xpath("//option[@value='1.465']")).click();
        WebElement selectElement = driver.findElement(By.name("cactivity"));
        Select select = new Select(selectElement);
        select.selectByValue("1.55");
    }

    public List<String> calculateAndStoreResults()  throws InterruptedException {
        driver.findElement(By.xpath("//td[@width='450']//input[@value='Calculate']")).click();
        Thread.sleep(500);
        List<String> calorieList = new ArrayList<>();
        calorieList.add(driver.findElement(By.xpath("//span[.='100%']/preceding::b")).getText());
        calorieList.add(driver.findElement(By.xpath("//span[.='91%']/preceding::b[1]")).getText());
        calorieList.add(driver.findElement(By.xpath("//span[.='82%']/preceding::b[1]")).getText());
        calorieList.add(driver.findElement(By.xpath("//span[.='65%']/preceding::b[1]")).getText());
        /* List<String> resultsCalories = new ArrayList<>;
         for(int i = 0; i < 3; i++) {
            resultsCalories[i].add(driver.findElement(By.xpath("//table/tbody/tr/td/div/b").[i+1]);
            System.out.println("Calories for value:", resultsCalories[i]);
        } */
        return calorieList;
    }

    public void closebrowser() { driver.quit(); }


    public static void main(String[] args) throws InterruptedException {
        ReadFromFlatFile obj = new ReadFromFlatFile();
        try {
            File file = new File("data/datafile.txt");
            Scanner scanner = new Scanner(file);

            List<String> bodyStats = new ArrayList<>();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                bodyStats.add(line);
            }


//            for(String s : bodyStats) {
//                System.out.println(s);
//            }
            obj.LaunchBrowser();
            obj.fillInFields((ArrayList<String>) bodyStats);
            List<String> calorieResOut = new ArrayList<>();
            calorieResOut = obj.calculateAndStoreResults();
            PrintWriter out = new PrintWriter("data/outfile.txt");
            for(String s : calorieResOut) {
                System.out.println(s);
                out.println(s);
            }
            out.close();
            scanner.close();
        } catch(FileNotFoundException e) {
            System.out.println("File not found!");
            e.printStackTrace();
        }
        obj.closebrowser();
    }
}
