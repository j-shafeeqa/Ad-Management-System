package com.example.gradleproject1;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.*;

// Centralized Logger Utility
class LoggerUtil {
    private static final Logger LOGGER = Logger.getLogger("AdvertisingSystem");
    private static boolean isInitialized = false;

    // Returns a synchronized logger instance
    public static synchronized Logger getLogger() {
         // Initialize logger if not already done
        if (!isInitialized) {
            try {
                FileHandler fh = new FileHandler("advertising_system.log", true);// Log to file
                fh.setFormatter(new SimpleFormatter());// Simple log formatting
                LOGGER.addHandler(fh);// Add file handler to logger
                isInitialized = true; // Mark logger as initialized
            } catch (Exception e) {
                System.err.println("Failed to initialize logger: " + e.getMessage());// Error handling
            }
        }
        return LOGGER;// Return the logger instance
    }
}

// System State Manager
class SystemState {
    private static boolean operational = true;// Flag indicating if the system is operational
    private static long availableStorage = 1000000; // Example storage limit

    // Returns the operational status of the system
    public static boolean isOperational() {
        return operational;
    }

    // Returns the current available storage
    public static long getAvailableStorage() {
        return availableStorage;
    }

    // Sets the available storage to a new value
    public static void setAvailableStorage(long storage) {
        availableStorage = storage;
    }
    
}

// Advertisement Registry
class AdvertisementRegistry {
    private static final Set<Integer> usedIds = Collections.synchronizedSet(new HashSet<>());// Set to store used advertisement IDs

    // Registers a new advertisement ID, returns true if added successfully
    public static boolean registerAdvertId(int id) {
        return usedIds.add(id);
    }

    // Unregisters an advertisement ID
    public static void unregisterAdvertId(int id) {
        usedIds.remove(id);
    }
}

// Advertiser Class
class Advertiser {
    private final int advertiserID;// Unique ID for the advertiser
    private final String advertiserName;// Name of the advertiser
    private final String contactInformation; // Contact information for the advertiser
    private final String address;// Address of the advertiser
    private final Set<Advertisement> advertisements;// Set of advertisements submitted by the advertiser

    // Constructor to initialize an advertiser's details
    public Advertiser(int advertiserID, String advertiserName, String contactInformation, String address) {
        this.advertiserID = advertiserID;
        this.advertiserName = advertiserName;
        this.contactInformation = contactInformation;
        this.address = address;
        this.advertisements = Collections.synchronizedSet(new HashSet<>());// Thread-safe set for advertisements
    }

    // Method to submit advertiser details and log them
    public void submitDetails() {
        LoggerUtil.getLogger().log(Level.INFO, "Advertiser details submitted: " + advertiserID);// Log the submission
        System.out.println("[Advertiser] Advertisement details submitted to the marketing system.");
        System.out.println("Advertiser ID: " + advertiserID);
        System.out.println("Advertiser Name: " + advertiserName);
        System.out.println("Contact Information: " + contactInformation);
        System.out.println("Address: " + address);
    }

    // Adds a new advertisement to the advertiser's set of advertisements
    public void addAdvertisement(Advertisement ad) {
        advertisements.add(ad);
    }

    // Returns an unmodifiable set of the advertiser's advertisements
    public Set<Advertisement> getAdvertisements() {
        return Collections.unmodifiableSet(advertisements);
    }

     // Returns the advertiser's unique ID
    public int getAdvertiserID() {
        return advertiserID;
    }

    // Returns the advertiser's name
    public String getAdvertiserName() {
        return advertiserName;
    }
}

// Base Employee class
abstract class Employee {
    protected int employeeID; // Employee's unique ID
    protected String employeeName; // Employee's name
    protected static final Logger LOGGER = LoggerUtil.getLogger(); // Logger for employee actions
    protected String contactInformation; // Employee's contact details
    

