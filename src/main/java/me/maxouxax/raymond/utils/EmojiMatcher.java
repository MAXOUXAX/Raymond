package me.maxouxax.raymond.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public enum EmojiMatcher {

    ONE("1", "one"),
    TWO("2", "two"),
    THREE("3", "three"),
    FOUR("4", "four"),
    FIVE("5", "five"),
    SIX("6", "six"),
    SEVEN("7", "seven"),
    EIGHT("8", "eight"),
    NINE("9", "nine"),
    ;

    private String name;
    private String value;

    EmojiMatcher(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static EmojiMatcher getEmojiFromInteger(int number) {
        return Arrays.stream(values()).collect(Collectors.toCollection(ArrayList::new)).stream().filter(emojiMatcher -> emojiMatcher.getValue().equalsIgnoreCase(String.valueOf(number))).findFirst().get();
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

}
