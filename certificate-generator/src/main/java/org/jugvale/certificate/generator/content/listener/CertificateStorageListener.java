package org.jugvale.certificate.generator.content.listener;

import org.jugvale.certificate.generator.model.Certificate;
import org.jugvale.certificate.generator.model.CertificateContent;

/**
 * Listen to new certificates and do some action with it.
 *
 */
public interface CertificateStorageListener {

    void newCertificateContent(CertificateContent storage);

    void removedCertificateContent(Certificate storage);

    default String name() {
        return this.getClass().getSimpleName();
    }

}