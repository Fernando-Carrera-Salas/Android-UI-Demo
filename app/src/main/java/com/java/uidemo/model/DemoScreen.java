package com.java.uidemo.model;

import com.java.uidemo.MenuActivity;

public class DemoScreen
{
    private int name_resource;
    private int color_resource;
    private int icon_resource;
    private int help_resource;
    private Class class_reference;
    private boolean enabled;

    public DemoScreen()
    {
        name_resource = 0;
        color_resource = 0;
        icon_resource = 0;
        help_resource = 0;
        class_reference = MenuActivity.class;
        enabled = false;
    }

    public int getName_resource() {
        return name_resource;
    }

    public void setName_resource(int name_resource) {
        this.name_resource = name_resource;
    }

    public Class getClass_reference() {
        return class_reference;
    }

    public void setClass_reference(Class class_reference) {
        this.class_reference = class_reference;
    }

    public int getColor_resource()
    {
        return color_resource;
    }

    public void setColor_resource(int color_resource)
    {
        this.color_resource = color_resource;
    }

    public int getIcon_resource()
    {
        return icon_resource;
    }

    public void setIcon_resource(int icon_resource)
    {
        this.icon_resource = icon_resource;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public int getHelp_resource()
    {
        return help_resource;
    }

    public void setHelp_resource(int help_resource)
    {
        this.help_resource = help_resource;
    }
}
