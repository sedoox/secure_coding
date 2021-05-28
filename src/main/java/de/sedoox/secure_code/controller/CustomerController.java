package de.sedoox.secure_code.controller;

import de.sedoox.secure_code.model.Customer;
import de.sedoox.secure_code.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerRepository repository;

    public CustomerController(CustomerRepository customerRepository) {
        this.repository = customerRepository;
    }

    @GetMapping
    public ResponseEntity getCustomer(@RequestParam(value = "id") int id) {
        if (repository.findById(id).isPresent()) {
            return new ResponseEntity<>(repository.findById(id).get().getEmail(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Keine Email für diese ID", HttpStatus.NOT_FOUND);
    }

    // akzeptiert JSON
    @PostMapping
    public ResponseEntity createCustomer(@RequestBody Customer customer) {
        try {
            repository.save(customer);
            return new ResponseEntity<>(customer.getEmail() + " wurde gespeichert!", HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity<>("Kunde wurde fehlerhaft übermittelt!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity deleteCustomer(@RequestParam(value = "id") int id) {
        if (repository.findById(id).isPresent()) {
            Customer customer = repository.findById(id).get();
            repository.delete(customer);
            return new ResponseEntity<>(customer.getEmail() + " wurde erfolgreich gelöscht!", HttpStatus.OK);
        }
        return new ResponseEntity<>("Keinen Kunden mit der ID " + id + " gefunden!", HttpStatus.NOT_FOUND);
    }

    //ExceptionsHandlers

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity handleMissingParams(MissingServletRequestParameterException exception) {
        String name = exception.getParameterName();
        return new ResponseEntity<>("Der Parameter '" + name + "' fehlt!", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity handleMissingParams(HttpMessageNotReadableException exception) {
        return new ResponseEntity<>("Body ist fehlerhaft", HttpStatus.BAD_REQUEST);
    }
}
