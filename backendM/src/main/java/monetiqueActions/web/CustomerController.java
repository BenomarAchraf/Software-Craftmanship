package monetiqueActions.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import monetiqueActions.dtos.CustomerDTO;
import monetiqueActions.exceptions.CustomerNotFoundException;
import monetiqueActions.services.BankAccountService;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")
@RequestMapping("/api")
public class CustomerController {
    private BankAccountService bankAccountService;

    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/customers")
    public List<CustomerDTO> customers(){
        return bankAccountService.listCustomers();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/customers/search")
    public List<CustomerDTO> searchCustomers(@RequestParam(name = "keyword",defaultValue = "") String keyword){
        return bankAccountService.searchCustomers("%"+keyword+"%");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/customers/{id}")
    public CustomerDTO getCustomer(@PathVariable(name = "id") Long customerId) throws CustomerNotFoundException {
        return bankAccountService.getCustomer(customerId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/customers")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        return bankAccountService.saveCustomer(customerDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/customers/{customerId}")
    public CustomerDTO updateCustomer(@PathVariable Long customerId, @RequestBody CustomerDTO customerDTO){
        customerDTO.setId(customerId);
        return bankAccountService.updateCustomer(customerDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/customers/{id}")
    public void deleteCustomer(@PathVariable Long id){
        bankAccountService.deleteCustomer(id);
    }
}
