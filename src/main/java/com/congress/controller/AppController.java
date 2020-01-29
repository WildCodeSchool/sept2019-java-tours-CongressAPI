package com.congress.controller;

import com.congress.storage.StorageFileNotFoundException;
import com.congress.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.file.Files;

@Controller
public class AppController {
    private StorageService storageService;

    @Autowired
    public void FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }


    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("pageTitle", "Accueil");
        model.addAttribute("page", "accueil");

        return "pages/index";
    }

    /*
        STORAGE Management
    */

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }


}
