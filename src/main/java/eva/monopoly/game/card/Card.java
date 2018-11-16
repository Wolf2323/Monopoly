package eva.monopoly.game.card;

import eva.monopoly.game.player.Player;

public abstract class Card {
	private String text;
	private CardType cardType;

	public Card(String text, CardType cardType) {
		this.text = text;
		this.cardType = cardType;
	}

	public String getText() {
		return text;
	}

	public CardType getCardType() {
		return cardType;
	}

	public abstract void action(Player p);

	public static enum CardType {
		EVENT, COMMUNITY;
	}
}
