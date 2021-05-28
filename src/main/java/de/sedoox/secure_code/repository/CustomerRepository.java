package de.sedoox.secure_code.repository;

import de.sedoox.secure_code.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
