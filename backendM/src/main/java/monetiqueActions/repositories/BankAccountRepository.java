package monetiqueActions.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import monetiqueActions.entities.BankAccount;

public interface BankAccountRepository extends JpaRepository<BankAccount,String> {
}
