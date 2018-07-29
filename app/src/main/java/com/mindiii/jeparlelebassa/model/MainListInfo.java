package com.mindiii.jeparlelebassa.model;

import java.io.Serializable;

/**
 * Created by mindiii on 14/4/17.
 */

public class MainListInfo implements Serializable {

    public Integer MainListItemId;
    public String  MainListItemName,MainListItemImage;

    public MainListInfo(){
        this.MainListItemId = null;
        this.MainListItemName = "";
        this.MainListItemImage = "";
    }

}
