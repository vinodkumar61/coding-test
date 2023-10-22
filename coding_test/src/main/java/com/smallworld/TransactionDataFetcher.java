package com.smallworld;

import com.google.gson.Gson;
import com.smallworld.data.Transaction;

import java.io.FileReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class TransactionDataFetcher {
	
	 private List<Transaction> transactions;

	    public TransactionDataFetcher(List<Transaction> transactions) {
	        this.transactions = transactions;
	    }

    /**
     * Returns the sum of the amounts of all transactions.
     * 
     * This method calculates the total sum of transaction amounts, considering each transaction
     * uniquely based on its "mtn" (unique identifier). Duplicate transactions with the same "mtn"
     * are excluded from the sum to ensure accurate representation.
     * 
     * @return The total sum of amounts for all unique transactions.
    */
    public double getTotalTransactionAmount() {
         Set<String> uniqueMtns = new HashSet<>();
        return transactions.stream()
                .filter(transaction -> uniqueMtns.add(transaction.getMtn()))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    /**
    * Returns the sum of the amounts of all transactions sent by the specified client.
    * 
    * This method calculates the total sum of transaction amounts where the given
    * sender's full name matches the sender's full name in each transaction. Each
    * transaction is considered uniquely based on its "mtn" (unique identifier), and
    * duplicate transactions with the same "mtn" are excluded to avoid double-counting.
    * 
    * @param senderFullName The full name of the client whose sent transactions are considered.
    * @return The total sum of amounts for all unique transactions sent by the specified client.
   */
    public double getTotalTransactionAmountSentBy(String senderFullName) {
        Set<String> uniqueTransactions = new HashSet<>();
        return transactions.stream()
                .filter(transaction -> senderFullName.equals(transaction.getSenderFullName()) && uniqueTransactions.add(transaction.getMtn()))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    /**
     * Returns the highest transaction amount.
     * 
     * This method finds and returns the highest (maximum) transaction amount among
     * all transactions. If there are no transactions, it returns a default value of 0.0.
     * 
     * @return The highest transaction amount or 0.0 if there are no transactions.
    */
    public double getMaxTransactionAmount() {
        return transactions.stream()
                .max(Comparator.comparingDouble(Transaction::getAmount))
                .map(Transaction::getAmount)
                .orElse(0.0); // Default value if there are no transactions
    }

    /**
     * Counts the number of unique clients that either sent or received a transaction.
     * 
     * This method combines the sender and beneficiary names from all transactions,
     * ensuring uniqueness, and then returns the count of distinct client names.
     * 
     * @return The count of unique clients involved in transactions.
    */
    public long countUniqueClients() {
         Stream<String> senderNames = transactions.stream().map(Transaction::getSenderFullName);
         Stream<String> beneficiaryNames = transactions.stream().map(Transaction::getBeneficiaryFullName);

        return Stream.concat(senderNames, beneficiaryNames)
                .distinct()
                .count();
    }

    /**
     * Checks whether a client (either sender or beneficiary) has at least one transaction
     * with a compliance issue that has not been solved.
     * 
     * @param clientFullName The full name of the client to check for compliance issues.
     * @return True if the client has at least one unresolved compliance issue, false otherwise.
    */
    public boolean hasOpenComplianceIssues(String clientFullName) {
        return transactions.stream()
        .anyMatch(transaction ->
                (clientFullName.equals(transaction.getSenderFullName()) || clientFullName.equals(transaction.getBeneficiaryFullName()))
                        && transaction.isIssueSolved() == false);
    }

    /**
     * Returns a map of transactions indexed by beneficiary name.
     * Each entry in the map represents a transaction where the beneficiary name is the key.
     * In case of duplicate beneficiary names, the existing transaction is retained.
     * 
     * @return A map where keys are beneficiary names and values are corresponding transactions.
    */
    public Map<String, Transaction> getTransactionsByBeneficiaryName() {
         return transactions.stream()
                .collect(Collectors.toMap(Transaction::getBeneficiaryFullName, transaction -> transaction, (existing, replacement) -> existing));
    }

    /**
     * Returns a set of unique identifiers for all open compliance issues.
     * An issue is considered open if its corresponding transaction has not been solved.
     * 
     * @return A set containing the identifiers of all open compliance issues.
    */
    public Set<Integer> getUnsolvedIssueIds() {
        return transactions.stream()
        .filter(transaction -> !transaction.isIssueSolved())
        .map(Transaction::getIssueId)
        .collect(Collectors.toSet());
    }

    /**
     * Returns a list of messages associated with all solved compliance issues.
     * Only transactions marked as 'solved' are considered, and their corresponding issue messages are included in the list.
     * 
     * @return A list containing messages of all solved compliance issues.
    */
    public List<String> getAllSolvedIssueMessages() {
        return transactions.stream()
                .filter(Transaction::isIssueSolved)
                .map(Transaction::getIssueMessage)
                .collect(Collectors.toList());
    }

    /**
     * Returns a list of the top 3 transactions with the highest amounts, sorted in descending order.
     * If there are fewer than 3 transactions, it returns the available transactions.
     * 
     * @return A list containing the top 3 transactions by amount.
    */
    public List<Transaction> getTop3TransactionsByAmount() {
        return transactions.stream()
                .sorted(Comparator.comparingDouble(Transaction::getAmount).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }

    /**
     * Returns the senderFullName of the sender with the highest total sent amount.
     * If multiple senders have the same highest total amount, it returns any one of them.
     * If there are no transactions, it returns an empty Optional.
     * 
     * @return An Optional containing the senderFullName of the top sender, or an empty Optional if there are no transactions.
     */
    public Optional<String> getTopSender() {
          Map<String, Double> totalSentAmountBySender = new HashMap<>();

        for (Transaction transaction : transactions) {
            totalSentAmountBySender.merge(transaction.getSenderFullName(), transaction.getAmount(), Double::sum);
        }

        Optional<Map.Entry<String, Double>> maxEntry = totalSentAmountBySender.entrySet().stream()
                .max(Map.Entry.comparingByValue());

        return maxEntry.map(Map.Entry::getKey);
    }
    
 
    private static Transaction[] loadTransactionsFromJson(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            return gson.fromJson(reader, Transaction[].class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void main(String[] args) {
  	  // Load JSON data into an array of Transaction objects
      Transaction[] transactionsArray = loadTransactionsFromJson("D:\\java\\test\\coding_test_v3 (1)(1)\\coding_test\\transactions.json");

      // Convert the array to a List for convenience
      List<Transaction> transactions = Arrays.asList(transactionsArray);

      // Create a TransactionDataFetcher instance
      TransactionDataFetcher dataFetcher = new TransactionDataFetcher(transactions);

      
      System.out.println("Total Transaction Amount: " + dataFetcher.getTotalTransactionAmount());
      System.out.println("Sum of Amounts of all transactions sent by the specified client: " + dataFetcher.getTotalTransactionAmountSentBy("Tom Shelby"));
      System.out.println("Max Transaction Amount: " + dataFetcher.getMaxTransactionAmount());
      System.out.println("Counts the number of unique clients that sent or received a transaction: " + dataFetcher.countUniqueClients());
      System.out.println("sender or beneficiary) has at least one transaction with a compliance issue that has not been solved: " + dataFetcher.hasOpenComplianceIssues("Tom Shelby"));
     
      
      Map<String, Transaction> transactionsByBeneficiary = dataFetcher.getTransactionsByBeneficiaryName();

      System.out.println("Transactions indexed by beneficiary name:");
      for (Map.Entry<String, Transaction> entry : transactionsByBeneficiary.entrySet()) {
          System.out.println("Beneficiary Name: " + entry.getKey());
          System.out.println("Transaction: " + entry.getValue());
          System.out.println("---");
      }

      System.out.println("Returns the identifiers of all open compliance issues: " + dataFetcher.getUnsolvedIssueIds()); 
      System.out.println("Returns a list of all solved issue messages: " + dataFetcher.getAllSolvedIssueMessages()); 
      System.out.println("Returns the 3 transactions with the highest amount sorted by amount descending:");

      List<Transaction> top3Transactions = dataFetcher.getTop3TransactionsByAmount();

        for (Transaction transaction : top3Transactions) {
            System.out.println("Transaction: " + transaction.toString());
        }

      System.out.println("Returns the senderFullName of the sender with the most total sent amount: " + dataFetcher.getTopSender()); 
  }

}
