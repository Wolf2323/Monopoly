package eva.monopoly.game.cards;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import eva.monopoly.game.cards.event.EventJailCard;
import eva.monopoly.game.cards.event.EventUnjailCard;
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

				String target;
				int amount;
				int house;
				int hotel;

				switch (type) {
				case "jail":
					cards.add(new EventJailCard(text));
					break;
				case "unjail":
					cards.add(new EventUnjailCard(text));
					break;
				case "move":
					target = objAction.get("target").getAsString();
					break;
				case "movenext":
					target = objAction.get("target").getAsString();
					break;
				case "moveamount":
					amount = objAction.get("amount").getAsInt();
					break;
				case "movenextmoneydouble":
					target = objAction.get("target").getAsString();
					break;
				case "money":
					amount = objAction.get("amount").getAsInt();
					break;
				case "moneybuildings":
					house = objAction.get("house").getAsInt();
					hotel = objAction.get("hotel").getAsInt();
					break;
				case "moneyplayer":
					amount = objAction.get("amount").getAsInt();
					break;
				default:
					throw new IllegalArgumentException("type '" + type + "' is unknown");
				}
			}
		} catch (URISyntaxException | IOException | IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	public static abstract class EventCard extends Card {
		public EventCard(String text) {
			super(text);
		}
	}
}
