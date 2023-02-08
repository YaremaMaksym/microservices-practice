package com.xsakon.customer;

import com.xsakon.amqp.RabbitMQMessageProducer;
import com.xsakon.clients.fraud.FraudCheckResponse;
import com.xsakon.clients.fraud.FraudClient;
import com.xsakon.clients.notification.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final FraudClient fraudClient;
    private final RabbitMQMessageProducer rabbitMQMessageProducer;

    @Value("${rabbitmq.exchanges.internal}")
    private String internalExchange;

    @Value("${rabbitmq.routing-keys.internal-notification}")
    private String internalNotificationRoutingKey;

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



        NotificationRequest notificationRequest = new NotificationRequest(
                customer.getId(),
                customer.getEmail(),
                String.format("Hi %s, welcome to xsakon's site", customer.getFirstName())
        );

        rabbitMQMessageProducer.publish(
                internalExchange,
                internalNotificationRoutingKey,
                notificationRequest
        );

    }
}