    // Constructor to initialize employee details
    public Employee(int employeeID, String employeeName, String contactInformation) {
        this.employeeID = employeeID;
        this.employeeName = employeeName;
        this.contactInformation = contactInformation;
    }

    // Getter for employee's name
    public String getEmployeeName() {
        return employeeName;
    }
    
    // Getter for employee's contact information
    public String getContactInformation() {
        return contactInformation;
    }
}

// Advertisement Status Enum
enum AdvertisementStatus {
    PENDING, VALIDATED, APPROVED, REJECTED // Different statuses of an advertisement
}

// Advertisement class
class Advertisement {
    private final int advertID; // Unique advertisement ID
    private final String content; // Content of the advertisement
    private final int size; // Size of the advertisement
    private final String placement; // Placement location for the advertisement
    private final int paymentTerms; // Payment terms for the advertisement
    private AdvertisementStatus status; // Current status of the advertisement
    private final LocalDate publicationDate; // Publication date for the advertisement
    private final Advertiser advertiser; // Advertiser associated with the advertisement

    // Constructor to initialize advertisement details and perform validation
    public Advertisement(int advertID, String content, int size, String placement, 
                        int paymentTerms, LocalDate publicationDate, Advertiser advertiser) {
        // Validate unique ID
        if (!AdvertisementRegistry.registerAdvertId(advertID)) {
            throw new IllegalArgumentException("Advertisement ID must be unique");
        }

        // Validate non-null advertiser
        if (advertiser == null) {
            throw new IllegalArgumentException("Advertiser cannot be null");
        }

        // Validate non-empty content
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Advertisement content cannot be empty");
        }

        // Validate positive size
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be positive");
        }

        // Validate placement
        if (placement == null || placement.trim().isEmpty()) {
            throw new IllegalArgumentException("Placement cannot be null or empty");
        }

        // Validate payment terms
        if (paymentTerms <= 0) {
            throw new IllegalArgumentException("Payment terms must be positive");
        }

        // Validate publication date
        if (publicationDate == null || publicationDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Publication date must be in the future");
        }

        // Set values after validation
        this.advertID = advertID;
        this.content = content;
        this.size = size;
        this.placement = placement;
        this.paymentTerms = paymentTerms;
        this.status = AdvertisementStatus.PENDING; // Default status is PENDING
        this.publicationDate = publicationDate;
        this.advertiser = advertiser;
        
        // Register advertisement with the associated advertiser
        advertiser.addAdvertisement(this);
    }

    // Getter for advertisement ID
    public int getAdvertID() {
        return advertID;
    }

    // Getter for advertisement status
    public AdvertisementStatus getStatus() {
        return status;
    }

    // Setter for advertisement status
    public void setStatus(AdvertisementStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        this.status = status;
    }

    // Getter for associated advertiser
    public Advertiser getAdvertiser() {
        return advertiser;
    }

    // Getter for advertisement size
    public int getSize() {
        return size;
    }

    // Getter for advertisement content
    public String getContent() {
        return content;
    }
}

// Processing Center
class ProcessingCenter {
    private final int processingID; // Unique ID for the processing center
    private final Queue<Advertisement> approvedAdverts = new ConcurrentLinkedQueue<>(); // Queue for approved advertisements
    private final Queue<Advertisement> revisionQueue = new ConcurrentLinkedQueue<>(); // Queue for advertisements needing revision
    private static final Logger LOGGER = LoggerUtil.getLogger(); // Logger for processing center actions

    // Constructor to initialize processing center with ID
    public ProcessingCenter(int processingID) {
        this.processingID = processingID;
    }

