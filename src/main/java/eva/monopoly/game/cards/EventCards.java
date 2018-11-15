package eva.monopoly.game.cards;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import eva.monopoly.utils.ResourceReaderUtil;

public class EventCards {
	ArrayList<EventCard> cards = new ArrayList<>();

	public void loadCards() {
		try {
			Path path = ResourceReaderUtil.getResourcePath("monopoly/resources/EventCards.json");
			JsonArray json = ResourceReaderUtil.getObjectAsJsonFromFile(path, JsonArray.class);
			System.out.println(json.toString());

			for (int i = 0; i < json.size(); i++) {
				JsonElement element = json.get(i);
				JsonObject obj = element.getAsJsonObject();

				String text = obj.get("text").getAsString();

				JsonObject objAction = obj.get("action").getAsJsonObject();
				String type = objAction.get("type").getAsString();

				switch (type) {
				case "money": {
					int amount = objAction.get("amount").getAsInt();
					cards.add(new EventMoneyCard());
					break;
				}
				default:
					break;
				}

			}
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
	}

	public abstract class EventCard extends Card {
		public EventCard(String text) {
			super(text);
		}
	}
}
