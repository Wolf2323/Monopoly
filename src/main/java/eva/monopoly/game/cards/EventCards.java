package eva.monopoly.game.cards;

import java.util.ArrayList;

public class EventCards {
	ArrayList<EventCard> cards = new ArrayList<>();

	public void loadCards() {
		
	}

	public abstract class EventCard extends Card {
		public EventCard(String text) {
			super(text);
		}
	}
}
