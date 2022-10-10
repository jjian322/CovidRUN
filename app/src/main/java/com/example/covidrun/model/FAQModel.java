package com.example.covidrun.model;

public class FAQModel {
    private String title, content;
    private boolean expanded;

    public FAQModel(String title, String content) {
        this.title = title;
        this.content = content;
        this.expanded = false;
    }

    @Override
    public String toString() {
        return "FAQ{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", expanded=" + expanded +
                '}';
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
