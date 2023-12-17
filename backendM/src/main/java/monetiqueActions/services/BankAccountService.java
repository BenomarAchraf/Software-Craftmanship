package monetiqueActions.services;

import java.util.List;

import monetiqueActions.dtos.AccountHistoryDTO;
import monetiqueActions.dtos.AccountOperationDTO;
import monetiqueActions.dtos.BankAccountDTO;
import monetiqueActions.dtos.CurrentBankAccountDTO;
import monetiqueActions.dtos.CustomerDTO;
import monetiqueActions.dtos.SavingBankAccountDTO;
import monetiqueActions.exceptions.BalanceNotSufficientException;
import monetiqueActions.exceptions.BankAccountNotFoundException;
import monetiqueActions.exceptions.CustomerNotFoundException;


public interface BankAccountService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
    SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
    List<CustomerDTO> listCustomers();
    Object getBankAccount(String accountId) throws BankAccountNotFoundException;
    void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;
    void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;

    List<BankAccountDTO> bankAccountList();

    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long customerId);

    List<AccountOperationDTO> accountHistory(String accountId);

    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;

    List<CustomerDTO> searchCustomers(String keyword);
}
