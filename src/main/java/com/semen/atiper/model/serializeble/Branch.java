package com.semen.atiper.model.serializeble;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Branch {
    @JsonProperty("name")
    private String name;
    @JsonProperty("commit")
    private Commit commit;
    @JsonProperty("protected")
    private boolean protectedBranch;

    public Branch() {
    }

    public Branch(String name, Commit commit, boolean protectedBranch) {
        this.name = name;
        this.commit = commit;
        this.protectedBranch = protectedBranch;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Commit getCommit() {
        return commit;
    }

    public void setCommit(Commit commit) {
        this.commit = commit;
    }

    public boolean isProtectedBranch() {
        return protectedBranch;
    }

    public void setProtectedBranch(boolean protectedBranch) {
        this.protectedBranch = protectedBranch;
    }
}
