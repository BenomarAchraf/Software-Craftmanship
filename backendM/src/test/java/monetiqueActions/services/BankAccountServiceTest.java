package monetiqueActions.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import monetiqueActions.dtos.AccountHistoryDTO;
import monetiqueActions.dtos.AccountOperationDTO;
import monetiqueActions.dtos.BankAccountDTO;
import monetiqueActions.dtos.CurrentBankAccountDTO;
import monetiqueActions.dtos.CustomerDTO;
import monetiqueActions.dtos.SavingBankAccountDTO;
import monetiqueActions.entities.AccountOperation;
import monetiqueActions.entities.BankAccount;
import monetiqueActions.entities.CurrentAccount;
import monetiqueActions.entities.Customer;
import monetiqueActions.entities.SavingAccount;
import monetiqueActions.enums.AccountStatus;
import monetiqueActions.enums.OperationType;
import monetiqueActions.exceptions.BalanceNotSufficientException;
import monetiqueActions.exceptions.BankAccountNotFoundException;
import monetiqueActions.exceptions.CustomerNotFoundException;
import monetiqueActions.mappers.BankAccountMapperImpl;
import monetiqueActions.repositories.AccountOperationRepository;
import monetiqueActions.repositories.BankAccountRepository;
import monetiqueActions.repositories.CustomerRepository;

@ExtendWith(MockitoExtension.class)
public class BankAccountServiceTest {
	
	@Mock
	private BankAccountRepository bankAccountRepository;
	@Mock
    private CustomerRepository customerRepository;
	@Mock
    private AccountOperationRepository accountOperationRepository;
	@Mock
    private BankAccountMapperImpl dtoMapper;
	
	
	@InjectMocks
	private BankAccountServiceImpl bankAccountServiceImpl;
	
	
	private CustomerDTO customerDTO;
	private CustomerDTO customerDTO1;
	private Customer customer;
	private Customer customer1;
	private CurrentAccount currentAccount;
	private CurrentBankAccountDTO currentBankAccountDTO;
	private SavingAccount savingAccount;
	private SavingBankAccountDTO savingBankAccountDTO;
	private AccountOperation debitaccountOperation;
	private AccountOperationDTO debitaccountOperationDTO;
	private AccountOperationDTO creditaccountOperationDTO;
	private AccountOperation creditaccountOperation;
	private BankAccount bankAccount;
	private AccountHistoryDTO accountHistoryDTO;

	@BeforeEach
	void init() {
		currentAccount = new CurrentAccount();
		currentAccount.setBalance(5000);
		currentAccount.setCreatedAt(new Date());
		currentAccount.setStatus(AccountStatus.CREATED);
		currentAccount.setId(UUID.randomUUID().toString());
		
		savingAccount = new SavingAccount();
		savingAccount.setBalance(5000);
		savingAccount.setCreatedAt(new Date());
		savingAccount.setStatus(AccountStatus.CREATED);
		savingAccount.setId(UUID.randomUUID().toString());
		
		customer=new Customer();
		customer.setId(1L);
		customer.setEmail("test@test.com");
		customer.setName("Test");
		
		customer1=new Customer();
		customer1.setId(2L);
		customer1.setEmail("test1@test.com");
		customer1.setName("Test1");
		
		customerDTO=new CustomerDTO();
		customerDTO.setId(1L);
		customerDTO.setEmail("test@test.com");
		customerDTO.setName("Test");
		
		customerDTO1=new CustomerDTO();
		customerDTO1.setId(2L);
		customerDTO1.setEmail("test1@test.com");
		customerDTO1.setName("Test1");
		
		currentBankAccountDTO= new CurrentBankAccountDTO();
		currentBankAccountDTO.setBalance(5000);
		currentBankAccountDTO.setId(UUID.randomUUID().toString());
		
		savingBankAccountDTO=new SavingBankAccountDTO();
		savingBankAccountDTO.setBalance(5000);
		savingBankAccountDTO.setId(UUID.randomUUID().toString());
		
		debitaccountOperation=new AccountOperation();
		debitaccountOperation.setType(OperationType.DEBIT);
		debitaccountOperation.setAmount(50);
		debitaccountOperation.setDescription("fqsqs");
		debitaccountOperation.setOperationDate(new Date());
		
		debitaccountOperationDTO=new AccountOperationDTO();
		debitaccountOperationDTO.setType(OperationType.DEBIT);
		debitaccountOperationDTO.setAmount(50);
		debitaccountOperationDTO.setDescription("fqsqs");
		debitaccountOperationDTO.setOperationDate(new Date());
		
		creditaccountOperation=new AccountOperation();
		creditaccountOperation.setType(OperationType.CREDIT);
		creditaccountOperation.setAmount(50);
		creditaccountOperation.setDescription("fqsqs");
		creditaccountOperation.setOperationDate(new Date());
		
		creditaccountOperationDTO=new AccountOperationDTO();
		creditaccountOperationDTO.setType(OperationType.CREDIT);
		creditaccountOperationDTO.setAmount(50);
		creditaccountOperationDTO.setDescription("fqsqs");
		creditaccountOperationDTO.setOperationDate(new Date());
		
		bankAccount = new BankAccount() {};
		bankAccount.setBalance(5000);
		
		accountHistoryDTO = new AccountHistoryDTO();
		accountHistoryDTO.setAccountId("5");
	}
	

