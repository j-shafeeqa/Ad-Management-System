package com.example.gradleproject1;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

public class AdvertisementTest {

    @Test
    public void testValidAdvertisementCreation() {
        try {
            Advertiser advertiser = new Advertiser(1, "ACME Corp", "+123-456-7890", "123 Elm Street");
            Advertisement advert = new Advertisement(
                101,
                "Valid Advertisement Content",
                100,
                "Front Page",
                1000,
                LocalDate.now().plusDays(7),
                advertiser
            );
            assertEquals(101, advert.getAdvertID());
            assertEquals(AdvertisementStatus.PENDING, advert.getStatus());
            assertEquals("Valid Advertisement Content", advert.getContent());
            System.out.println("Test Passed: Valid advertisement created successfully.");
        } catch (Exception e) {
            fail("Test Failed: Exception thrown for valid advertisement: " + e.getMessage());
        }
    }

    @Test
    public void testInvalidAdvertisementCreation() {
        try {
            Advertiser advertiser = new Advertiser(1, "ACME Corp", "+123-456-7890", "123 Elm Street");
            Advertisement advert = new Advertisement(
                102,
                "Invalid Advertisement Content",
                100,
                "Front Page",
                1000,
                LocalDate.now().minusDays(1), // Invalid: past publication date
                advertiser
            );
            fail("Test Failed: Exception expected but not thrown for invalid advertisement.");
        } catch (IllegalArgumentException e) {
            assertEquals("Publication date must be in the future", e.getMessage());
            System.out.println("Test Passed: Exception thrown as expected for invalid advertisement.");
        }
    }
}
