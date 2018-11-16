package eva.monopoly.game.card.cards;

import eva.monopoly.game.card.Card;
import eva.monopoly.game.player.Player;

public class JailCard extends Card {

	public JailCard(String text, CardType cardType) {
		super(text, cardType);
	}

	@Override
	public void action(Player p) {
		p.sendToJail();
	}

}
