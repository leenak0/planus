package com.planus.plan.controller;

import com.planus.plan.dto.PlanResDTO;
import com.planus.plan.dto.PlanSaveReqDTO;
import com.planus.plan.service.PlanService;
import io.swagger.models.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/plans")
public class PlanController {
    @Autowired
    PlanService planService;

    @GetMapping("/{tripId}")
    public ResponseEntity getPlan(@PathVariable long tripId) {
        Map<String, Object> resultMap = new HashMap<>();

        try {
            List<PlanResDTO> planResDTOList = planService.readPlan(tripId);
            resultMap.put("planList", planResDTOList);
            resultMap.put("message", "success");
            return new ResponseEntity(resultMap, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("message", "일정 조회 오류");
            return new ResponseEntity(resultMap, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/")
    public ResponseEntity savePlan(@RequestBody PlanSaveReqDTO planSaveReqDTO) {
        Map<String, Object> resultMap = new HashMap<>();

        try {
             // redis에 저장되어 있는 값을 바로 mysql에 저장?
            // 혹은 PlanSaveReqDTO를 받아 mysql에 저장?
            long tripId = planService.savePlan(planSaveReqDTO);
            resultMap.put("message", "success");
            return new ResponseEntity(resultMap, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("message", "일정 저장 오류");
            return new ResponseEntity(resultMap, HttpStatus.BAD_REQUEST);
        }
    }

}
