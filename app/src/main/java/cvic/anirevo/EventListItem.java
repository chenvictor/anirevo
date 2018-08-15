package cvic.anirevo;

import cvic.anirevo.model.anirevo.ArEvent;

class EventListItem {

    //Either an item or a header

    private ArEvent event;

    private String headerText;
    private int headerColor;

    private boolean header;

    public EventListItem(ArEvent event) {
        this.event = event;
        header = false;
    }

    public EventListItem(String headerText, int headerColor) {
        this.headerText = headerText;
        this.headerColor = headerColor;
        header = true;
    }

    public boolean isHeader() {
        return header;
    }

    public String getHeaderText() {
        return headerText;
    }

    public int getHeaderColor() {
        return headerColor;
    }

    public ArEvent getEvent() {
        return event;
    }
}
