package eva.monopoly.client.utils;

import eva.monopoly.api.game.player.Player.Pawn;

public class PlayerLobby {
	private String name;
	private String pawn;
	private String readyStatus;

	public PlayerLobby(String name, String pawn, String readyStatus) {
		this.name = name;
		this.pawn = pawn;
		this.readyStatus = readyStatus;
	}

	public PlayerLobby(String name, String pawn) {
		this.name = name;
		this.pawn = pawn;
		this.readyStatus = "Not Ready";
	}

	public PlayerLobby(String name) {
		this.name = name;
		this.pawn = "No Selection";
		this.readyStatus = "Not Ready";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPawn() {
		return pawn;
	}

	public void setPawn(String pawn) {
		this.pawn = pawn;
	}

	public String getReadyStatus() {
		return readyStatus;
	}

	public void setReadyStatus() {
		switch (this.readyStatus) {
		case "Ready":
			this.readyStatus = "Not Ready";
			return;
		case "Not Ready":
			this.readyStatus = "Ready";
			return;
		}
	}

}
