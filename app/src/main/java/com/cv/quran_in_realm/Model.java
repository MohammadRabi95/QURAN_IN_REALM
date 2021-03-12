package com.cv.quran_in_realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Model extends RealmObject {
    @PrimaryKey
    public int num;
    public String name;

}
