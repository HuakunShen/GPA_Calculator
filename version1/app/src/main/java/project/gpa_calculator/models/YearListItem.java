package project.gpa_calculator.models;

public class YearListItem extends ListItem {
    private String name;
    private String description;
    private Object obj;


    private String gpa;


    public YearListItem(String name, String description, String gpa, Object obj) {
        this.name = name;
        this.description = description;
        this.gpa = gpa;
        this.obj = obj;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public String getGpa() {
        return gpa;
    }

    public void setGpa(String gpa) {
        this.gpa = gpa;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
