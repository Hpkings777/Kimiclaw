package com.sun.mail.imap;

/* JADX INFO: loaded from: classes.dex */
public class ACL implements Cloneable {
    private String name;
    private Rights rights;

    public ACL(String str) {
        this.name = str;
        this.rights = new Rights();
    }

    public Object clone() throws CloneNotSupportedException {
        ACL acl = (ACL) super.clone();
        acl.rights = (Rights) this.rights.clone();
        return acl;
    }

    public String getName() {
        return this.name;
    }

    public Rights getRights() {
        return this.rights;
    }

    public void setRights(Rights rights) {
        this.rights = rights;
    }

    public ACL(String str, Rights rights) {
        this.name = str;
        this.rights = rights;
    }
}
