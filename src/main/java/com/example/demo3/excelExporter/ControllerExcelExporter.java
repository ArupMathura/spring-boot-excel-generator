package com.example.demo3.excelExporter;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ControllerExcelExporter {

    public void exportControllersToExcel(List<Class<?>> controllerClasses, String outputPath) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Controller Info");

        int rowIndex = 0;
        Row headerRow = sheet.createRow(rowIndex++);
        headerRow.createCell(0).setCellValue("Controller");
        headerRow.createCell(1).setCellValue("RequestMapping");
        headerRow.createCell(2).setCellValue("RequestMethod");
        headerRow.createCell(3).setCellValue("Method");

        for (Class<?> controllerClass : controllerClasses) {
            RestController restControllerAnnotation = controllerClass.getAnnotation(RestController.class);
            Controller controllerAnnotation = controllerClass.getAnnotation(Controller.class);
            if (restControllerAnnotation != null || controllerAnnotation != null) {
                RequestMapping classRequestMapping = controllerClass.getAnnotation(RequestMapping.class);
                String controllerRequestMapping = "";
                RequestMethod[] classRequestMethods = null;

                if (classRequestMapping != null) {
                    controllerRequestMapping = classRequestMapping.value()[0];
                    classRequestMethods = classRequestMapping.method();
                }

                Method[] methods = controllerClass.getDeclaredMethods();
                for (Method method : methods) {
                    RequestMapping methodRequestMapping = method.getAnnotation(RequestMapping.class);
                    if (methodRequestMapping != null) {
                        String methodRequestMappingValue = methodRequestMapping.value()[0];
                        RequestMethod[] methodRequestMethods = methodRequestMapping.method();

                        Row dataRow = sheet.createRow(rowIndex++);
                        dataRow.createCell(0).setCellValue(controllerClass.getSimpleName());
                        dataRow.createCell(1).setCellValue(controllerRequestMapping + methodRequestMappingValue);

                        if (methodRequestMethods != null && methodRequestMethods.length > 0) {
                            dataRow.createCell(2).setCellValue(methodRequestMethods[0].toString());
                        }

                        dataRow.createCell(3).setCellValue(method.getName());
                    }
                }
            }
        }

        try (FileOutputStream outputStream = new FileOutputStream(outputPath)) {
            workbook.write(outputStream);
        }
    }

    private List<Class<?>> getControllerClasses(String basePackage) {
        List<Class<?>> controllerClasses = new ArrayList<>();

        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(RestController.class));
        scanner.addIncludeFilter(new AnnotationTypeFilter(Controller.class));

        Set<org.springframework.beans.factory.config.BeanDefinition> candidates = scanner.findCandidateComponents(basePackage);
        for (org.springframework.beans.factory.config.BeanDefinition candidate : candidates) {
            try {
                Class<?> clazz = Class.forName(candidate.getBeanClassName());
                controllerClasses.add(clazz);
            } catch (ClassNotFoundException e) {
                // Handle the exception if needed
            }
        }

        return controllerClasses;
    }

    public static void main(String[] args) {
        ControllerExcelExporter exporter = new ControllerExcelExporter();
        String basePackage = "com.example.demo3.controller"; // Adjust this package
        List<Class<?>> controllerClasses = exporter.getControllerClasses(basePackage);
        String outputPath = "D:\\Eclipse-Workspace\\demo3\\excel\\ControllerInfo.xlsx";

        try {
            exporter.exportControllersToExcel(controllerClasses, outputPath);
            System.out.println("Data exported successfully.");
        } catch (IOException e) {
            System.err.println("Error exporting data: " + e.getMessage());
        }
    }
}

