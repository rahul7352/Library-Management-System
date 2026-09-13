package com.airtribe.lms.service;

import com.airtribe.lms.entity.Patron;
import com.airtribe.lms.pattern.factory.PatronFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PatronService {

    private static final Logger logger = Logger.getLogger(PatronService.class.getName());
    private final PatronFactory patronFactory;
    private final Map<String, Patron> patrons = new LinkedHashMap<>();

    public PatronService(PatronFactory patronFactory) {
        this.patronFactory = patronFactory;
    }

    public Patron addPatron(String name, String email) {
        Patron patron = patronFactory.createPatron(name, email);
        patrons.put(patron.getPatronId(), patron);
        logger.log(Level.INFO, "Created patron: {0}", patron.getPatronId());
        return patron;
    }

    public void updatePatronsInfo(String patronId, String name, String email) {
        Patron patron = patrons.get(patronId);
        if(patron == null) {
            logger.log(Level.WARNING, "Attempted to update unknown patron: {0}", patronId);
            return;
        }
        patron.setName(name);
        patron.setEmail(email);
    }

    public Optional<Patron> findPatronById(String patronId) {
        return Optional.ofNullable(patrons.get(patronId));
    }
}