	@Test
	@DisplayName("It should save the Customer to the bank database")
	void saveCustomer(){
		when(customerRepository.save(any(Customer.class))).thenReturn(customer);
		when(dtoMapper.fromCustomer(customer)).thenReturn(customerDTO);
		when(dtoMapper.fromCustomerDTO(customerDTO)).thenReturn(customer);
		
		CustomerDTO result=bankAccountServiceImpl.saveCustomer(customerDTO);
		assertNotNull(result);
		assertThat(result.getName()).isEqualTo("Test");
	}
	
	@Test
	@DisplayName("It should save the Current Bank Account into the bank database")
	void saveCurrentBankAccountwithoutException() throws CustomerNotFoundException {
		
		when(customerRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(customer));
		when(bankAccountRepository.save(any(CurrentAccount.class))).thenReturn(currentAccount);
		when(dtoMapper.fromCurrentBankAccount(currentAccount)).thenReturn(currentBankAccountDTO);
		
		CurrentBankAccountDTO result = bankAccountServiceImpl.saveCurrentBankAccount(5000.0, 5.0, 1L);
		
		assertNotNull(result);
		assertThat(result.getBalance()).isEqualTo(5000);
	}
	
	@Test
	@DisplayName("It should throw an exception while saving the Current Bank Account into the bank database")
	void saveeCurrentBankAccountwithException() {
		
		when(customerRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(null));
		assertThrows(CustomerNotFoundException.class, () -> {
			bankAccountServiceImpl.saveCurrentBankAccount(5000.0, 5.0, 1L);
        });
		
		
	}
	
	@Test
	@DisplayName("It should save the Saving Bank Account into the bank database")
	void saveSavingBankAccountwithoutException() throws CustomerNotFoundException {
		
		when(customerRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(customer));
		when(bankAccountRepository.save(any(SavingAccount.class))).thenReturn(savingAccount);
		when(dtoMapper.fromSavingBankAccount(savingAccount)).thenReturn(savingBankAccountDTO);
		
		SavingBankAccountDTO result = bankAccountServiceImpl.saveSavingBankAccount(5000.0, 5.0, 1L);
		
		assertNotNull(result);
		assertThat(result.getBalance()).isEqualTo(5000);
	}
	
	@Test
	@DisplayName("It should throw an exception while saving the Saving Bank Account into the bank database")
	void saveSavingBankAccountwithException() {
		
		when(customerRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(null));
		assertThrows(CustomerNotFoundException.class, () -> {
			bankAccountServiceImpl.saveSavingBankAccount(5000.0, 5.0, 1L);
        });
		
		
	}
	
	@Test
	@DisplayName("It should get All Customers from the bank database")
	void getlistCustomers() {
		List<Customer> customers=new ArrayList();
		customers.add(customer);
		customers.add(customer1);
		List<CustomerDTO> customerDTOS =new ArrayList();
		customerDTOS.add(customerDTO);
		customerDTOS.add(customerDTO1);
		
		when(customerRepository.findAll()).thenReturn(customers);
		when(dtoMapper.fromCustomer(customer)).thenReturn(customerDTO);
		when(dtoMapper.fromCustomer(customer1)).thenReturn(customerDTO1);
		
		List<CustomerDTO> results=bankAccountServiceImpl.listCustomers();
		
		for(int i=0;i<results.size();i++) {
			assertEquals(results.get(i),customerDTOS.get(i) );
		}
		
	}
	
