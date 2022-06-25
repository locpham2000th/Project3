package com.example.project3.mqtt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;

import static java.lang.System.out;

@Configuration
public class OutConfiguration{

    public void test() {
        out.println("run this function");
    }
//    @Bean
//    RouterFunction<ServerResponse> http(MessageChannel out){
//        return route()
//                .GET("/send/{name}", request -> {
//                    String name = request.pathVariable("name");
//                    Message<String> message = MessageBuilder.withPayload("Hello, HiveMQ and "+ name + "!").build();
//                    out.send(message);
//                    return ServerResponse.ok().build();
//                })
//                .build();
//    }

    @Bean
    public MqttPahoMessageHandler outboundAdapter(
            @Value("${hivemq.command}") String topic,
            MqttPahoClientFactory factory){
        MqttPahoMessageHandler mh = new MqttPahoMessageHandler("producer", factory);
        mh.setDefaultTopic(topic);
        return mh;
    }

    @Bean
    public IntegrationFlow outboundFlow(MessageChannel out,
                                        MqttPahoMessageHandler outboundAdapter){
        return IntegrationFlows
                .from(out)
                .handle(outboundAdapter)
                .get();
    }

    @Bean
    public MessageChannel out(){
        return MessageChannels.direct().get();
    }

}
