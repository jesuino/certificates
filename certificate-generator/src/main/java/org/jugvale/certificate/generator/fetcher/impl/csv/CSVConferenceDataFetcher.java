package org.jugvale.certificate.generator.fetcher.impl.csv;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import io.quarkus.arc.AlternativePriority;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.jugvale.certificate.generator.fetcher.ConferenceData;
import org.jugvale.certificate.generator.fetcher.ConferenceDataFetcher;

/**
 * 
 * Fetches Conference data from a CSV file
 * 
 * @author wsiqueir
 *
 */
@Alternative
@ApplicationScoped
@AlternativePriority(1)
public class CSVConferenceDataFetcher implements ConferenceDataFetcher {

    public static final String DESCRIPTION = "Fetches data from CSV file";
    public static final String NAME = "CSV";
    public static final String FILE_PROP = "certificate.fetcher.csv.file";

    Logger logger = Logger.getLogger(CSVConferenceDataFetcher.class);

    @Inject
    @ConfigProperty(name = FILE_PROP, defaultValue = "")
    Optional<String> csvConfFilePath;

    @Inject
    CSVProcessor CSVProcessor;

    @Override
    public String description() {
        return DESCRIPTION;
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public ConferenceData conferenceData() {
        if (csvConfFilePath.isEmpty() || csvConfFilePath.get().isBlank()) {
            logger.infov("CSV file path {0} not specified.", FILE_PROP);
            return ConferenceData.empty();
        }
        logger.infov("Loading data from file {0}", FILE_PROP);
        return CSVProcessor.processCSV(csvConfFilePath.get());
    }

}
