package javalabs.models;

import javalabs.classes.Workspace;

public class Model {
    Workspace currentItem;
    public Workspace getCurrentItem() {
        return currentItem;
    }
    public void refresh(){}
    public int save(int id, String name, String desc){ return 0; }
}
