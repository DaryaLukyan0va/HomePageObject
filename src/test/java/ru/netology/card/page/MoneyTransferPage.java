package ru.netology.card.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.card.data.DataGenerator.CardData;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

public class MoneyTransferPage {


    private final SelenideElement sumField = $("[data-test-id=amount] input");
    private final SelenideElement cardField = $("[data-test-id='from'] input");
    private final SelenideElement transferButton = $("[data-test-id=action-transfer]");
    private final SelenideElement notEnoughMoneyError = $(withText("Недостаточно средств для совершения перевода!"));

    public void moneyTransfer(CardData cardData, int amount) {
        sumField.setValue(String.valueOf(amount));
        cardField.setValue(cardData.getCardNumber());
        transferButton.click();
    }

    public void notEnoughMoneyError(CardData cardData, int amount) {
        sumField.setValue(String.valueOf(amount));
        cardField.setValue(cardData.getCardNumber());
        transferButton.click();
        if (amount > Integer.parseInt(cardData.getCardBalance())) {
            notEnoughMoneyError.shouldBe(Condition.visible);
        }

    }
}
