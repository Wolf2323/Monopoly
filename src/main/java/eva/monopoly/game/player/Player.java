package eva.monopoly.game.player;

public interface Player {

	// Player
	public Pawn getPawn();

	public String getName();

	// Money
	public int getMoney();

	public int addMoney(int money);

	public int removeMoney(int money);

	public boolean transferMoney(Player p, int money);

	public void sendToJail();

	public void releaseFromJail();

	public void moveToTaregt(String target);

	public void moveToNextTarget(String target);

	public void moveAmount(int amount);

	public void setPayDouble();

}
