package br.com.delivery.micro.service;

import br.com.delivery.micro.event.dto.response.GetClientInfoDto;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "client-microservice",
        url = "${client.micro.url}"
)
public interface IClientDelivery {

    @GetMapping("/api/client/{id}/info")
    GetClientInfoDto getClientInfo(
            @Parameter(description = "Client id", required = true)
            @PathVariable String id
    );
}
