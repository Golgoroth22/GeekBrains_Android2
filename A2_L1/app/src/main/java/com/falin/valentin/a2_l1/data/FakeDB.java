package com.falin.valentin.a2_l1.data;

import java.util.ArrayList;
import java.util.List;

public class FakeDB {
    private static FakeDB ourInstance;

    private static List<Note> db;

    public static FakeDB getInstance() {
        if (ourInstance == null) {
            return new FakeDB();
        }
        return ourInstance;
    }

    private FakeDB() {
        ourInstance = new FakeDB();
        db = new ArrayList<>();
        db.add(new Note("Note 1", "Text 1 Text 1 Text 1 Text 1 Text 1"));
        db.add(new Note("Note 2", "Text 2 Text 2 Text 2 Text 2 Text 2"));
        db.add(new Note("Note 3", "Text 3 Text 3 Text 3 Text 3 Text 3"));
        db.add(new Note("Note 4", "Text 4 Text 4 Text 4 Text 4 Text 4"));
        db.add(new Note("Note 5", "Text 5 Text 5 Text 5 Text 5 Text 5"));
    }
}
