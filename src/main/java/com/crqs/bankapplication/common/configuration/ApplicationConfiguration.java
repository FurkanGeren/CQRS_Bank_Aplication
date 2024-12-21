package com.crqs.bankapplication.common.configuration;

import com.crqs.bankapplication.common.commands.customer.CreateCustomerCommand;
import com.crqs.bankapplication.common.commands.customer.LoginCustomerQuery;
import com.crqs.bankapplication.query.repository.CustomerRepository;
import jakarta.annotation.PostConstruct;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

@Configuration

public class ApplicationConfiguration {

    private final CommandBus commandBus;
    private final JwtService jwtService;
    private final CustomerRepository customerRepository;


    private static final Logger logger = LoggerFactory.getLogger(ApplicationConfiguration.class);


    public ApplicationConfiguration(CommandBus commandBus, JwtService jwtService, CustomerRepository customerRepository) {
        this.commandBus = commandBus;
        this.jwtService = jwtService;
        this.customerRepository = customerRepository;
    }


    @Bean
    public UserDetailsService userDetailsService() {
        return customerId -> customerRepository.findById(customerId)
                .orElseThrow();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider= new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)throws Exception{
        return configuration.getAuthenticationManager();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @PostConstruct
    public void registerDispatchInterceptor() {
        commandBus.registerDispatchInterceptor((MessageDispatchInterceptor<CommandMessage<?>>) messages -> (index, message) -> {
            String token = jwtService.extractAuthorizationToken();
            if (token != null) {
                return message.andMetaData(Map.of("Authorization", token));
            }
            return message;
        });
    }


    @PostConstruct
    public void registerHandlerInterceptor() {
        commandBus.registerHandlerInterceptor((unitOfWork, interceptorChain) -> {

            CommandMessage<?> commandMessage = unitOfWork.getMessage();
            logger.info(commandMessage.getPayload().toString());
            if (commandMessage.getPayloadType().equals(LoginCustomerQuery.class) || commandMessage.getPayloadType().equals(CreateCustomerCommand.class)) {
                return interceptorChain.proceed();
            }

            String token = (String) unitOfWork.getMessage().getMetaData().get("Authorization");

            System.out.println(token);

            if (token == null ) { //|| !token.startsWith("Bearer ")
                throw new SecurityException("Missing or invalid token");
            }

            if (!jwtService.validateToken(token)) {
                throw new SecurityException("Invalid token");
            }

            return interceptorChain.proceed();

        });
    }

}
