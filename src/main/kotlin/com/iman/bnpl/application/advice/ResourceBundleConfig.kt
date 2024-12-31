package com.iman.bnpl.application.advice

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
class ResourceBundleConfig {
    
    @Bean
    fun defaultBundleMessage(): ResourceBundle {
        return ResourceBundle.getBundle("bundles/messages")
    }
}