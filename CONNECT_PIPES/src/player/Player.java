package player;

public class Player {

    static int nextId = 1;
    int id;
    String nick;
    int score;
    int level = 1;

    public Player() {
        id = nextId;
        nextId++;
        nick = "";
        score = 0;
    }
    
    public Player(String nick, int score) {
        this();
        this.nick = nick;
        this.score = score;
    }
    
    public Player(int id, String nick, int score) {
        this.id = id;
        if (id >= nextId) {
            nextId = id + 1;        // Uwaga może być z tym problem;
        }
        this.nick = nick;
        this.score = score;
    }
    
    public Player setNick(String nick) {
        this.nick = nick;
        return this;
    }
    
    public Player setScore(int score) {
        this.score = score;
        return this;
    }
    
    public Player setLevel(int level) {
        this.level = level;
        return this;
    }
    
    public Player addToScore(int x) {
        this.score += x;
        return this;
    }
    
    public int getId() {
        return id;
    }
    public String getNick() {
        return nick;
    }
    
    public int getScore() {
        return score;
    }
    
    public int getLevel() {
        return level;
    }
    
    @Override
    public String toString() {
        String temp;
        temp = id + " " + nick + " " + score + " " + level;
        return temp;
    }
}
