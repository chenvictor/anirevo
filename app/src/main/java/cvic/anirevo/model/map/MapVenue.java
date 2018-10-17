package cvic.anirevo.model.map;

public class MapVenue {

    private String name;
    private String imagePath;

    MapVenue(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath (String path) {
        imagePath = path;
    }

}