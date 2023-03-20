package com.example.dodam.config;

import com.example.dodam.repository.inquiry.InquiryRepository;
import com.example.dodam.repository.inquiry.JdbcInquiryRepository;
import com.example.dodam.service.inquiry.InquiryService;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@org.springframework.context.annotation.Configuration
public class InquiryConfig {
    private final DataSource dataSource;

    public InquiryConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public InquiryService inquiryService(){
        return new InquiryService(inquiryRepository());
    }
    @Bean
    public InquiryRepository inquiryRepository(){
        return new JdbcInquiryRepository(dataSource);
    }
}
