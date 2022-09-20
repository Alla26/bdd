package ru.netology.bdd.test;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.bdd.data.DataHelper;
import ru.netology.bdd.page.DashboardPage;
import ru.netology.bdd.page.LoginPage;
import ru.netology.bdd.page.ReplenishmentPage;


import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MoneyTransferTest {
    @BeforeEach
    void loginToAccount() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @AfterEach
    void returnBalance() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
        var DashboardPage = new DashboardPage();
        var firstCardBalance = DashboardPage.getCardBalance(DataHelper.getFirstCard().getId());
        var secondCardBalance = DashboardPage.getCardBalance(DataHelper.getSecondCard().getId());
        if (firstCardBalance > secondCardBalance) {
            DashboardPage.replenishSecondCardClick();
            var ReplenishmentPage = new ReplenishmentPage();
            ReplenishmentPage.successTransferCardToCard(String.valueOf((firstCardBalance - secondCardBalance) / 2), DataHelper.getFirstCard().getNumber());
        } else if (firstCardBalance < secondCardBalance) {
            DashboardPage.replenishFirstCardClick();
            var ReplenishmentPage = new ReplenishmentPage();
            ReplenishmentPage.successTransferCardToCard(String.valueOf((secondCardBalance - firstCardBalance) / 2), DataHelper.getSecondCard().getNumber());
        }
    }

    @AfterEach
    void closeWebBrowser() {
        closeWebDriver();
    }

    @Test
    void shouldTransferMoneyFromFirstToSecondCard() {
        var DashboardPage = new DashboardPage();
        DashboardPage.replenishSecondCardClick();
        var ReplenishmentPage = new ReplenishmentPage();
        var amount = 7000;
        ReplenishmentPage.successTransferCardToCard(String.valueOf(amount), DataHelper.getFirstCard().getNumber());
        var firstCardBalance = DashboardPage.getCardBalance(DataHelper.getFirstCard().getId());
        var secondCardBalance = DashboardPage.getCardBalance(DataHelper.getSecondCard().getId());
        assertEquals(10000 - amount, firstCardBalance);
        assertEquals(10000 + amount, secondCardBalance);
    }

    @Test
    void shouldTransferMoneyFromSecondToFirstCard() {
        var DashboardPage = new DashboardPage();
        DashboardPage.replenishFirstCardClick();
        var ReplenishmentPage = new ReplenishmentPage();
        var amount = 3500;
        ReplenishmentPage.successTransferCardToCard(String.valueOf(amount), DataHelper.getSecondCard().getNumber());
        var firstCardBalance = DashboardPage.getCardBalance(DataHelper.getFirstCard().getId());
        var secondCardBalance = DashboardPage.getCardBalance(DataHelper.getSecondCard().getId());
        assertEquals(10000 + amount, firstCardBalance);
        assertEquals(10000 - amount, secondCardBalance);
    }

    @Test
    void shouldTransferNotWholeAmountFromFirstToSecondCard() {
        var DashboardPage = new DashboardPage();
        DashboardPage.replenishSecondCardClick();
        var ReplenishmentPage = new ReplenishmentPage();
        var amount = 500.00;
        ReplenishmentPage.successTransferCardToCard(String.valueOf(amount), DataHelper.getFirstCard().getNumber());
        var firstCardBalance = DashboardPage.getCardBalance(DataHelper.getFirstCard().getId());
        var secondCardBalance = DashboardPage.getCardBalance(DataHelper.getSecondCard().getId());
        assertEquals(10000 - amount, firstCardBalance);
        assertEquals(10000 + amount, secondCardBalance);
    }

    @Test
    void shouldNotTransferAmountGreaterBalanceFromSecondToFirstCard() {
        var DashboardPage = new DashboardPage();
        DashboardPage.replenishFirstCardClick();
        var ReplenishmentPage = new ReplenishmentPage();
        var amount = 11000;
        ReplenishmentPage.unSuccessTransferCardToCard(String.valueOf(amount), DataHelper.getSecondCard().getNumber());
    }


}