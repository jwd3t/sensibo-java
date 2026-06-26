package com.sensibo.platform.pc26010u202318609.registry.interfaces.rest;

import com.sensibo.platform.pc26010u202318609.registry.application.commandservices.DeviceCommandService;
import com.sensibo.platform.pc26010u202318609.registry.application.queryservices.DeviceQueryService;
import com.sensibo.platform.pc26010u202318609.registry.domain.model.aggregates.Device;
import com.sensibo.platform.pc26010u202318609.registry.domain.model.queries.GetDeviceById;
import com.sensibo.platform.pc26010u202318609.registry.interfaces.rest.resources.CreateDeviceResource;
import com.sensibo.platform.pc26010u202318609.registry.interfaces.rest.transform.CreateDeviceCommandFromResourceAssembler;
import com.sensibo.platform.pc26010u202318609.registry.interfaces.rest.transform.DeviceResourceFromEntityAssembler;
import com.sensibo.platform.pc26010u202318609.shared.application.result.ApplicationError;
import com.sensibo.platform.pc26010u202318609.shared.application.result.Result;
import com.sensibo.platform.pc26010u202318609.shared.interfaces.rest.transform.ResponseEntityAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/devices")
public class DevicesController {

  private final DeviceCommandService deviceCommandService;
  private final DeviceQueryService deviceQueryService;

  public DevicesController(DeviceCommandService deviceCommandService, DeviceQueryService deviceQueryService) {
    this.deviceCommandService = deviceCommandService;
    this.deviceQueryService = deviceQueryService;
  }

  @PostMapping
  public ResponseEntity<?>  createDevice(@RequestBody CreateDeviceResource resource) {
    var command = CreateDeviceCommandFromResourceAssembler.toCommandFromResource(resource);
    var result = this.deviceCommandService.handle(command)
        .flatMap(deviceId -> this.deviceQueryService.handle(new GetDeviceById(deviceId))
            .<Result<Device, ApplicationError>>map(Result::success)
            .orElseGet(() -> Result.failure(ApplicationError.notFound("Device", deviceId.toString()))));

    return ResponseEntityAssembler.toResponseEntityFromResult(
        result, DeviceResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED
    );

  }

}
