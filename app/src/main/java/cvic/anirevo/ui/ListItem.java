package cvic.anirevo.ui;

public class ListItem<T> {

    //Either an item or a header

    private T item;

    private String headerText;

    private boolean header;

    public ListItem(T item) {
        this.item = item;
        header = false;
    }

    public ListItem(String headerText) {
        this.headerText = headerText;
        header = true;
    }

    public boolean isHeader() {
        return header;
    }

    public String getHeaderText() {
        return headerText;
    }

    public T getItem() {
        return item;
    }
}
