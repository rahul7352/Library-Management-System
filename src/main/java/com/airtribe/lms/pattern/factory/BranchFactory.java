package com.airtribe.lms.pattern.factory;

import com.airtribe.lms.entity.Branch;

import java.util.concurrent.atomic.AtomicInteger;

public class BranchFactory extends SequentialIdFactory {

    public BranchFactory() {
        super("BR");
    }

    public Branch createBranch(String branchName, String branchAddress) {
        return new Branch(nextId(), branchName, branchAddress);
    }
}