    // Method to store advertisement in the appropriate queue
    public synchronized void storeAdvert(Advertisement advert) {
        try {
            if (!SystemState.isOperational()) {
                throw new IllegalStateException("System is not operational");
            }

            if (SystemState.getAvailableStorage() < advert.getSize()) {
                throw new IllegalStateException("Insufficient storage capacity");
            }

            if (advert == null) {
                throw new IllegalArgumentException("Advertisement cannot be null");
            }

            // Store the advertisement in the correct queue based on status
            if (advert.getStatus() == AdvertisementStatus.APPROVED) {
                approvedAdverts.offer(advert);
                SystemState.setAvailableStorage(SystemState.getAvailableStorage() - advert.getSize());
                LOGGER.log(Level.INFO, "Stored approved advert ID: " + advert.getAdvertID());
                System.out.println("[ProcessingCenter] Stored approved advert ID: " + advert.getAdvertID());
            } else if (advert.getStatus() == AdvertisementStatus.REJECTED) {
                revisionQueue.offer(advert);
                LOGGER.log(Level.INFO, "Added rejected advert to revision queue: " + advert.getAdvertID());
                System.out.println("[ProcessingCenter] Added rejected advert to revision queue: " + advert.getAdvertID());
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error storing advertisement: " + e.getMessage());
            System.out.println("[ProcessingCenter] Error: " + e.getMessage());
            throw e;
        }
    }

    // Getter for the revision queue
    public Queue<Advertisement> getRevisionQueue() {
        return revisionQueue;
    }
}

// Command interface
interface AdvertCommand {
    void execute(); // Execute the command
    void undo(); // Undo the command
    String getCommandName(); // Get the name of the command
}

// Base Command class
abstract class BaseAdvertCommand implements AdvertCommand {
    protected static final Logger LOGGER = LoggerUtil.getLogger();
    
    // Execute the command and log its execution
    @Override
    public void execute() {
        LOGGER.log(Level.INFO, "Executing command: " + getCommandName());
        executeCommand();
        LOGGER.log(Level.INFO, "Completed command: " + getCommandName());
    }
    
    // Abstract method for executing the specific command logic
    protected abstract void executeCommand();
}

// Review Advertisement Command
class ReviewAdvertCommand extends BaseAdvertCommand {
    private final Advertisement advert;// Advertisement to be reviewed
    private final Editor editor;// Editor who will review the advertisement
    
    // Constructor to initialize advertisement and editor
    public ReviewAdvertCommand(Advertisement advert, Editor editor) {
        if (advert == null || editor == null) {
            throw new IllegalArgumentException("Advertisement and editor cannot be null");
        }
        this.advert = advert;
        this.editor = editor;
    }
    
    // Executes the review process
    @Override
    protected void executeCommand() {
        if (!SystemState.isOperational()) {
            throw new IllegalStateException("System is not operational");
        }
        editor.reviewAdvertisement(advert);// Let editor review the advert
    }
    
    // Undo the review by resetting the advertisement's status to PENDING
    @Override
    public void undo() {
        advert.setStatus(AdvertisementStatus.PENDING);
        LOGGER.log(Level.INFO, "Undoing review for advert ID: " + advert.getAdvertID());
    }
    
    // Get the name of the command
    @Override
    public String getCommandName() {
        return "ReviewAdvertCommand";
    }
}

// Approve Advertisement Command
class ApproveAdvertCommand extends BaseAdvertCommand {
    private final Advertisement advert; // Advertisement to be approved
    private final ProcessingCenter processingCenter;
    
    // Constructor to initialize advertisement and processing center
    public ApproveAdvertCommand(Advertisement advert, ProcessingCenter processingCenter) {
        if (advert == null || processingCenter == null) {
            throw new IllegalArgumentException("Advertisement and processing center cannot be null");
        }
        this.advert = advert;
        this.processingCenter = processingCenter;
    }
    
    // Approves the advertisement and stores it in the processing center
    @Override
    protected void executeCommand() {
        advert.setStatus(AdvertisementStatus.APPROVED);
        processingCenter.storeAdvert(advert);
    }
    
    // Undo the approval by resetting the advertisement's status to PENDING
    @Override
    public void undo() {
        advert.setStatus(AdvertisementStatus.PENDING);
        LOGGER.log(Level.INFO, "Undoing approval for advert ID: " + advert.getAdvertID());
    }
    
