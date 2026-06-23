package com.web.webhook.controller;

import com.web.webhook.dto.requestDto.TemplateRequestDto;
import com.web.webhook.dto.responseDto.TemplateResponseDto;
import com.web.webhook.service.TemplateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/templates")
public class TemplateController {

    private final TemplateService templateService;

    public TemplateController(
            TemplateService templateService) {

        this.templateService = templateService;
    }

    @PostMapping("/create-template")
    public ResponseEntity<TemplateResponseDto>
    createTemplate(
            @RequestBody TemplateRequestDto requestDto){

        return ResponseEntity.ok(
                templateService.createTemplate(
                        requestDto));
    }

    @GetMapping("/get-all-templates")
    public ResponseEntity<List<TemplateResponseDto>>
    getAllTemplates(){

        return ResponseEntity.ok(
                templateService.getAllTemplates());
    }

    @GetMapping("/get-template-by-id/{id}")
    public ResponseEntity<TemplateResponseDto>
    getTemplateById(
            @PathVariable Long id){

        return ResponseEntity.ok(
                templateService.getTemplateById(id));
    }

    @PutMapping("/update-template/{id}")
    public ResponseEntity<TemplateResponseDto>
    updateTemplate(
            @PathVariable Long id,
            @RequestBody TemplateRequestDto requestDto){

        return ResponseEntity.ok(
                templateService.updateTemplate(
                        id,
                        requestDto));
    }

    @PatchMapping("/patch-template/{id}")
    public ResponseEntity<TemplateResponseDto>
    patchTemplate(
            @PathVariable Long id,
            @RequestBody TemplateRequestDto requestDto){

        return ResponseEntity.ok(
                templateService.patchTemplate(
                        id,
                        requestDto));
    }

    @DeleteMapping("/delete-template/{id}")
    public ResponseEntity<String>
    deleteTemplate(
            @PathVariable Long id){

        templateService.deleteTemplate(id);

        return ResponseEntity.ok(
                "Template Deleted Successfully");
    }
}