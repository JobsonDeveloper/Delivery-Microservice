package br.com.delivery.micro.service;

import br.com.delivery.micro.event.dto.response.GetUserInfoDto;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "user-microservice",
        url = "${user.micro.url}"
)
public interface IUserDelivery {

    @GetMapping("/api/user/{id}/info")
    GetUserInfoDto getUserInfo(
            @Parameter(description = "User id", required = true)
            @PathVariable String id
    );
}
