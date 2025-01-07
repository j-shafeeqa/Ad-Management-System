/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.example.gradleproject1;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

public class ProcessingCenterTest {

    @Test
    public void testStoreAdvertisementWithInsufficientStorage() {
        // Set up the system components
        ProcessingCenter processingCenter = new ProcessingCenter(1);

        // Set available storage to a small value (e.g., 50 bytes)
        SystemState.setAvailableStorage(50);

        // Create an advertiser
        Advertiser advertiser = new Advertiser(1, "ACME Corp", "+123-456-7890", "123 Elm Street");

        // Create an advertisement with size exceeding available storage
        Advertisement advert = new Advertisement(
            103,                    // Unique advert ID
            "Large Advertisement",  // Valid content
            100,                    // Size exceeding storage
            "Back Cover",           // Valid placement
            1000,                   // Valid payment terms
            LocalDate.now().plusDays(7), // Valid future publication date
            advertiser              // Valid advertiser
        );

        // Set the advertisement status to APPROVED
        advert.setStatus(AdvertisementStatus.APPROVED);

        // Attempt to store the advertisement and expect an exception
        Exception exception = assertThrows(
            IllegalStateException.class, 
            () -> processingCenter.storeAdvert(advert)
        );

        // Assert that the exception message matches the expected output
        assertEquals("Insufficient storage capacity", exception.getMessage());
    }
}
