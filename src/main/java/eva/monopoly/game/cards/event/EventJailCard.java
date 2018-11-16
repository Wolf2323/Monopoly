package eva.monopoly.game.cards.event;

import eva.monopoly.game.cards.EventCards.EventCard;
import eva.monopoly.game.player.Player;

public class EventJailCard extends EventCard {

	public EventJailCard(String text) {
		super(text);
	}

	@Override
	public void action(Player p) {
		p.sendToJail();
	}

}
