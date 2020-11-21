package org.jugvale.certificate.generator.event;

import org.jugvale.certificate.generator.model.Certificate;

/**
 * Email fire when a certificate is deleted.
 *
 */
public class DeletedCertificateEvent {

    private Certificate certificate;

    public DeletedCertificateEvent(Certificate certificate) {
        this.certificate = certificate;
    }

    public Certificate getCertificate() {
        return certificate;
    }
}