	@Test
	@DisplayName("It should get Saving bank Account from the bank database")
	void getSavingBankAccount() {
		when(bankAccountRepository.findById(any(String.class))).thenReturn(Optional.ofNullable(savingAccount));
		when(dtoMapper.fromSavingBankAccount(savingAccount)).thenReturn(savingBankAccountDTO);
		BankAccountDTO result=bankAccountServiceImpl.getBankAccount("6");
		assertEquals(result, savingBankAccountDTO);
	}
	
	@Test
	@DisplayName("It should get Current bank Account from the bank database")
	void getCurrentBankAccount() {
		when(bankAccountRepository.findById(any(String.class))).thenReturn(Optional.ofNullable(currentAccount));
		when(dtoMapper.fromCurrentBankAccount(currentAccount)).thenReturn(currentBankAccountDTO);
		BankAccountDTO result=bankAccountServiceImpl.getBankAccount("6");
		assertEquals(result, currentBankAccountDTO);
	}
	
	@Test
	@DisplayName("It should test the debit Action")
	void debit() throws BankAccountNotFoundException, BalanceNotSufficientException {
		when(bankAccountRepository.findById(any(String.class))).thenReturn(Optional.ofNullable(bankAccount));
		when(accountOperationRepository.save(any(AccountOperation.class))).thenReturn(debitaccountOperation);
		when(bankAccountRepository.save(bankAccount)).thenReturn(bankAccount);
		bankAccountServiceImpl.debit("1", 1, "1");
		assertEquals(4999.0,bankAccount.getBalance());
	}
	
	@Test
	@DisplayName("It should test the credit Action")
	void credit() throws BankAccountNotFoundException, BalanceNotSufficientException {
		when(bankAccountRepository.findById(any(String.class))).thenReturn(Optional.ofNullable(bankAccount));
		when(accountOperationRepository.save(any(AccountOperation.class))).thenReturn(creditaccountOperation);
		when(bankAccountRepository.save(bankAccount)).thenReturn(bankAccount);
		bankAccountServiceImpl.credit("1", 1, "1");
		assertEquals(5001,bankAccount.getBalance());
	}
	
	@Test
	@DisplayName("It should return the bank Account List")
	void bankAccountList() {
		List<BankAccount> bankAccounts=new ArrayList();
		bankAccounts.add(currentAccount);
		bankAccounts.add(savingAccount);
		List<BankAccountDTO> bankAccountDTOS =new ArrayList();
		bankAccountDTOS.add(currentBankAccountDTO);
		bankAccountDTOS.add(savingBankAccountDTO);
		
		when(bankAccountRepository.findAll()).thenReturn(bankAccounts);
		when(dtoMapper.fromSavingBankAccount(savingAccount)).thenReturn(savingBankAccountDTO);
		when(dtoMapper.fromCurrentBankAccount(currentAccount)).thenReturn(currentBankAccountDTO);
		
		List<BankAccountDTO> results=bankAccountServiceImpl.bankAccountList();
		
		for(int i=0;i<results.size();i++) {
			assertEquals(results.get(i),bankAccountDTOS.get(i) );
		}
		
	}
	
	@Test
	@DisplayName("It should return the customer based on his id")
	void getCustomer() throws CustomerNotFoundException {
		when(customerRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(customer));
		when(dtoMapper.fromCustomer(customer)).thenReturn(customerDTO);
		assertEquals(bankAccountServiceImpl.getCustomer(1L),customerDTO);
		
	}
	
	@Test
	@DisplayName("It should return the operarion History of a specific account")
	void accountHistory() {
		List<AccountOperation> accountOperations=new ArrayList();
		accountOperations.add(creditaccountOperation);
		accountOperations.add(debitaccountOperation);
		
		List<AccountOperationDTO> accountOperationsDTO=new ArrayList();
		accountOperationsDTO.add(creditaccountOperationDTO);
		accountOperationsDTO.add(debitaccountOperationDTO);
		
		when(accountOperationRepository.findByBankAccountId(any(String.class))).thenReturn(accountOperations);
		when(dtoMapper.fromAccountOperation(creditaccountOperation)).thenReturn(creditaccountOperationDTO);
		when(dtoMapper.fromAccountOperation(debitaccountOperation)).thenReturn(debitaccountOperationDTO);
		
		List<AccountOperationDTO> results=bankAccountServiceImpl.accountHistory("5");
		for(int i=0;i<results.size();i++) {
			assertEquals(results.get(i),accountOperationsDTO.get(i) );
		}
		
	}
	
	
	
	
	
	
	
}
