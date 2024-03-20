package dev.itea.echo.utils;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import dev.itea.echo.entity.recommend.Word;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * HanLP utils
 *
 * @author: isixe
 * @create: 2024-03-19 20:34
 **/
@Slf4j
public class HanlpUtil {

    /**
     * get HanLP segment
     *
     * @param sentence text
     */
    public static List<Word> segment(String sentence) throws IOException {
        //get segment list
        List<Term> termList = HanLP.segment(sentence);

        //remove stop words
        List<String> stopwords = Files.readAllLines(Paths.get("src/main/resources/data/dictionary/stopwords.txt"));
        termList.removeIf(term -> stopwords.contains(term.word));

        //converted to word object
        return termList.stream()
                .filter(term -> !term.word.equals(" "))
                .map(term -> new Word(term.word, term.nature.toString()))
                .collect(Collectors.toList());
    }
}
