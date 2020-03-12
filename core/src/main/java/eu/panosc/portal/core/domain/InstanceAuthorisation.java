package eu.panosc.portal.core.domain;

public class InstanceAuthorisation {

    private InstanceMember member;
    private InstanceNetwork network;
    private Account account;

    public InstanceMember getMember() {
        return member;
    }

    public void setMember(InstanceMember member) {
        this.member = member;
    }

    public InstanceNetwork getNetwork() {
        return network;
    }

    public void setNetwork(InstanceNetwork network) {
        this.network = network;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
