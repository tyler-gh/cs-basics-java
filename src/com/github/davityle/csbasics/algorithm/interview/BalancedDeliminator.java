package com.github.davityle.csbasics.algorithm.interview;


import com.github.davityle.csbasics.data.list.LinkedList;
import com.github.davityle.csbasics.data.list.Stack;

import java.util.Optional;

public class BalancedDeliminator {
    BalancedDeliminator() {}

    private enum Deliminator {
        CURLY('{', '}'), BRACKET('[', ']'), PAREN('(', ')');

        private final char open, close;

        Deliminator(char open, char close) {
            this.open = open;
            this.close = close;
        }

        static Optional<Deliminator> open(char c) {
            for(Deliminator d : values()) {
                if(d.open == c)
                    return Optional.of(d);
            }
            return Optional.empty();
        }

        static Optional<Deliminator> close(char c) {
            for(Deliminator d : values()) {
                if(d.close == c)
                    return Optional.of(d);
            }
            return Optional.empty();
        }
    }

    public static boolean areDeliminatorsBalanced(String s) {
        Stack<Deliminator> stack = new LinkedList<>();
        char[] chars = s.toCharArray();
        for (char c : chars) {
            Deliminator.open(c).ifPresent(stack::push);
            Optional<Deliminator> close = Deliminator.close(c);
            if(close.isPresent()) {
                if(stack.getSize() == 0 || !stack.pop().equals(close.get()))
                    return false;
            }
        }
        return stack.getSize() == 0;
    }


}
