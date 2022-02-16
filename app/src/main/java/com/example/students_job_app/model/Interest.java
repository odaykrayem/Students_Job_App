package com.example.students_job_app.model;

public class Interest {

    private int id;
    private String tag;
    private boolean selected;

    public Interest(int id, String tag, boolean selected) {
        this.id = id;
        this.tag = tag;
        this.selected = selected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
