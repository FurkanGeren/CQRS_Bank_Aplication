package com.crqs.bankapplication.command.controller;


import com.crqs.bankapplication.common.commands.customer.CreateCustomerCommand;
import com.crqs.bankapplication.common.dto.CreateCustomerRequestDTO;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerCommandController {

    private final CommandGateway commandGateway;


    public CustomerCommandController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }


    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CreateCustomerRequestDTO dto) {
         commandGateway.send(new CreateCustomerCommand(
                 UUID.randomUUID().toString(), dto.firstName(),
                 dto.lastName(), dto.citizenID(),
                 dto.sex()
        ));
         return ResponseEntity.ok().build();
    }


}
