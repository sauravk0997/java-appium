package com.disney.heath_check.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MultiverseDevicesResponse {
    private String id;
    private String status;
}
