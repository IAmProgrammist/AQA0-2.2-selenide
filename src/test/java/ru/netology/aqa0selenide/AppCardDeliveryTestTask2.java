package ru.netology.aqa0selenide;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

public class AppCardDeliveryTestTask2 {

    private static final long ADD_DAYS = 4;
    private static final String SELECT_CITY = "Брянск";

    @BeforeEach
    public void setupTest() {
        open("http://localhost:9999");
    }

    @Test
    public void shouldBeSuccessfullyCompleted() {
        selectCity(SELECT_CITY);
        String currentDate = selectAndReturnFormattedDate(ADD_DAYS);
        $x("//*[@data-test-id='date']//input").setValue(currentDate);
        $x("//*[@data-test-id='name']//input").setValue("Иванов-Иваныч Иван");
        $x("//*[@data-test-id='phone']//input").setValue("+78005553535");
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[text()='Забронировать']/../..").click();
        $x("//*[@data-test-id='notification'][contains(@class, 'notification_visible')]//*[contains(@class, 'notification__content')]")
                .shouldBe(Condition.visible, Duration.ofSeconds(20))
                .shouldHave(Condition.exactText(String.format("Встреча успешно забронирована на %s", currentDate)));
    }

    private String selectAndReturnFormattedDate(long addDays) {
        LocalDate localDate = LocalDate.now();
        LocalDate requiredDate = localDate.plusDays(addDays);
        int yearDelta = requiredDate.getYear() - localDate.getYear();
        int monthDelta = requiredDate.getMonthValue() - localDate.getMonthValue();
        $x("//*[@data-test-id='date']//*[contains (@class, 'input__icon')]").click();
        while (yearDelta != 0){
            if(yearDelta > 0){
                $x("//*[contains (@class, 'popup')]//*[@data-step='12']").click();
                yearDelta--;
            }
            if(yearDelta < 0){
                $x("//*[contains (@class, 'popup')]//*[@data-step='-12']").click();
                yearDelta++;
            }
        }

        while (monthDelta != 0){
            if (monthDelta > 0){
                $x("//*[contains (@class, 'popup_visible')]//*[@data-step='1']").click();
                monthDelta--;
            }
            if(monthDelta < 0) {
                $x("//*[contains (@class, 'popup_visible')]//*[@data-step='-1']").click();
                monthDelta++;
            }
        }
        $x(String.format("//*[contains (@class, 'popup_visible')]//*[contains (@class, 'calendar__layout')]//*[text()='%d']",
                requiredDate.getDayOfMonth())).click();
        return requiredDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    private void selectCity(String selectCity) {
        $x("//*[@data-test-id='city']//input").sendKeys(selectCity.substring(0, 2));
        $x(String.format("//*[contains(@class, 'popup_visible')]//*[text()='%s']/..", selectCity)).click();
    }

    @Test
    public void shouldBeSuccessfullyCompletedCSS() {
        selectCityCSS(SELECT_CITY);
        String currentDate = selectAndReturnFormattedDateCSS(ADD_DAYS);
        $("[data-test-id='date'] input").setValue(currentDate);
        $("[data-test-id='name'] input").setValue("Иванов-Иваныч Иван");
        $("[data-test-id='phone'] input").setValue("+78005553535");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $("[data-test-id='notification'].notification_visible .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(20))
                .shouldHave(Condition.exactText(String.format("Встреча успешно забронирована на %s", currentDate)));
    }

    private String selectAndReturnFormattedDateCSS(long addDays) {
        LocalDate localDate = LocalDate.now();
        LocalDate requiredDate = localDate.plusDays(addDays);
        int yearDelta = requiredDate.getYear() - localDate.getYear();
        int monthDelta = requiredDate.getMonthValue() - localDate.getMonthValue();
        $("[data-test-id='date'] button").click();
        while (yearDelta != 0){
            if(yearDelta > 0){
                $(".popup [data-step='12']").click();
                yearDelta--;
            }
            if(yearDelta < 0){
                $(".popup [data-step='-12']").click();
                yearDelta++;
            }
        }

        while (monthDelta != 0){
            if (monthDelta > 0){
                $(".popup [data-step='1']").click();
                monthDelta--;
            }
            if(monthDelta < 0) {
                $(".popup [data-step='-1']").click();
                monthDelta++;
            }
        }

        $$(".popup_visible .calendar__layout td")
                .findBy(text(String.valueOf(requiredDate.getDayOfMonth()))).click();
        return requiredDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    private void selectCityCSS(String selectCity) {
        $("[data-test-id='city'] input").sendKeys(selectCity.substring(0, 2));
        $$(".popup_visible .menu-item").findBy(text(selectCity)).click();
    }
}
