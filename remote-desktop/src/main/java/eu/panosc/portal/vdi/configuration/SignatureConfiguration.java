package eu.panosc.portal.vdi.configuration;

import javax.validation.constraints.NotNull;

public class SignatureConfiguration {


    public SignatureConfiguration() {

    }

    public SignatureConfiguration(@NotNull String privateKeyPath, @NotNull String publicKeyPath) {
        this.privateKeyPath = privateKeyPath;
        this.publicKeyPath = publicKeyPath;
    }

    @NotNull
    private String privateKeyPath;

    @NotNull
    private String publicKeyPath;


    public String getPrivateKeyPath() {
        return privateKeyPath;
    }

    public String getPublicKeyPath() {
        return publicKeyPath;
    }
}
