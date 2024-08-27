package com.java.uidemo.model;

public class DemoLanguage
{
    private String iso_code;
    private int icon_resource;
    private int name_resource;

    public DemoLanguage()
    {
        iso_code = "";
        icon_resource = 0;
        name_resource = 0;
    }

    public String getIso_code()
    {
        return iso_code;
    }

    public void setIso_code(String iso_code)
    {
        this.iso_code = iso_code;
    }

    public int getIcon_resource()
    {
        return icon_resource;
    }

    public void setIcon_resource(int icon_resource)
    {
        this.icon_resource = icon_resource;
    }

    public int getName_resource()
    {
        return name_resource;
    }

    public void setName_resource(int name_resource)
    {
        this.name_resource = name_resource;
    }
}
