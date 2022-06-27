package ru.netology.aqa0selenide;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;

public class AppCardDeliveryTestTask1 {

    private static final long ADD_DAYS = 1505;
    private static final String SELECT_CITY = "Рязань";

    private String formatCurrentDate(long addDays) {
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }



    @BeforeEach
    public void setupTest() {
        open("http://localhost:9999");
    }

    @Test
    public void shouldBeSuccessfullyCompleted() {
        $x("//*[@data-test-id='city']//input").setValue(SELECT_CITY);
        String currentDate = formatCurrentDate(ADD_DAYS);
        $x("//*[@data-test-id='date']//input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $x("//*[@data-test-id='date']//input").sendKeys(currentDate);
        $x("//*[@data-test-id='name']//input").setValue("Иванов-Иваныч Иван");
        $x("//*[@data-test-id='phone']//input").setValue("+78005553535");
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[text()='Забронировать']/../..").click();
        $x("//*[@data-test-id='notification'][contains(@class, 'notification_visible')]//*[contains(@class, 'notification__content')]")
                .shouldBe(Condition.visible, Duration.ofSeconds(20))
                .shouldHave(Condition.exactText(String.format("Встреча успешно забронирована на %s", currentDate)));
    }
}
