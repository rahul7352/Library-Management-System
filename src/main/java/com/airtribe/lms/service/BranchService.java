package com.airtribe.lms.service;

import com.airtribe.lms.entity.Branch;
import com.airtribe.lms.exception.BranchNotFoundException;
import com.airtribe.lms.pattern.factory.BranchFactory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BranchService {

    private static final Logger logger = Logger.getLogger(BranchService.class.getName());
    private final BranchFactory branchFactory;
    private final Map<String, Branch> branches = new LinkedHashMap<>();

    public BranchService(BranchFactory branchFactory) {
        this.branchFactory = branchFactory;
    }

    public Branch createBranch(String branchName, String branchAddress) {
        Branch branch = branchFactory.createBranch(branchName, branchAddress);
        branches.put(branch.getBranchId(), branch);
        logger.log(Level.INFO, "Created branch: {0}", branch);
        return branch;
    }

    public Branch findBranchById(String branchId) throws BranchNotFoundException {
        Branch branch = branches.get(branchId);
        if (branch == null) {
            throw new BranchNotFoundException("No branch with id " + branchId);
        }
        return branch;
    }

    public List<Branch> findAllBranches() {
        return List.copyOf(branches.values());
    }
}
