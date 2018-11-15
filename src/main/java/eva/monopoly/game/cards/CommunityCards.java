package eva.monopoly.game.cards;

import java.util.ArrayList;

public class CommunityCards {
	ArrayList<CommunityCard> cards = new ArrayList<>();
	
	public void loadCards() {
		
	}
	public abstract class CommunityCard extends Card {
		public CommunityCard(String text) {
			super(text);
		}
	}

}
