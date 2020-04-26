package network.message;

import model.enumerations.Color;

import java.util.List;

public class ColorsMessage extends Message {

    private List<Color> colorList;


    public ColorsMessage(String nickname, List<Color> colorList) {
        super(nickname, MessageType.INIT_COLORS);
        this.colorList = colorList;
    }

    @Override
    public String toString() {
        return "ColorsMessage{" +
                "nickname=" + getNickname() +
                ", messageType=" + getMessageType() +
                ", colorList=" + colorList +
                '}';
    }

    public List<Color> getColorList() {
        return colorList;
    }
}
