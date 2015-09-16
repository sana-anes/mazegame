package mazegame.core;

import mazegame.server.Icon;

class End extends Thing {

    private static Icon ICON = new Icon('e');

    End(Place place) {
        super(place, ICON);
    }

    public char getIconChar() {
        return icon.getChar();
    }

    public String toString() {
        return "End = " + super.toString();
    }
}
