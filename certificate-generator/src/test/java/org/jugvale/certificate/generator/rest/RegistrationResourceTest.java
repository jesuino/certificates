package org.jugvale.certificate.generator.rest;

import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.post;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.jugvale.certificate.generator.fetcher.ConferenceData;
import org.jugvale.certificate.generator.fetcher.impl.ConfigurationDataFetcher;
import org.jugvale.certificate.generator.model.Registration;
import org.jugvale.certificate.generator.model.RegistrationSummary;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class RegistrationResourceTest {
    
    
    private static final String REGISTRATION_EXTERNAL_CONFERENCE_ID_PARAM_URI = "/registration/external_conference_id/{id}";
    
    @Test
    public void testRegistrationsForEvent() {
        ConferenceData confData = createConferenceData();
        Registration registration = confData.getRegistrations().get(0);
        
        RegistrationSummary[] registrationSummary = get(REGISTRATION_EXTERNAL_CONFERENCE_ID_PARAM_URI, 
                                                        registration.conference.externalId)
                                                     .then()
                                                     .statusCode(200)
                                                     .extract()
                                                     .as(RegistrationSummary[].class);
        
        assertEquals(1, registrationSummary.length);
        RegistrationSummary summary = registrationSummary[0];
        assertEquals(registration.attendee.name, summary.getAttendeeName());
        assertEquals(registration.attendee.id, summary.getAttendeeId());
        assertEquals(registration.conference.name, summary.getConferenceName());
        assertEquals(registration.conference.externalId, summary.getConferenceExternalId());
        
        get(REGISTRATION_EXTERNAL_CONFERENCE_ID_PARAM_URI, 123456).then().statusCode(404);
        
    }
    
    private ConferenceData createConferenceData() {
        delete(ConferenceDataFetcherResource.FETCHER_BASE_URI).then().statusCode(204);
        return post(ConferenceDataFetcherResource.FETCH_DATA_FULL_URI, ConfigurationDataFetcher.NAME).then()
                                                                           .statusCode(200)
                                                                           .extract()
                                                                           .as(MutableConferenceData.class);
    }

}
