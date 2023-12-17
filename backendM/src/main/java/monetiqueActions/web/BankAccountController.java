package monetiqueActions.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import monetiqueActions.dtos.AccountHistoryDTO;
import monetiqueActions.dtos.AccountOperationDTO;
import monetiqueActions.dtos.BankAccountDTO;
import monetiqueActions.dtos.CreditDTO;
import monetiqueActions.dtos.DebitDTO;
import monetiqueActions.dtos.TransferRequestDTO;
import monetiqueActions.exceptions.BalanceNotSufficientException;
import monetiqueActions.exceptions.BankAccountNotFoundException;
import monetiqueActions.services.BankAccountServiceImpl;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class BankAccountController {
    private BankAccountServiceImpl bankAccountService;

    public BankAccountController(BankAccountServiceImpl bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/accounts/{accountId}")
    public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(accountId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/accounts")
    public List<BankAccountDTO> listAccounts(){
        return bankAccountService.bankAccountList();
    }

    @GetMapping("/accounts/{accountId}/operations")
    public List<AccountOperationDTO> getHistory(@PathVariable String accountId){
        return bankAccountService.accountHistory(accountId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/accounts/{accountId}/pageOperations")
    public AccountHistoryDTO getAccountHistory(
            @PathVariable String accountId,
            @RequestParam(name="page",defaultValue = "0") int page,
            @RequestParam(name="size",defaultValue = "5")int size) throws BankAccountNotFoundException {
        return bankAccountService.getAccountHistory(accountId,page,size);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/accounts/debit")
    public DebitDTO debit(@RequestBody DebitDTO debitDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        this.bankAccountService.debit(debitDTO.getAccountId(),debitDTO.getAmount(),debitDTO.getDescription());
        return debitDTO;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/accounts/credit")
    public CreditDTO credit(@RequestBody CreditDTO creditDTO) throws BankAccountNotFoundException {
        this.bankAccountService.credit(creditDTO.getAccountId(),creditDTO.getAmount(),creditDTO.getDescription());
        return creditDTO;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/accounts/transfer")
    public void transfer(@RequestBody TransferRequestDTO transferRequestDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        this.bankAccountService.transfer(
                transferRequestDTO.getAccountSource(),
                transferRequestDTO.getAccountDestination(),
                transferRequestDTO.getAmount());
    }
}
