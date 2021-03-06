package com.github.mikesafonov.prometheus.telegram.config;

import com.github.mikesafonov.prometheus.telegram.service.message.CustomMessageConverter;
import com.github.mikesafonov.prometheus.telegram.service.message.EmojiService;
import com.github.mikesafonov.prometheus.telegram.service.message.MessageConverter;
import com.github.mikesafonov.prometheus.telegram.service.message.SimpleMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * @author MikeSafonov
 */
@Configuration
public class MessageConverterConfig {

    @Bean
    @ConditionalOnMissingBean(MessageConverter.class)
    public MessageConverter simpleMessageConverter(EmojiService emojiService) {
        return new CustomMessageConverter(emojiService);
    }

    @Bean
    public RestTemplate restTemplate(TelegramProperties telegramProperties) {
        TelegramProxyProperties telegramPropertiesProxy = telegramProperties.getProxy();
        if (telegramPropertiesProxy.isEnable()) {
            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
            InetSocketAddress address = new InetSocketAddress(telegramPropertiesProxy.getHost(), telegramPropertiesProxy.getPort());
            Proxy proxy = new Proxy(Proxy.Type.HTTP, address);
            factory.setProxy(proxy);
            return new RestTemplate(factory);
        } else {
            return new RestTemplate();
        }
    }
}
