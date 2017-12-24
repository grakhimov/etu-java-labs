package javalabs.classes;

public class Workspace {
    private Integer id;
    private String name;
    private String desc;

    public Workspace(Integer id, String name, String desc){
        this.id = id;
        this.name = name;
        this.desc = desc;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
