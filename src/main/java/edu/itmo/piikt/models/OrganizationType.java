package edu.itmo.piikt.models;

public enum OrganizationType {
    COMMERCIAL(1),
    PUBLIC(2),
    GOVERNMENT(3),
    TRUST(4),
    OPEN_JOINT_STOCK_COMPANY(5);

    private final int id;
    OrganizationType(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }
}