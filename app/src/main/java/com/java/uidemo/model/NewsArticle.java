package com.java.uidemo.model;

public class NewsArticle
{
    private int image_resource, text_resource;
    private boolean loading;

    public NewsArticle()
    {
        image_resource = 0;
        text_resource = 0;
        loading = true;
    }

    public NewsArticle(int image_resource, int text_resource)
    {
        this.image_resource = image_resource;
        this.text_resource = text_resource;
        loading = true;
    }

    public int getImage_resource()
    {
        return image_resource;
    }

    public void setImage_resource(int image_resource)
    {
        this.image_resource = image_resource;
    }

    public int getText_resource()
    {
        return text_resource;
    }

    public void setText_resource(int text_resource)
    {
        this.text_resource = text_resource;
    }

    public boolean isLoading()
    {
        return loading;
    }

    public void setLoading(boolean loading)
    {
        this.loading = loading;
    }
}
