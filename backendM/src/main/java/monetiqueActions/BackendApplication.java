package monetiqueActions;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import monetiqueActions.dtos.BankAccountDTO;
import monetiqueActions.dtos.CurrentBankAccountDTO;
import monetiqueActions.dtos.CustomerDTO;
import monetiqueActions.dtos.SavingBankAccountDTO;
import monetiqueActions.entities.Role;
import monetiqueActions.entities.User;
import monetiqueActions.enums.ERole;
import monetiqueActions.exceptions.CustomerNotFoundException;
import monetiqueActions.repositories.AccountOperationRepository;
import monetiqueActions.repositories.BankAccountRepository;
import monetiqueActions.repositories.CustomerRepository;
import monetiqueActions.repositories.RoleRepository;
import monetiqueActions.repositories.UserRepository;
import monetiqueActions.services.BankAccountService;

import java.util.*;
import java.util.stream.Stream;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
    /*
    @Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService){
        return args -> {
           Stream.of("Hassan","Imane","Mohamed").forEach(name->{
               CustomerDTO customer=new CustomerDTO();
               customer.setName(name);
               customer.setEmail(name+"@gmail.com");
               bankAccountService.saveCustomer(customer);
           });
           bankAccountService.listCustomers().forEach(customer->{
               try {
                   bankAccountService.saveCurrentBankAccount(Math.random()*90000,9000,customer.getId());
                   bankAccountService.saveSavingBankAccount(Math.random()*120000,5.5,customer.getId());

               } catch (CustomerNotFoundException e) {
                   e.printStackTrace();
               }
           });
            List<BankAccountDTO> bankAccounts = bankAccountService.bankAccountList();
            for (BankAccountDTO bankAccount:bankAccounts){
                for (int i = 0; i <10 ; i++) {
                    String accountId;
                    if(bankAccount instanceof SavingBankAccountDTO){
                        accountId=((SavingBankAccountDTO) bankAccount).getId();
                    } else{
                        accountId=((CurrentBankAccountDTO) bankAccount).getId();
                    }
                    bankAccountService.credit(accountId,10000+Math.random()*120000,"Credit");
                    bankAccountService.debit(accountId,1000+Math.random()*9000,"Debit");
                }
            }
        };
    }
    @Bean
    CommandLineRunner start(CustomerRepository customerRepository,
                            BankAccountRepository bankAccountRepository,
                            AccountOperationRepository accountOperationRepository,
                            RoleRepository roleRepository,
                            UserRepository userRepository){
        return args -> {
            Role role = new Role();
            role.setName(ERole.ROLE_ADMIN);
            roleRepository.save(role);

            Set<Role> roles = new HashSet<>();
            roles.add(role);

            User user = new User();
            user.setEmail("benomar@gmail.com");
            user.setUsername("benomar");
            user.setRoles(roles);

            String password = "benomar";
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String encodedPassword = encoder.encode(password);

            user.setPassword(encodedPassword);

            userRepository.save(user);
        };
    }*/
}
