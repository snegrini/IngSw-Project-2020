package network.message;

import model.board.Position;
import model.enumerations.Color;

import java.util.List;

public class Init extends Message {
    //TODO serialVersionUID

    private List<Color> colorList;
    private Color color; //used in reply
    private List<Position> positionList;

    /**
     * Constructor for initialization request
     *
     * @param colorList
     * @param positionList
     */
    public Init(List<Color> colorList, List<Position> positionList) {
        super("",MessageType.INIT);
        this.colorList = colorList;
        this.positionList = positionList;
    }

    /**
     * Constructor for initialization reply
     *
     * @param color
     * @param positionList
     */
    public Init(Color color, List<Position> positionList) {
        super("",MessageType.INIT);
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
}
