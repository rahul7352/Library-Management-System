package com.airtribe.lms.pattern.factory;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class SequentialIdFactory {

    private final String prefix;
    private final AtomicInteger sequence = new AtomicInteger(1);
    protected SequentialIdFactory(String prefix) {
        this.prefix = prefix;
    }

    protected final String nextId() {
        return String.format("%s%03d", prefix, sequence.getAndIncrement());
    }
}
