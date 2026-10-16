package creature;
public interface Creature {
    boolean isAlive();
    String showStatus();
    String attack(Creature target);
    String getName();
    int getHp();
    void setHp(int hp);
}