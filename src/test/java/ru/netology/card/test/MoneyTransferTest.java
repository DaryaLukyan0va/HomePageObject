package ru.netology.card.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.card.data.DataGenerator;
import ru.netology.card.data.DataGenerator.CardData;
import ru.netology.card.page.DashboardPage;
import ru.netology.card.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {

    DashboardPage dashboardPage;
    private SelenideElement notEnoughMoneyError;

    @BeforeEach
    void setUp() {
        Configuration.headless = true;
        open("http://localhost:9999", LoginPage.class);
        val loginPage = new LoginPage();
        val authInfo = DataGenerator.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataGenerator.getVerificationCodeFor(authInfo);
        dashboardPage = verificationPage.validVerify(verificationCode);

    }

    @Test
    public void moneyTransferFromTheFirstToTheSecondCard() {
        int amount = 1;
        var firstCardData = CardData.getFirstCardData();
        var secondCardData = CardData.getSecondCardData();
        int firstCardBalanceBeforeTransfer = dashboardPage.getFirstCardCurrentBalance();
        int secondCardCurrentBalanceBeforeTransfer = dashboardPage.getSecondCardCurrentBalance();
        val moneyTransferPage = dashboardPage.secondCard();
        moneyTransferPage.moneyTransfer(firstCardData, amount);
        int firstCardBalanceAfterTransfer = DataGenerator.balanceCardTransferFrom(firstCardBalanceBeforeTransfer, amount);
        int secondCArdBalanceAfterTransfer = DataGenerator.balanceCardTransferTo(secondCardCurrentBalanceBeforeTransfer, amount);
        int firstCardNewBalance = dashboardPage.getFirstCardCurrentBalance();
        int secondCArdNewBalance = dashboardPage.getSecondCardCurrentBalance();
        assertEquals(firstCardBalanceAfterTransfer, firstCardNewBalance);
        assertEquals(secondCArdBalanceAfterTransfer, secondCArdNewBalance);
    }

    @Test
    public void moneyTransferFromTheSecondToTheFirstCard() {
        int amount = 1;
        var firstCardBalanceBeforeTransfer = dashboardPage.getFirstCardCurrentBalance();
        var secondCardCurrentBalanceBeforeTransfer = dashboardPage.getSecondCardCurrentBalance();
        val moneyTransferPage = dashboardPage.firstCard();
        CardData secondCardData = CardData.getSecondCardData();
        moneyTransferPage.moneyTransfer(secondCardData, amount);
        int firstCardBalanceAfterTransfer = DataGenerator.balanceCardTransferTo(firstCardBalanceBeforeTransfer, amount);
        int secondCArdBalanceAfterTransfer = DataGenerator.balanceCardTransferFrom(secondCardCurrentBalanceBeforeTransfer, amount);
        int firstCardNewBalance = dashboardPage.getFirstCardCurrentBalance();
        int secondCArdNewBalance = dashboardPage.getSecondCardCurrentBalance();
        assertEquals(firstCardBalanceAfterTransfer, firstCardNewBalance);
        assertEquals(secondCArdBalanceAfterTransfer, secondCArdNewBalance);
    }

    @Test
    public void transferFromTheFirstCardToTheSecondMoreThanThereIsMoney() {
        int amount = 100_000;
        var firstCardData = CardData.getFirstCardData();
        var secondCardData = CardData.getSecondCardData();
        val moneyTransferPage = dashboardPage.secondCard();
        moneyTransferPage.moneyTransfer(firstCardData, amount);
    }

    @Test
    public void transferFromTheSecondCardToTheFirstMoreThanThereIsMoney() {
        int amount = 110_000;
        var secondCardData = CardData.getSecondCardData();
        var firstCardData = CardData.getFirstCardData();
        val moneyTransferPage = dashboardPage.secondCard();
        moneyTransferPage.moneyTransfer(secondCardData, amount);
    }

    @Test
    public void notEnoughMoneyError() {
        notEnoughMoneyError.shouldBe(Condition.visible);
    }

}