    @Override
    public String getCommandName() {
        return "ApproveAdvertCommand";
    }
}

// Reject Advertisement Command
class RejectAdvertCommand extends BaseAdvertCommand {
    private final Advertisement advert;
    private final MarketingDepartment marketing;
    
    public RejectAdvertCommand(Advertisement advert, MarketingDepartment marketing) {
        if (advert == null || marketing == null) {
            throw new IllegalArgumentException("Advertisement and marketing department cannot be null");
        }
        this.advert = advert;
        this.marketing = marketing;
    }
    
    // Rejects the advertisement and notifies marketing department
    @Override
    protected void executeCommand() {
        advert.setStatus(AdvertisementStatus.REJECTED);
        marketing.notifyRejection(advert);
    }
    
    // Undo the rejection by resetting the advertisement's status to PENDING
    @Override
    public void undo() {
        advert.setStatus(AdvertisementStatus.PENDING);
        LOGGER.log(Level.INFO, "Undoing rejection for advert ID: " + advert.getAdvertID());
    }
    
    @Override
    public String getCommandName() {
        return "RejectAdvertCommand";
    }
}

// Editor class
class Editor extends Employee {
    private final ProcessingCenter processingCenter;
    private final SystemManager systemManager;

    public Editor(int employeeID, String employeeName, String contactInformation, 
                 ProcessingCenter processingCenter, SystemManager systemManager) {
        super(employeeID, employeeName, contactInformation);
        this.processingCenter = processingCenter;
        this.systemManager = systemManager;
    }

    // Reviews the advertisement and sets the next steps based on its status
    public void reviewAdvertisement(Advertisement advert) {
        try {
            LOGGER.log(Level.INFO, "Editor " + employeeName + " reviewing advert ID: " + advert.getAdvertID());
            System.out.println("[Editor " + employeeName + "] Reviewing advert ID: " + advert.getAdvertID());
            
            // Simulating review decision
            if (advert.getStatus() == AdvertisementStatus.VALIDATED) {
                System.out.println("[Editor " + employeeName + "] Advertisement validation confirmed for ID: " + advert.getAdvertID());
                AdvertCommand approveCommand = new ApproveAdvertCommand(advert, processingCenter);
                systemManager.setCommand(approveCommand);
            } else {
                System.out.println("[Editor " + employeeName + "] Advertisement validation failed for ID: " + advert.getAdvertID());
                AdvertCommand rejectCommand = new RejectAdvertCommand(advert, null);
                systemManager.setCommand(rejectCommand);
            }
            
            systemManager.executeCommands();
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in editor review process: " + e.getMessage());
            System.out.println("[Editor " + employeeName + "] Error: Review process failed - " + e.getMessage());
            throw e;
        }
    }
}

// Marketing Department
class MarketingDepartment extends Employee {
    private final SystemManager systemManager;

    public MarketingDepartment(int employeeID, String employeeName, String contactInformation, 
                              SystemManager systemManager) {
        super(employeeID, employeeName, contactInformation);
        this.systemManager = systemManager;
    }

