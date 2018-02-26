package com.falin.valentin.a2_l1.data;

import com.falin.valentin.a2_l1.Note;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FakeDB implements Serializable {
    private static List<Note> db = new ArrayList<>();

    static {
//        db.add(new Note("Note 1", "Curabitur metus nulla, viverra in ipsum sit amet, placerat vulputate nulla. Pellentesque vitae libero in ipsum egestas egestas. Duis diam velit, bibendum ac libero EXTRA_ID, pellentesque interdum est."));
//        db.add(new Note("Note 2", "Duis gravida venenatis enim, in luctus dolor tristique EXTRA_ID. Donec dignissim erat a dapibus efficitur. Interdum et malesuada fames ac ante ipsum primis in faucibus."));
//        db.add(new Note("Note 3", "Donec eget tincidunt quam. Etiam in sem commodo, cursus lectus vel, vulputate dui."));
//        db.add(new Note("Note 4", "Curabitur consectetur nulla in ipsum sollicitudin, nec venenatis purus scelerisque. Donec maximus, sapien in dictum tincidunt, lacus mi varius erat, ac viverra erat turpis in purus. Cras eget imperdiet turpis. Proin faucibus tortor in est sollicitudin, sit amet molestie eros efficitur. Quisque a posuere neque."));
//        db.add(new Note("Note 5", "Cras ac dolor vitae neque ultrices iaculis ac vitae purus. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Ut a sapien vel nunc hendrerit consequat eu ut orci. Aenean tortor odio, tincidunt in tincidunt non, mattis nec quam. In sed rutrum tellus, eleifend tincidunt tellus."));
    }

    public static List<Note> getDb() {
        return db;
    }
}
