package com.airtribe.lms.pattern.factory;

import com.airtribe.lms.entity.Patron;

public class PatronFactory extends SequentialIdFactory {

    public PatronFactory() {
        super("PR");
    }

    public Patron createPatron(String name, String email) {
        return new Patron(nextId(), name, email);
    }
}
