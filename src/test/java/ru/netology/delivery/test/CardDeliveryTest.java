package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.delivery.data.DataGenerator.generateDate;

class DeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");

        var firstMeetingDate = DataGenerator.generateDate(4);
        var secondMeetingDate = DataGenerator.generateDate(7);

        SelenideElement formElement = $("form");
        formElement.$("[data-test-id=city] input").setValue(validUser.getCity());
        formElement.$("[data-test-id=date] input").press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        formElement.$("[data-test-id=date] input").setValue(firstMeetingDate);
        formElement.$("[data-test-id=name] input").setValue(validUser.getName());
        formElement.$("[data-test-id=phone] input").setValue(validUser.getPhone());
        formElement.$("label").click();
        $(withText("Запланировать")).click();
        $(Selectors.withText("Успешно")).should(Condition.visible, Duration.ofSeconds(15));
        $("[class='notification__content']")
                .shouldHave(exactText("Встреча успешно запланирована на " + firstMeetingDate))
                .shouldBe(Condition.visible);
        formElement.$("[data-test-id=date] input").press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        formElement.$("[data-test-id=date] input").setValue(secondMeetingDate);
        $(withText("Запланировать")).click();
        $("[data-test-id='replan-notification'] .notification__content")
                .shouldHave(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?"))
                .shouldBe(Condition.visible);
        $(withText("Перепланировать")).click();
        $("[data-test-id='success-notification'] .notification__content")
                .shouldHave(exactText("Встреча успешно запланирована на " + secondMeetingDate))
                .shouldBe(Condition.visible);

    }
}