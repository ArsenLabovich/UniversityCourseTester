package org.example.strtools;

import difflib.DiffUtils;
import difflib.Patch;

import java.util.Arrays;
import java.util.List;

public class StringComparator {

    public static boolean isEquivalentByDiff(String lhs, String rhs) {
        List<String> leftLines = normalizeLines(lhs);
        List<String> rightLines = normalizeLines(rhs);

        Patch<String> patch = DiffUtils.diff(leftLines, rightLines);
        return patch.getDeltas().isEmpty();
    }

    public static boolean isEquivalent(String lhs, String rhs) {
        List<String> leftLines = lhs.lines().toList();
        List<String> rightLines = rhs.lines().toList();

        Patch<String> patch = DiffUtils.diff(leftLines, rightLines);
        return patch.getDeltas().isEmpty();
    }

    private static List<String> normalizeLines(String input) {
        return Arrays.stream(input.split("\\R"))
                .map(String::trim)
                .map(s -> s.replaceAll("\\s+", " "))
                .toList();
    }
}
