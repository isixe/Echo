package dev.itea.echo.utils;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import dev.itea.echo.entity.recommend.Word;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Content base recommended algorithm utils
 *
 * @author: isixe
 * @create: 2024-03-22 14:41
 **/
@Slf4j
public class ContentBaseUtils {

    /**
     * get cosine similarity
     *
     * @param a vector
     * @param b vector
     */
    public static Double cosineSimilarity(List<Double> a, List<Double> b) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        // compute dot product
        for (int i = 0; i < a.size(); i++) {
            dotProduct += a.get(i) * b.get(i);
        }

        // compute norm for a
        for (Double value : a) {
            normA += value * value;
        }
        normA = Math.sqrt(normA);

        // compute norm for b
        for (Double value : b) {
            normB += value * value;
        }
        normB = Math.sqrt(normB);

        // compute similarity
        double similarity = dotProduct / (normA * normB);
        log.info("cosine similarity: {}", String.format("%.3f", similarity));
        return similarity;
    }

    /**
     * get document vetor
     *
     * @param corpus   all documents
     * @param segments segment list
     */
    public static List<Double> getDocVetor(Set<String> corpus, List<Word> segments) {
        List<Double> vector = new ArrayList<>();
        for (Word word : segments) {
            log.info(word.getName());

            log.info("TF (Term Frequency) with segment: {}", String.format("%.3f", word.getTf()));

            double idf = computeIDF(corpus, word.getName());
            log.info("IDF (Inverse Document Frequency) with segment: {}", String.format("%.3f", idf));

            double tf_idf = computeTF_IDF(word.getTf(), idf);
            log.info("TF-IDF（TF * IDF） with segment: {}", String.format("%.3f", tf_idf));

            vector.add(tf_idf);
        }

        return vector;
    }


    /**
     * compute TF-IDF（TF * IDF）
     *
     * @param tf  TF (Term Frequency)
     * @param idf IDF (Inverse Document Frequency)
     */
    public static double computeTF_IDF(double tf, double idf) {
        return tf * idf;
    }

    /**
     * Compute TF (Term Frequency)
     *
     * @param sentence text
     * @param word     segment
     */
    public static double computeTF(String sentence, String word) {
        // Segment the sentence
        List<Term> termList = HanLP.segment(sentence);
        // Remove stopwords
        termList.removeIf(term -> HanlpUtil.getStopwords().contains(term.word));

        // Calculate term frequency
        long count = termList.stream().filter(term -> word.equals(term.word)).count();
        return (double) count / termList.size();
    }

    /**
     * Compute IDF (Inverse Document Frequency)
     *
     * @param set  sentence set
     * @param word segment
     */
    public static double computeIDF(Set<String> set, String word) {
        int count = 0;
        for (String s : set) {
            List<Word> words = HanlpUtil.segment(s);
            words = new ArrayList<>(words.stream()
                    .collect(Collectors.toMap(Word::getName, Function.identity(), (existing, replacement) -> existing))
                    .values());

            for (Word w : words) {
                if (w.getName().equals(word)) {
                    count++;
                }
            }
        }
        return Math.log((double) count / set.size() + 1);
    }
}
