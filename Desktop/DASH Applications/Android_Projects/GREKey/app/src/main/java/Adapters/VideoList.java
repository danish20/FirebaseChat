package Adapters;

/**
 * Created by Danish on 1/28/16.
 */
public class VideoList
{
    String name,link;
    public VideoList(String name,String link)
    {
        this.name=name;
        this.link=link;
    }
    public String getLink()
    {
        return link;
    }
    public String getName()
    {
        return name;
    }
}
