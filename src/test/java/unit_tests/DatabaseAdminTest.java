package unit_tests;

import database.Database;
import domain.enums.TransactionStatus;
import domain.transactions.Transaction;
import domain.users.DatabaseAdministratorAccount;
import domain.enums.UserRole;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DatabaseAdministratorAccount.
 */
public class DatabaseAdminTest {

    @Test
    void testConstructorAndGetters() {
        // Arrange
        String adminId = "A1";
        String username = "adminUser";
        String firstName = "Alice";
        String lastName = "Admin";
        String passwordHash = "hash-value";

        // Act
        DatabaseAdministratorAccount admin =
                new DatabaseAdministratorAccount(adminId, username, firstName, lastName, passwordHash);

        // Assert
        assertEquals(adminId, admin.getAdminID());
        assertEquals(username, admin.getUsername());
        assertEquals(firstName, admin.getFirstname());
        assertEquals(lastName, admin.getLastname());
        assertEquals(passwordHash, admin.getPasswordHash());
        assertEquals(UserRole.ADMIN, admin.getRole());
    }

    @Test
    void testReverseTransactionSetsStatusToReversed() {
        // Arrange
        Database mockDb = mock(Database.class);

        Transaction transaction = new Transaction(
                "TRX-1",
                "ACC-1",
                "ACC-2",
                null,
                null,
                100.0,
                "TRANSFER",
                LocalDateTime.now(),
                TransactionStatus.COMPLETED
        );

        when(mockDb.retrieveTransaction("TRX-1")).thenReturn(transaction);

        DatabaseAdministratorAccount admin =
                new DatabaseAdministratorAccount("A1", "admin", "First", "Last", "hash", mockDb);

        // Act
        admin.reverseTransactions("TRX-1");

        // Assert
        assertEquals(TransactionStatus.REVERSED, transaction.getStatus());
    }

}
