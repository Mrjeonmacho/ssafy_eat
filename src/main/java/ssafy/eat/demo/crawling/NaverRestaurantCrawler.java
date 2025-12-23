package ssafy.eat.demo.crawling;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ssafy.eat.demo.domain.restaurant.GoogleRating;
import ssafy.eat.demo.domain.restaurant.KakaoRating;
import ssafy.eat.demo.domain.restaurant.NaverRating;
import ssafy.eat.demo.domain.restaurant.Restaurant;
import ssafy.eat.demo.repository.RestaurantRepository;

import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Profile("crawling") // "crawling" 프로필일 때만 실행
@RequiredArgsConstructor
public class NaverRestaurantCrawler implements CommandLineRunner {

    private final RestaurantRepository restaurantRepository;
    private static final int MAX_COUNT = 50;

    @Override
    public void run(String... args) throws Exception {
        WebDriver driver = null;
        try {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");
            options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36");
            options.addArguments("--disable-blink-features=AutomationControlled");
            options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
            options.setExperimentalOption("useAutomationExtension", false);

            driver = new ChromeDriver(options);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            JavascriptExecutor js = (JavascriptExecutor) driver;

            driver.get("https://map.naver.com/v5/search/역삼 밥집");

            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("searchIframe"));
            
            loadAllPlacesByWheel(driver);
            
            List<WebElement> places = driver.findElements(By.cssSelector("li.UEzoS"));
            int count = places.size();
            
            if (count == 0) {
                System.out.println("크롤링 실패: 맛집 목록을 찾지 못했습니다.");
                return;
            }

            System.out.println("크롤링 대상 개수: " + count);
            String prevName = ""; // 이전 가게 이름 저장 변수

            for (int i = 0; i < Math.min(count, MAX_COUNT); i++) {
                List<WebElement> currentPlaces = driver.findElements(By.cssSelector("li.UEzoS"));
                WebElement targetLi = currentPlaces.get(i);

                js.executeScript("arguments[0].scrollIntoView({block: 'center'});", targetLi);
                
                String previewName = targetLi.findElement(By.cssSelector("span.TYaxT")).getText();
                System.out.println((i + 1) + "번째 | 크롤링 시도: " + previewName);
                
                WebElement clickable = targetLi.findElement(By.cssSelector("a"));
                js.executeScript("arguments[0].click();", clickable);
                Thread.sleep(1000);

                driver.switchTo().defaultContent();
                
                // URL이 변경될 때까지 대기
                try {
                    wait.until(d -> d.getCurrentUrl().contains("/place/"));
                } catch (TimeoutException e) {
                    System.out.println("   > 스킵 (URL 변경 감지 실패)");
                    returnToSearch(driver);
                    continue;
                }
                
                // 상세 페이지 타입에 따라 iframe 전환 또는 현재 페이지에서 작업
                if (driver.findElements(By.id("entryIframe")).isEmpty()) {
                     // Iframe이 없는 경우, 현재 페이지에서 바로 정보 추출
                } else {
                    driver.switchTo().frame("entryIframe");
                }
                
                // 방어적으로 가게명 추출
                List<WebElement> nameEls = driver.findElements(By.cssSelector("span.Fc1rA"));
                if (nameEls.isEmpty()) {
                    System.out.println("   > 스킵 (상세 페이지 구조 다름, 가게명 없음)");
                    returnToSearch(driver);
                    continue;
                }
                String name = nameEls.get(0).getText();

                if (name.isEmpty() || restaurantRepository.findByName(name).isPresent()) {
                    System.out.println("   > 스킵 (이름이 없거나 이미 존재: " + name + ")");
                    returnToSearch(driver);
                    continue;
                }

                String address = safeText(driver, "span.LDgIH");
                String ratingText = safeText(driver, "span.PXMot");
                double naverRating = ratingText.isEmpty() ? 0.0 : Double.parseDouble(ratingText.replace("별점", "").trim());

                double latitude = 0.0, longitude = 0.0;
                String currentUrl = driver.getCurrentUrl();
                Pattern pattern = Pattern.compile("c=([0-9.]+),([0-9.]+)");
                Matcher matcher = pattern.matcher(currentUrl);
                if (matcher.find()) {
                    longitude = Double.parseDouble(matcher.group(1));
                    latitude = Double.parseDouble(matcher.group(2));
                }

                Restaurant restaurant = Restaurant.builder().name(name).address(address).description("").cuisineType("").latitude(latitude).longitude(longitude).naverRating(new NaverRating(naverRating)).kakaoRating(new KakaoRating(0.0)).googleRating(new GoogleRating(0.0)).build();
                restaurantRepository.save(restaurant);
                System.out.println("   > 저장 완료: " + name);

                returnToSearch(driver);
            }

            System.out.println("✅ 크롤링 완료!");

        } catch (Exception e) {
            System.err.println("크롤링 중 오류가 발생했습니다.");
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    private void loadAllPlacesByWheel(WebDriver driver) throws InterruptedException {
        System.out.println("가상 스크롤을 위해 Wheel 이벤트를 발생시킵니다...");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.presenceOfElementLocated(By.id("_pcmap_list_scroll_container")));

        for (int i = 0; i < 12; i++) {
            js.executeScript("const el = document.getElementById('_pcmap_list_scroll_container'); if (el) { el.dispatchEvent(new WheelEvent('wheel', { deltaY: 1000, bubbles: true, cancelable: true })); }");
            Thread.sleep(500); 
        }
        System.out.println("스크롤 완료.");
    }

    private String safeText(WebDriver driver, String selector) {
        try {
            return driver.findElement(By.cssSelector(selector)).getText();
        } catch (Exception e) {
            return "";
        }
    }

    private void returnToSearch(WebDriver driver) {
        try {
            driver.switchTo().defaultContent();
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("searchIframe"));
        } catch (Exception e) {
            System.err.println("searchIframe으로 돌아가는 중 오류 발생, 무시하고 진행");
        }
    }
}