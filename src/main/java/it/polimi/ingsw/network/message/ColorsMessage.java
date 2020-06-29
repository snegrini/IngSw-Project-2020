package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.enumerations.Color;

import java.util.List;

public class ColorsMessage extends Message {

    private static final long serialVersionUID = -3704504226997118508L;
    private final List<Color> colorList;

    public ColorsMessage(String nickname, List<Color> colorList) {
        super(nickname, MessageType.INIT_COLORS);
        this.colorList = colorList;
    }

    @Override
    public String toString() {
        return "ColorsMessage{" +
                "nickname=" + getNickname() +
                ", colorList=" + colorList +
                '}';
    }

    public List<Color> getColorList() {
        return colorList;
    }
}
