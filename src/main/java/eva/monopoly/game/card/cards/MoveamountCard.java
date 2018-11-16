package eva.monopoly.game.card.cards;

import eva.monopoly.game.card.Card;
import eva.monopoly.game.player.Player;

public class MoveamountCard extends Card {
	private int amount;

	public MoveamountCard(String text, CardType cardType, int amount) {
		super(text, cardType);
		this.amount = amount;
	}

	@Override
	public void action(Player p) {
		p.moveAmount(amount);
	}

}
