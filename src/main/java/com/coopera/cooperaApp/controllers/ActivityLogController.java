package com.coopera.cooperaApp.controllers;

import com.coopera.cooperaApp.dtos.response.ApiResponse;
import com.coopera.cooperaApp.models.ActivityLog;
import com.coopera.cooperaApp.services.activityLogServices.ActivityLogService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/logs/")
@AllArgsConstructor
public class ActivityLogController {
    private final ActivityLogService activityLogService;

    @GetMapping("findAllLogsByMemberId")
    public ResponseEntity<ApiResponse<?>> findAllLogsByMemberId(
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int items
    )  {
        List<ActivityLog> response =  activityLogService.findByMemberId(page, items);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.builder()
                                .message("Logs retrieved successfully")
                                .data(response).success(true).build()
                );
    }


    @GetMapping("/findAllLogsByCooperativeId")
    public ResponseEntity<ApiResponse<?>> findAllLogsByCooperativeId(
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int items
    )  {
        List<ActivityLog> response =  activityLogService.findByCooperativeId(page, items);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.builder()
                                .message("Logs retrieved successfully")
                                .data(response).success(true).build()
                );
    }
}
