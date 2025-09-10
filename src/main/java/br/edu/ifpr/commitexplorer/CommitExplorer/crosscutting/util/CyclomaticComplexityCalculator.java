package br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.util;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

@Slf4j
public class CyclomaticComplexityCalculator {
    
    private CyclomaticComplexityCalculator() {
    }

    public static class ComplexityResult {
        private final double averageComplexity;
        private final int totalMethods;
        private final List<MethodComplexity> methodComplexities;

        public ComplexityResult(double averageComplexity, int totalMethods, List<MethodComplexity> methodComplexities) {
            this.averageComplexity = averageComplexity;
            this.totalMethods = totalMethods;
            this.methodComplexities = methodComplexities;
        }

        public double getAverageComplexity() {
            return averageComplexity;
        }

        public int getTotalMethods() {
            return totalMethods;
        }

        public List<MethodComplexity> getMethodComplexities() {
            return methodComplexities;
        }
    }

    public static class MethodComplexity {
        private final String methodName;
        private final int complexity;
        private final int lineNumber;

        public MethodComplexity(String methodName, int complexity, int lineNumber) {
            this.methodName = methodName;
            this.complexity = complexity;
            this.lineNumber = lineNumber;
        }

        public String getMethodName() {
            return methodName;
        }

        public int getComplexity() {
            return complexity;
        }

        public int getLineNumber() {
            return lineNumber;
        }
    }

    public static ComplexityResult calculateComplexity(String filePath, String fileContent) {
        log.debug("Calculating cyclomatic complexity for file: {}", filePath);

        try {
            return calculateComplexityByDecisionPoints(fileContent);

        } catch (Exception e) {
            log.warn("Erro ao calcular complexidade ciclomática para {}: {}", filePath, e.getMessage());
            return new ComplexityResult(1.0, 0, new ArrayList<>());
        }
    }

    private static ComplexityResult calculateComplexityByDecisionPoints(String fileContent) {
        List<MethodComplexity> methodComplexities = new ArrayList<>();
        
        String[] lines = fileContent.split("\n");
        
        int currentMethodComplexity = 1;
        String currentMethodName = "unknown";
        int methodStartLine = 1;
        boolean inMethod = false;
        
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            
            if (line.matches(".*\\b(public|private|protected|static|final).*\\w+\\s*\\(.*\\).*\\{.*") ||
                line.matches(".*\\w+\\s*\\(.*\\).*\\{.*")) {
                
                if (inMethod) {
                    methodComplexities.add(new MethodComplexity(currentMethodName, currentMethodComplexity, methodStartLine));
                }
                
                currentMethodName = extractMethodName(line);
                currentMethodComplexity = 1;
                methodStartLine = i + 1;
                inMethod = true;
            }
            
            if (inMethod) {
                currentMethodComplexity += countDecisionPoints(line);
            }
            
            if (line.equals("}") && inMethod) {
                methodComplexities.add(new MethodComplexity(currentMethodName, currentMethodComplexity, methodStartLine));
                inMethod = false;
            }
        }
        
        if (inMethod) {
            methodComplexities.add(new MethodComplexity(currentMethodName, currentMethodComplexity, methodStartLine));
        }

        OptionalDouble average = methodComplexities.stream()
                .mapToInt(MethodComplexity::getComplexity)
                .average();

        double averageComplexity = average.orElse(1.0);
        int totalMethods = methodComplexities.size();

        return new ComplexityResult(averageComplexity, totalMethods, methodComplexities);
    }
    
    private static String extractMethodName(String line) {
        try {
            String cleaned = line.replaceAll("\\s+", " ");
            String[] parts = cleaned.split("\\(")[0].split("\\s+");
            return parts[parts.length - 1];
        } catch (Exception e) {
            return "unknown";
        }
    }
    
    private static int countDecisionPoints(String line) {
        int points = 0;
        String lowerLine = line.toLowerCase();
        
        points += countOccurrences(lowerLine, "if");
        points += countOccurrences(lowerLine, "else if");
        points += countOccurrences(lowerLine, "while");
        points += countOccurrences(lowerLine, "for");
        points += countOccurrences(lowerLine, "case");
        points += countOccurrences(lowerLine, "catch");
        points += countOccurrences(lowerLine, "&&");
        points += countOccurrences(lowerLine, "||");
        points += countOccurrences(lowerLine, "?");
        
        return points;
    }
    
    private static int countOccurrences(String text, String pattern) {
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(pattern, index)) != -1) {
            count++;
            index += pattern.length();
        }
        return count;
    }

    public static double calculateAverageComplexityForFiles(List<String> filePaths, List<String> fileContents) {
        if (filePaths.size() != fileContents.size()) {
            throw new IllegalArgumentException("O número de caminhos de arquivo deve corresponder ao número de conteúdos");
        }

        List<Double> complexities = new ArrayList<>();
        
        for (int i = 0; i < filePaths.size(); i++) {
            ComplexityResult result = calculateComplexity(filePaths.get(i), fileContents.get(i));
            if (result.getTotalMethods() > 0) {
                complexities.add(result.getAverageComplexity());
            }
        }

        return complexities.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(1.0);
    }
}
