package com.java.uidemo.model;

public class DropdownItem
{
    private int id, icon_resource;
    private String text;

    public DropdownItem()
    {
        id = 0;
        icon_resource = 0;
        text = "";
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getIcon_resource()
    {
        return icon_resource;
    }

    public void setIcon_resource(int icon_resource)
    {
        this.icon_resource = icon_resource;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }
}
