package eva.monopoly.game.cards;

import eva.monopoly.game.player.Player;

public abstract class Card {
	private String text;

	public Card(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public abstract void action(Player p);
}
