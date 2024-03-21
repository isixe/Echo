package dev.itea.echo.utils;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import dev.itea.echo.entity.recommend.Word;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * HanLP utils
 *
 * @author: isixe
 * @create: 2024-03-19 20:34
 **/
@Slf4j
public class HanlpUtil {

    private final static String STOP_WORD_DICT_PATH = "/data/dictionary/stopwords.txt";

    /**
     * get HanLP segment
     *
     * @param sentence text
     */
    public static List<Word> segment(String sentence) throws IOException {
        // Load stopwords into a set for faster lookup
        String classpath = ResourceUtils.getURL("classpath:").getPath().substring(1);
        Path targetPath = Paths.get(classpath, STOP_WORD_DICT_PATH);
        Stream<String> lines = Files.lines(targetPath);
        Set<String> stopwords = lines.collect(Collectors.toSet());

        // Segment the sentence
        List<Term> termList = HanLP.segment(sentence);

        // Remove stopwords
        termList.removeIf(term -> stopwords.contains(term.word));

        // Convert to Word objects
        return termList.stream()
                .filter(term -> !term.word.equals(" "))
                .map(term -> new Word(term.word, term.nature.toString()))
                .collect(Collectors.toList());
    }

    /**
     * compute TF-IDF（TF * IDF）
     *
     * @param set      sentence set
     * @param sentence text
     * @param word     segment
     */
    public static double getTF_IDF(Set<String> set, String sentence, String word) throws IOException {
        double tf = computeTF(sentence, word);
        double idf = computeIDF(set, word);
        log.info("TF-IDF（TF * IDF） with segment: {}", String.format("%.3f", idf));
        return tf * idf;
    }

    /**
     * Compute TF (Term Frequency)
     *
     * @param sentence text
     * @param word     segment
     */
    public static double computeTF(String sentence, String word) throws IOException {
        // Load stopwords into a set for faster lookup
        String classpath = ResourceUtils.getURL("classpath:").getPath().substring(1);
        Path targetPath = Paths.get(classpath, STOP_WORD_DICT_PATH);
        Stream<String> lines = Files.lines(targetPath);
        Set<String> stopwords = lines.collect(Collectors.toSet());

        // Segment the sentence
        List<Term> termList = HanLP.segment(sentence);

        // Remove stopwords
        termList.removeIf(term -> stopwords.contains(term.word));

        // Calculate term frequency
        long count = termList.stream().filter(term -> word.equals(term.word)).count();
        double tf = (double) count / termList.size();
        log.info("TF (Term Frequency) in the segment sentence: {}", String.format("%.3f", tf));
        return tf;
    }

    /**
     * Compute IDF (Inverse Document Frequency)
     *
     * @param set  sentence set
     * @param word segment
     */
    public static double computeIDF(Set<String> set, String word) throws IOException {
        int count = 0;
        for (String s : set) {
            List<Word> words = segment(s);
            for (Word w : words) {
                if (w.getName().equals(word)) {
                    count++;
                }
            }
        }
        double idf = Math.log((double) count / set.size());
        log.info("IDF (Inverse Document Frequency) in all document: {}", String.format("%.3f", idf));
        return idf;
    }

}
