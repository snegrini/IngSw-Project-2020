package network.message;

import model.board.Position;
import model.enumerations.Color;

import java.util.List;

public class Init extends Message {
    private static final long serialVersionUID = 190423201260252146L;

    private List<Color> colorList;
    private Color color; //used in reply
    private List<Position> positionList;

    /**
     * Constructor for initialization request
     *
     * @param colorList    list of worker colors available.
     * @param positionList list of positions available on the board.
     */
    public Init(List<Color> colorList, List<Position> positionList) {
        super("", MessageType.INIT);
        this.colorList = colorList;
        this.positionList = positionList;
    }

    /**
     * Constructor for initialization reply
     *
     * @param color the color chosen by the user.
     * @param positionList the position list of the two workers chosen by the user.
     */
    public Init(Color color, List<Position> positionList) {
        super("", MessageType.INIT);
        this.color = color;
        this.positionList = positionList;
    }

    public List<Color> getColorList() {
        return colorList;
    }

    public List<Position> getPositionList() {
        return positionList;
    }

    @Override
    public String toString() {
        return "Init{" +
                "nickname=" + getNickname() +
                ", messageType=" + getMessageType() +
                "colorList=" + colorList +
                ", positionList=" + positionList +
                '}';
    }

    public Color getColor() {
        return color;
    }
}
