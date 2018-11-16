package eva.monopoly.game.card.cards;

import eva.monopoly.game.card.Card;
import eva.monopoly.game.card.Cards;
import eva.monopoly.game.player.Player;

public class UnjailCard extends Card {
	private Cards eventCards;

	public UnjailCard(String text, CardType cardType, Cards eventCards) {
		super(text, cardType);
		this.eventCards = eventCards;
	}

	@Override
	public void action(Player p) {
		eventCards.pickupCard(this, p);
	}

}
