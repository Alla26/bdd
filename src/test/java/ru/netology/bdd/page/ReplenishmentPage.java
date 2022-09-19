package ru.netology.bdd.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class ReplenishmentPage {

    private SelenideElement heading = $("[data-test-id=dashboard]");
    private SelenideElement headingH1 = $("h1 [class=\"heading heading_size_xl heading_theme_alfa-on-white\"]");
    private SelenideElement amountField = $("[data-test-id=\"amount\"] .input__control");
    private SelenideElement whereFrom = $("[data-test-id=\"from\"] input");
    private SelenideElement whereTo = $("/[data-test-id=\"to\"] input");
    private SelenideElement button = $("[data-test-id=\"action-transfer\"] .button__text");
    private SelenideElement cancelButton = $("[data-test-id=\"action-cancel\"] .button__text");
    private SelenideElement errorNotification = $("[data-test-id=\"error-notification\"] .notification__content");


    public ReplenishmentPage() {
        heading.shouldBe(visible);
    }

    public DashboardPage successTransferCardToCard(String amount, String from) {
        amountField.val(amount);
        whereFrom.val(from);
        button.click();
        return new DashboardPage();
    }

    public ReplenishmentPage unSuccessTransferCardToCard(String amount, String from) {
        amountField.val(amount);
        whereFrom.val(from);
        button.click();
        errorNotification.shouldHave(text("Ошибка!"));
        return new ReplenishmentPage();
    }
}
