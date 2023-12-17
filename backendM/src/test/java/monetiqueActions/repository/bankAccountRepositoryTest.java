package monetiqueActions.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import monetiqueActions.entities.BankAccount;
import monetiqueActions.entities.CurrentAccount;
import monetiqueActions.entities.SavingAccount;
import monetiqueActions.enums.AccountStatus;
import monetiqueActions.repositories.BankAccountRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class bankAccountRepositoryTest {
	@Autowired
	private BankAccountRepository bankAccountRepository;
	
	private BankAccount achrafAccount;
	private BankAccount benomarAccount;
	
	@BeforeEach
	void init() {
		achrafAccount = new SavingAccount();
		achrafAccount.setCustomer(null);
		achrafAccount.setBalance(5000);
		achrafAccount.setCreatedAt(new Date());
		achrafAccount.setStatus(AccountStatus.CREATED);
		achrafAccount.setId(UUID.randomUUID().toString());
		
		benomarAccount = new CurrentAccount();
		benomarAccount.setCustomer(null);
		benomarAccount.setBalance(5000);
		benomarAccount.setCreatedAt(new Date());
		benomarAccount.setStatus(AccountStatus.CREATED);
		benomarAccount.setId(UUID.randomUUID().toString());
		
	}
	
	
	@Test
	@DisplayName("It should save the Account to the bank database")
	void save() {
		BankAccount account = bankAccountRepository.save(achrafAccount);
		assertNotNull(account);
		assertThat(account.getId()).isNotEqualTo(null);
	}
	

	
	@Test
	@DisplayName("It should return the Bank Account list with size of 2")
	void getAll() {
		bankAccountRepository.save(benomarAccount);
		bankAccountRepository.save(achrafAccount);
		
		List<BankAccount> list = bankAccountRepository.findAll();
		
		assertNotNull(list);
		assertThat(list).isNotNull();
		assertEquals(2, list.size());
	}
	
	@Test
	@DisplayName("It should return the bank account by its id")
	void getById() {
		bankAccountRepository.save(achrafAccount);
		
		BankAccount account = bankAccountRepository.findById(achrafAccount.getId()).get();
		
		assertNotNull(account);
		assertEquals(5000.0, account.getBalance());
		assertThat(account.getCreatedAt()).isBefore(new Date(123,11,17));
	}
	
	@Test
	@DisplayName("It should update the BankAccount Status")
	void update() {
		
		bankAccountRepository.save(achrafAccount);
		
		BankAccount account = bankAccountRepository.findById(achrafAccount.getId()).get();
		account.setStatus(AccountStatus.ACTIVATED);
		BankAccount updatedaccount = bankAccountRepository.save(account);
		
		assertEquals(AccountStatus.ACTIVATED, updatedaccount.getStatus());
	}
	
	@Test
	@DisplayName("It should delete the BankAccount")
	void deleteMovie() {
		
		bankAccountRepository.save(achrafAccount);
		String id = achrafAccount.getId();
		
		bankAccountRepository.save(benomarAccount);
		
		bankAccountRepository.delete(achrafAccount);
		
		List<BankAccount> list = bankAccountRepository.findAll();
		
		Optional<BankAccount> exitingAccount = bankAccountRepository.findById(id);
		
		assertEquals(1, list.size());
		assertThat(exitingAccount).isEmpty();
		
	}
	
	
	
	
}
