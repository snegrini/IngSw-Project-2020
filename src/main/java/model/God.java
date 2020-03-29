package model;

public class  God {
    private int id;
    private String name;
    private String caption;
    private String description;
    private List<Effect> effectList;

    public God(int id, String name, String caption, String description, List<Effect> effectList) {
        this.id = id;
        this.name = name;
        this.caption = caption;
        this.description = description;
        this.effectList = effectList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Effect> getEffectList() {
        return effectList;
    }

    public void setEffectList(List<Effect> effectList) {
        this.effectList = effectList;
    }
}
