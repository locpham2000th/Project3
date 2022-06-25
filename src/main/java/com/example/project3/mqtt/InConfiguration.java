package com.example.project3.mqtt;

import com.example.project3.service.ReceiveData;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;

import static java.lang.System.out;

@Configuration
class InConfiguration{

    private final ReceiveData receiveData;

    InConfiguration(ReceiveData receiveData) {
        this.receiveData = receiveData;
    }

    @Bean
    IntegrationFlow inboundFlow(MqttPahoMessageDrivenChannelAdapter inboundAdapter){
        return IntegrationFlows
                .from(inboundAdapter)
                .handle((GenericHandler<String>) (payload, headers) -> {
                    out.println("new message! " + payload);
                    headers.forEach((k,v) -> out.println(k + "=" + v));
                    receiveData.receiveData(payload);

                    return null;
                })
                .get();
    }

    @Bean
    MqttPahoMessageDrivenChannelAdapter inboundAdapter(@Value("${hivemq.topic}")String topic,
                                                       MqttPahoClientFactory clientFactory){
        return new MqttPahoMessageDrivenChannelAdapter("consumer", clientFactory, topic);
    }


}
