package cvic.anirevo.model.anirevo;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import cvic.anirevo.model.StarManager;
import cvic.anirevo.model.Starrable;

public class ArGuest extends Starrable {

    private final int id;

    private final String name;
    private String title;
    private String japanese;

    private String portraitPath;

    private Set<ArEvent> events;

    private boolean starred = false;

    ArGuest(String name, int id) {
        this.id = id;
        this.name = name;
        events = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArGuest guest = (ArGuest) o;
        return Objects.equals(name, guest.name) &&
                Objects.equals(title, guest.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, title);
    }

    public void addEvent(ArEvent arEvent) {
        events.add(arEvent);
    }

    public boolean hasJapanese() {
        return japanese != null;
    }

    public String getJapanese() {
        return japanese;
    }

    public void setJapanese(String japanese) {
        this.japanese = japanese;
    }

    public int getId() {
        return id;
    }

    public String getPortraitPath() {
        return portraitPath;
    }

    public void setPortraitPath(String portraitPath) {
        this.portraitPath = portraitPath;
    }

    @Override
    public boolean toggleStarred() {
        boolean starred = super.toggleStarred();
        if (starred) {
            StarManager.getInstance().add(this);
        } else {
            StarManager.getInstance().remove(this);
        }
        return starred;
    }
}
