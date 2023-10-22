package com.smallworld;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.smallworld.data.Transaction;

public class TransactionDataFetcherTest {

      private static Transaction[] loadTransactionsFromJson(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            return gson.fromJson(reader, Transaction[].class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

      // Load JSON data into an array of Transaction objects
      Transaction[] transactionsArray = loadTransactionsFromJson("D:\\java\\test\\coding_test_v3 (1)(1)\\coding_test\\transactions.json");

      // Convert the array to a List for convenience
      List<Transaction> transactions = Arrays.asList(transactionsArray);

      // Create a TransactionDataFetcher instance
      TransactionDataFetcher dataFetcher = new TransactionDataFetcher(transactions);

    
    // Unit test for the getTotalTransactionAmount method in the TransactionDataFetcher class. 
    @Test
    public void testGetTotalTransactionAmount() throws IOException {
        // Test the getTotalTransactionAmount method
        double expectedTotalAmount = 2889.17 ; // Calculated based on the provided data
        double actualTotalAmount = dataFetcher.getTotalTransactionAmount();

        // Assert that the actual total amount matches the expected total amount
        assertEquals(expectedTotalAmount, actualTotalAmount, 0.01); // Using a delta to account for possible floating-point errors
    }

    // Unit test for the getTotalTransactionAmountSentBy method in the TransactionDataFetcher class.
    @Test
    public void testGetTotalTransactionAmountSentBy() {
    
        // Test the getTotalTransactionAmountSentBy method
        double totalAmountSentBySender1 = dataFetcher.getTotalTransactionAmountSentBy("Billy Kimber");
        double totalAmountSentBySender2 = dataFetcher.getTotalTransactionAmountSentBy("Grace Burgess");

        // Assert the results
        assertEquals(459.09, totalAmountSentBySender1, 0.01); // Using a delta for possible floating-point errors
        assertEquals(666.0, totalAmountSentBySender2, 0.01);
    }

    // Unit test for the getMaxTransactionAmount method in the TransactionDataFetcher class.
    @Test
    public void testGetMaxTransactionAmount() {
    
        // Test the getMaxTransactionAmount method
        double maxTransactionAmount = dataFetcher.getMaxTransactionAmount();

        // Assert the result
        assertEquals(985.0, maxTransactionAmount, 0.01); // Using a delta for possible floating-point errors
    }

    // Unit test for the countUniqueClients method in the TransactionDataFetcher class.
    @Test
    public void testCountUniqueClients() {
        // Test the countUniqueClients method
        long uniqueClientCount = dataFetcher.countUniqueClients();

        // Assert the result
        assertEquals(14, uniqueClientCount);
    }

    // Unit test for the hasOpenComplianceIssues method in the TransactionDataFetcher class.
    @Test
    public void testHasOpenComplianceIssues() {
    
        // Test the hasOpenComplianceIssues method
        boolean hasOpenComplianceIssues1 = dataFetcher.hasOpenComplianceIssues("Aunt Polly");
        boolean hasOpenComplianceIssues2 = dataFetcher.hasOpenComplianceIssues("Aberama Gold");
        boolean hasOpenComplianceIssues3 = dataFetcher.hasOpenComplianceIssues("Tom Shelby");

        // Assert the results
        assertFalse(hasOpenComplianceIssues1);
        assertFalse(hasOpenComplianceIssues2);
        assertTrue(hasOpenComplianceIssues3);
    }

    // Unit test for the getTransactionsByBeneficiaryName method in the TransactionDataFetcher class.
    @Test
    public void testGetTransactionsByBeneficiaryName() {
    
        // Test the getTransactionsByBeneficiaryName method
        Map<String, Transaction> transactionsByBeneficiary = dataFetcher.getTransactionsByBeneficiaryName();

        // Assert the results
        assertEquals(10, transactionsByBeneficiary.size()); 
        assertTrue(transactionsByBeneficiary.containsKey("Alfie Solomons"));
        assertTrue(transactionsByBeneficiary.containsKey("Arthur Shelby"));
        assertTrue(transactionsByBeneficiary.containsKey("Aberama Gold"));

        // Check if the values are correct
        assertEquals(transactions.get(0), transactionsByBeneficiary.get("Alfie Solomons"));
        assertEquals(transactions.get(1), transactionsByBeneficiary.get("Arthur Shelby"));
    }

    // Unit test for the getUnsolvedIssueIds method in the TransactionDataFetcher class.
    @Test
    public void testGetUnsolvedIssueIds() {

        // Test the getUnsolvedIssueIds method
        Set<Integer> unsolvedIssueIds = dataFetcher.getUnsolvedIssueIds();

        // Assert the results
        assertEquals(5, unsolvedIssueIds.size()); 
        assertFalse(unsolvedIssueIds.contains(2));
        assertTrue(unsolvedIssueIds.contains(3));
        assertTrue(unsolvedIssueIds.contains(1)); 
    }

    // Unit test for the getAllSolvedIssueMessages method in the TransactionDataFetcher class.
    @Test
    public void testGetAllSolvedIssueMessages() {
  
        // Test the getAllSolvedIssueMessages method
        List<String> solvedIssueMessages = dataFetcher.getAllSolvedIssueMessages();

        // Assert the results
        assertEquals(8, solvedIssueMessages.size()); 
        assertTrue(solvedIssueMessages.contains("Never gonna give you up"));
        assertFalse(solvedIssueMessages.contains("Something's fishy"));
        assertFalse(solvedIssueMessages.contains("Don't let this transaction happen"));
    }

    // Unit test for the getTop3TransactionsByAmount method in the TransactionDataFetcher class.
    @Test
    public void testGetTop3TransactionsByAmount() {
        
        // Test the getTop3TransactionsByAmount method
        List<Transaction> top3Transactions = dataFetcher.getTop3TransactionsByAmount();

        // Assert the results
        assertEquals(3, top3Transactions.size());
        assertEquals(transactions.get(4), top3Transactions.get(0)); 
        assertEquals(transactions.get(7), top3Transactions.get(1));
        assertEquals(transactions.get(7), top3Transactions.get(2));
    }


    // Unit test for the getTopSender method in the TransactionDataFetcher class.
    @Test
    public void testGetTopSender() {
        // Test the getTopSender method
        Optional<String> topSender = dataFetcher.getTopSender();

        // Assert the results
        assertTrue(topSender.isPresent());
        assertEquals("Grace Burgess", topSender.get()); // Grace Burgess has the most total sent amount
    }
    


}
