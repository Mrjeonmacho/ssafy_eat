package ssafy.eat.demo.crawling;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
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

            String originalWindow = driver.getWindowHandle();

            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("searchIframe"));

            loadAllPlacesByWheel(driver);

            List<WebElement> places = driver.findElements(By.cssSelector("li.UEzoS"));
            int count = places.size();

            if (count == 0) {
                System.out.println("크롤링 실패: 맛집 목록을 찾지 못했습니다.");
                return;
            }

            System.out.println("크롤링 대상 개수: " + count);

            for (int i = 0; i < Math.min(count, MAX_COUNT); i++) {
                // StaleElementReferenceException을 피하기 위해 매번 목록을 다시 찾음
                List<WebElement> currentPlaces = driver.findElements(By.cssSelector("li.UEzoS"));
                if (i >= currentPlaces.size()) {
                    System.out.println("목록의 끝에 도달하여 크롤링을 중단합니다.");
                    break;
                }
                WebElement targetLi = currentPlaces.get(i);

                js.executeScript("arguments[0].scrollIntoView({block: 'center'});", targetLi);
                Thread.sleep(300);

                String previewName = targetLi.findElement(By.cssSelector("span.TYaxT")).getText();
                System.out.println((i + 1) + "번째 | 크롤링 시도: " + previewName);

                // 1. 목록 아이템 클릭
                js.executeScript("arguments[0].click();", targetLi);

                try {
                    // 2. entryIframe 리셋 및 로드 대기 (SPA의 비동기 동작에 대응)
                    driver.switchTo().defaultContent();
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("entryIframe")));
                    wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("entryIframe"));

                    // 3. Iframe URL에서 placeId 추출
                    String entryUrl = driver.getCurrentUrl();
                    Matcher matcher = Pattern.compile("/place/(\\d+)").matcher(entryUrl);
                    if (!matcher.find()) {
                        System.out.println("   > 스킵 (entryIframe URL에서 placeId 없음)");
                        continue; // finally 블록으로 가서 복귀 로직 수행
                    }
                    String placeId = matcher.group(1);

                    // 4. 데이터 추출
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("h1")));
                    String name = driver.findElement(By.cssSelector("h1")).getText();

                    boolean shouldSkip = false;
                    if (name.isEmpty() || restaurantRepository.findByName(name).isPresent()) {
                        System.out.println("   > 스킵 (이름이 없거나 이미 존재: " + name + ")");
                        shouldSkip = true;
                    }

                    if (!shouldSkip) {
                        String address = safeText(driver, "span.LDgIH");
                        double naverRating = 0.0; // 별점은 다음 단계에서 추출
                        double latitude = 0.0, longitude = 0.0; // 좌표는 다음 단계에서 추출

                        Restaurant restaurant = Restaurant.builder()
                                .name(name)
                                .address(address)
                                .description("")
                                .cuisineType("")
                                .latitude(latitude)
                                .longitude(longitude)
                                .naverRating(new NaverRating(naverRating))
                                .kakaoRating(new KakaoRating(0.0))
                                .googleRating(new GoogleRating(0.0))
                                .build();
                        restaurantRepository.save(restaurant);
                        System.out.println("   > 저장 완료: " + name);
                    }
                } catch (TimeoutException e) {
                    System.out.println("   > 스킵 (entryIframe 로드 시간 초과)");
                } finally {
                    // 5. 원래의 searchIframe으로 복귀
                    driver.switchTo().defaultContent();
                    driver.switchTo().frame("searchIframe");
                }
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
}