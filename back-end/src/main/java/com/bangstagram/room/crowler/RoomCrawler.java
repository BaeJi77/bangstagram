package com.bangstagram.room.crowler;

import com.bangstagram.room.domain.model.Room;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RoomCrawler {
    private static String url = "https://store.naver.com/attractions/list?page=1&query=%EB%B0%A9%ED%83%88%EC%B6%9C";

    private ChromeDriver getDriver() {

        // WebDriver 경로 설정
        System.setProperty("webdriver.chrome.driver", "back-end/lib/chromedriver.exe");

        // WebDriver 옵션 설정
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");            // 전체화면 실행
        options.addArguments("--disable-popup-blocking");    // 팝업 무시
        options.addArguments("--disable-default-apps");     // 기본앱 사용 안함

        // WebDriver 객체 생성
        ChromeDriver driver = new ChromeDriver( options );

        return driver;
    }

    public List<String> getURLs(ChromeDriver driver) {
        List<String> searchURLs = new ArrayList<>();

        try {
            WebDriverWait wait = new WebDriverWait(driver, 30);
            WebElement parent = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"container\"]/div[2]/div[1]")));

            List<WebElement> elementList = parent.findElements(By.className("name"));

            elementList.forEach(webElement -> {
                String searchURL = webElement.getAttribute("href");
                searchURLs.add(searchURL);
            });
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            return searchURLs;
        }
    }

    public List<Room> getList() {

        ChromeDriver driver = getDriver();

        driver.get(url);

        List<Room> rooms = new ArrayList<>();

        List<String> searchUrls = getURLs(driver);

        for(String searchUrl: searchUrls) {
            driver.get(searchUrl);
            WebElement el = driver.findElementByClassName("list_bizinfo");

            String title = null, phone = null, address = null, link = null, desc = null;
            try {
                title = el.findElement(By.xpath("//*[@id=\"content\"]/div[1]/div[1]/strong")).getText();
                phone = el.findElement(By.cssSelector("#content > div:nth-child(2) > div > div > div.list_item.list_item_biztel")).getText();
                address = el.findElement(By.cssSelector("#content > div:nth-child(2) > div > div > div.list_item.list_item_address > div > ul > li:nth-child(1) > span")).getText();
                link = el.findElement(By.className("biz_url")).getAttribute("href");
                desc = el.findElement(By.cssSelector("#content > div:nth-child(2) > div> div > div.list_item.list_item_desc > div > div > span")).getText();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                rooms.add(Room.builder()
                        .title(title)
                        .phone(phone)
                        .address(address)
                        .link(link)
                        .description(desc)
                        .build());
            }

        }
        return rooms;
    }
}
