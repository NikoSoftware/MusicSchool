package com.example.niko.musicschool.model;

/**
 * 首页banner
 * <p/>
 * Created by wzd on 2016/8/25.
 */
public class BannerModel {

    private String id;
    private String name;
    private String img;
    private String link_type;
    private String url;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLink_type() {
        return link_type;
    }

    public void setLink_type(String link_type) {
        this.link_type = link_type;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "BannerModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", img='" + img + '\'' +
                ", link_type='" + link_type + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
