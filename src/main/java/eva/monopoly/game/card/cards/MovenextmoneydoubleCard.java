package eva.monopoly.game.card.cards;

import eva.monopoly.game.card.Card;
import eva.monopoly.game.player.Player;

public class MovenextmoneydoubleCard extends Card {
	private String target;

	public MovenextmoneydoubleCard(String text, CardType cardType, String target) {
		super(text, cardType);
		this.target = target;
	}

	@Override
	public void action(Player p) {
		p.setPayDouble();
		p.moveToNextTarget(target);
	}

}
