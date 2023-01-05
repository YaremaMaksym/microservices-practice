package com.xsakon.customer;

import com.xsakon.clients.fraud.FraudCheckResponse;
import com.xsakon.clients.fraud.FraudClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final FraudClient fraudClient;


    public  void registerCustomer(CustomerRegistrationRequest request){

        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();

        // todo: check if email valid
        // todo: check if email not taken
        customerRepository.saveAndFlush(customer);


        FraudCheckResponse fraudCheckResponse =
                fraudClient.isFraudster(customer.getId());


        if (fraudCheckResponse.isFraudster()){
            throw new IllegalStateException("fraudster");
        }

        // todo: send notification
    }
}
