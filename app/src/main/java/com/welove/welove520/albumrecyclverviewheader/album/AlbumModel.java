package com.welove.welove520.albumrecyclverviewheader.album;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Raomengyang on 17-9-6.
 * Email    : ericrao@welove-inc.com
 * Desc     :
 * Version  : 1.0
 */

public class AlbumModel implements Serializable {

    private static final long serialVersionUID = -8026941593964108107L;

    private List<String> photos;

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }
}
