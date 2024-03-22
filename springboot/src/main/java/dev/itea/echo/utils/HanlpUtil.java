package dev.itea.echo.utils;

import ch.qos.logback.core.util.COWArrayList;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import dev.itea.echo.entity.recommend.Word;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * HanLP utils
 *
 * @author: isixe
 * @create: 2024-03-19 20:34
 **/
public class HanlpUtil {

    private final static String STOP_WORD_DICT_PATH = "/data/dictionary/stopwords.txt";
    private static Set<String> stopwords;

    public static Set<String> getStopwords() {
        return stopwords;
    }

    static {
        try {
            // Load stopwords into a set for faster lookup
            String classpath = ResourceUtils.getURL("classpath:").getPath().substring(1);
            Path targetPath = Paths.get(classpath, STOP_WORD_DICT_PATH);
            Stream<String> lines = Files.lines(targetPath);
            stopwords = lines.collect(Collectors.toSet());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * get HanLP segment
     *
     * @param sentence text
     */
    public static List<Word> segment(String sentence) {
        // Segment the sentence
        List<Term> termList = HanLP.segment(sentence);

        // Remove stopwords
        termList.removeIf(term -> stopwords.contains(term.word));

        // Convert to Word objects
        return new ArrayList<>(termList.stream()
                .filter(term -> !term.word.equals(" ") && term.word.length() > 1)
                .map(term -> new Word(term.word, term.nature.toString(), 0.0))
                .collect(Collectors.toMap(Word::getName, Function.identity(), (existing, replacement) -> existing))
                .values());
    }
}
