package ExcursionOrganizer.DB;

//import sun.plugin2.main.server.Plugin;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 3/4/11
 * Time: 10:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class PlaceOfInterest {

    private int id;
    private String name;
    private String description;

    public PlaceOfInterest(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public PlaceOfInterest(String name, String description) {
        this(-1, name, description);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String toString() {
        return "name = " + name + ", descr = " + description;
    }
}
