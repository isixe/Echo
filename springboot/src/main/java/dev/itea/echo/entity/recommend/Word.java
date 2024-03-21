package dev.itea.echo.entity.recommend;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * @author: isixe
 * @create: 2024-03-20 08:42
 * @description: 分词对象
 **/
@Data
@NoArgsConstructor
public class Word implements Comparable<Word> {
    private String name;
    private String pos;
    private double tf;
    private double tf_idf;

    public Word(String name, String pos) {
        this.name = name;
        this.pos = pos;
    }

    @Override
    public int compareTo(Word word) {
        if (word == null || word.name == null) {
            return 1;
        }

        if (this.name == null) {
            return -1;
        }

        return this.name.compareTo(word.name);
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (name != null) {
            stringBuilder.append(name);
        }
        if (pos != null) {
            stringBuilder.append("/").append(pos);
        }

        return stringBuilder.toString();
    }

}
