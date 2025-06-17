package br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.util;

import lombok.extern.slf4j.Slf4j;
import net.sourceforge.pmd.PMDConfiguration;
import net.sourceforge.pmd.PmdAnalysis;
import net.sourceforge.pmd.lang.Language;
import net.sourceforge.pmd.lang.LanguageRegistry;
import net.sourceforge.pmd.lang.LanguageVersion;
import net.sourceforge.pmd.lang.LanguageVersionDiscoverer;
import net.sourceforge.pmd.lang.document.FileId;
import net.sourceforge.pmd.lang.document.TextFile;
import net.sourceforge.pmd.lang.document.TextFileContent;
import net.sourceforge.pmd.lang.rule.RuleSet;
import net.sourceforge.pmd.lang.rule.RuleSetLoader;
import net.sourceforge.pmd.reporting.Report;

import java.nio.file.Path;
import java.util.List;

@Slf4j
public class PMDExecutor {

    private static final String CACHE_PATH = "tmp/pmd-analysis-cache.ser";

    private static LanguageVersion resolveVersion(PMDConfiguration config, String filePath) {
        Language javaLang = LanguageRegistry.PMD.getLanguageById("java");
        LanguageVersionDiscoverer discoverer = config.getLanguageVersionDiscoverer();

        LanguageVersion version = discoverer.getDefaultLanguageVersionForFile(filePath);
        if (version == null && javaLang != null) {
            version = javaLang.getDefaultVersion();
        }

        return version;
    }

    private static PMDConfiguration createConfigWithCache() {
        PMDConfiguration config = new PMDConfiguration();
        config.setAnalysisCacheLocation(CACHE_PATH);
        return config;
    }

    public static Report analyzeFile(String filePath, String fileContent) {
        PMDConfiguration config = createConfigWithCache();
        log.atInfo().log("Analyzing file: {}", filePath);

        try (PmdAnalysis pmd = PmdAnalysis.create(config)) {
            LanguageVersion version = resolveVersion(config, filePath);

            RuleSetLoader loader = RuleSetLoader.fromPmdConfig(config);
            RuleSet ruleSet = loader.loadFromResource("rulesets/java/quickstart.xml");
            pmd.addRuleSet(ruleSet);

            FileId fileId = FileId.fromPath(Path.of(filePath));
            TextFileContent content = TextFileContent.fromCharSeq(fileContent);
            TextFile textFile = TextFile.forCharSeq(content.getNormalizedText(), fileId, version);

            pmd.files().addFile(textFile);

            return pmd.performAnalysisAndCollectReport();
        }
    }

    public static Report analyzeMultipleFiles(List<AnalyzableFile> files) {
        PMDConfiguration config = createConfigWithCache();

        try (PmdAnalysis pmd = PmdAnalysis.create(config)) {
            RuleSetLoader loader = RuleSetLoader.fromPmdConfig(config);
            RuleSet ruleSet = loader.loadFromResource("rulesets/java/quickstart.xml");
            pmd.addRuleSet(ruleSet);

            for (AnalyzableFile file : files) {
                LanguageVersion version = resolveVersion(config, file.filePath());
                FileId fileId = FileId.fromPath(Path.of(file.filePath()));
                TextFileContent content = TextFileContent.fromCharSeq(file.content());
                TextFile textFile = TextFile.forCharSeq(content.getNormalizedText(), fileId, version);
                pmd.files().addFile(textFile);
            }

            return pmd.performAnalysisAndCollectReport();
        }
    }
}