    // Submit advertisement for review by editor
    public void submitAdvertForReview(Advertisement advert, Editor editor) {
        try {
            if (validateAdvertInput(advert)) {
                LOGGER.log(Level.INFO, "Marketing " + employeeName + " submitting validated advert ID: " + advert.getAdvertID());
                System.out.println("[Marketing " + employeeName + "] Submitting validated advert ID: " + advert.getAdvertID() + " for review");
                advert.setStatus(AdvertisementStatus.VALIDATED);
                
                AdvertCommand reviewCommand = new ReviewAdvertCommand(advert, editor);
                systemManager.setCommand(reviewCommand);
                systemManager.executeCommands();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in advertisement submission: " + e.getMessage());
            System.out.println("[Marketing " + employeeName + "] Error: Submission failed - " + e.getMessage());
        }
    }

    // Validate advertisement input
    private boolean validateAdvertInput(Advertisement advert) {
        try {
            if (advert == null) throw new IllegalArgumentException("Advertisement cannot be null");
            if (advert.getAdvertID() <= 0) throw new IllegalArgumentException("Invalid ID");
            if (advert.getContent() == null || advert.getContent().isEmpty()) {
                throw new IllegalArgumentException("Invalid content");
            }
            LOGGER.log(Level.INFO, "Advertisement validation successful for ID: " + advert.getAdvertID());
            System.out.println("[Marketing " + employeeName + "] Advertisement validation successful for ID: " + advert.getAdvertID());
            return true;
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Advertisement validation failed: " + e.getMessage());
            System.out.println("[Marketing " + employeeName + "] Advertisement validation failed: " + e.getMessage());
            return false;
        }
    }

    // Notify about advertisement rejection
    public void notifyRejection(Advertisement advert) {
        try {
            if (advert == null) {
                throw new IllegalArgumentException("Advertisement cannot be null");
            }
            LOGGER.log(Level.WARNING, "Rejection notification sent for advert ID: {0}", advert.getAdvertID());
            System.out.println("[Marketing " + employeeName + "] Notification: Advert ID: " + 
                             advert.getAdvertID() + " was rejected. Please revise and resubmit.");
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "Error in rejection notification: {0}", e.getMessage());
            System.out.println("[Marketing " + employeeName + "] Error: Rejection notification failed - " + e.getMessage());
        }
    }
}

// System Manager class
class SystemManager {
    private final Queue<AdvertCommand> commandQueue = new ConcurrentLinkedQueue<>();// Queue of commands to execute
    private final Deque<AdvertCommand> commandHistory = new ConcurrentLinkedDeque<>();// Stack of executed commands for undo
    private static final Logger LOGGER = LoggerUtil.getLogger();

    // Set a command for execution
    public synchronized void setCommand(AdvertCommand command) {
        try {
            if (command == null) {
                throw new IllegalArgumentException("Command cannot be null");
            }
            commandQueue.offer(command);// Add command to the queue
            LOGGER.log(Level.INFO, "Command added to queue: " + command.getCommandName());
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "Error setting command: " + e.getMessage());
        }
    }

    // Execute all commands in the queue
    public synchronized void executeCommands() {
        try {
            while (!commandQueue.isEmpty()) {
                AdvertCommand command = commandQueue.poll();
                command.execute();
                commandHistory.push(command);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error executing commands: " + e.getMessage());
        }
    }

     // Undo the last executed command
    public synchronized void undoLastCommand() {
        if (!commandHistory.isEmpty()) {
            AdvertCommand command = commandHistory.pop();// Get the last executed command
            command.undo();
            LOGGER.log(Level.INFO, "Undoing command: " + command.getCommandName());
        }
    }
}

// Main Application class
public class App {
    public static void main(String[] args) {
        try {
            // Initialize system components
            SystemManager systemManager = new SystemManager();
            ProcessingCenter processingCenter = new ProcessingCenter(1);
            
            // Create employees
            Editor editor = new Editor(1, "John", "+123-456-7890", processingCenter, systemManager);
            MarketingDepartment marketing = new MarketingDepartment(2, "Jane", "+123-456-4590", systemManager);
            
            // Create advertiser and submit details
            Advertiser advertiser = new Advertiser(1, "ACME Corp", "+123-456-7890", "123 Elm Street");
            advertiser.submitDetails();
            
            // Create test advertisement
            Advertisement advert1 = new Advertisement(
                101,
                "Test Advertisement Content",
                100,
                "Front Page",
                1000,
                LocalDate.now().plusDays(7),
                advertiser
            );
            
            System.out.println("\n--- Processing Advertisement ---");
            marketing.submitAdvertForReview(advert1, editor);
            
        } catch (Exception e) {
            System.err.println("Error in main: " + e.getMessage());
            LoggerUtil.getLogger().log(Level.SEVERE, "Error in main: " + e.getMessage());
        }
    }
}