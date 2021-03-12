package com.cv.quran_in_realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Model extends RealmObject {


    public static final String PROPERTY_NAME = "name";
    public static final String PROPERTY_NUM = "num";

    @PrimaryKey
    //@Required
    public int num;
    public String name;

}
