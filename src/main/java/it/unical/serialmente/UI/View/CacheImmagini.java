package it.unical.serialmente.UI.View;

import javafx.scene.image.Image;
import java.util.HashMap;

public class CacheImmagini {
    static CacheImmagini istanza;
    private final HashMap<String, Image> cache = new HashMap<>();

    private CacheImmagini() {}

    public static CacheImmagini getInstance() {
        if(istanza==null){
            istanza = new CacheImmagini();
        }
        return istanza;
    }

    public Image getImg(String url){
        if(cache.containsKey(url)){
            return cache.get(url);
        }
        String dimensionePoster = "https://image.tmdb.org/t/p/original";
        Image img = new Image(dimensionePoster +url,0,0,true,true,true);
        cache.put(url,img);
        return img;
    }
}