package com.edu.bjtu.fightagainstlandlord.Memento;

public class GameMementoCaretaker {
    private GameMemento gameMemento;

    public void setGameMemento(GameMemento gameMemento) {
        this.gameMemento = gameMemento;
    }

    public GameMemento getGameMemento() {
        return gameMemento;
    }
}
