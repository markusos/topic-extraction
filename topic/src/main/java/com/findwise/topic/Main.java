package com.findwise.topic;

public class Main {
    public static void main (String[] args) {
        if(args.length <= 0) {
            invalidInput();
        }
        else if(args[0].equalsIgnoreCase("-i")) {
            com.findwise.topic.indexer.Main.run();
        }
        else if(args[0].equalsIgnoreCase("-e")) {
            com.findwise.topic.extractor.Main.run();
        }
        else {
            invalidInput();
        }
    }

    private static void invalidInput() {
        System.out.println("Use the flag -i to index topic data, or the flag -e to extract topic data from the input file");
    }
}


