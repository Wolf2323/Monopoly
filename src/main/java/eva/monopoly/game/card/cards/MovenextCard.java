package eva.monopoly.game.card.cards;

import eva.monopoly.game.card.Card;
import eva.monopoly.game.player.Player;

public class MovenextCard extends Card {
	String target;

	public MovenextCard(String text, CardType cardType, String target) {
		super(text, cardType);
		this.target = target;
	}

	@Override
	public void action(Player p) {
		p.moveToNextTarget(target);
	}

}
